package com.marcarndt.morse;

import com.marcarndt.morse.command.BaseCommand;
import com.marcarndt.morse.command.commandlets.Commandlet;
import com.marcarndt.morse.service.StateService;
import com.marcarndt.morse.telegrambots.TelegramBotsApi;
import com.marcarndt.morse.telegrambots.api.methods.send.SendMessage;
import com.marcarndt.morse.telegrambots.api.objects.Chat;
import com.marcarndt.morse.telegrambots.api.objects.Contact;
import com.marcarndt.morse.telegrambots.api.objects.Message;
import com.marcarndt.morse.telegrambots.api.objects.Update;
import com.marcarndt.morse.telegrambots.api.objects.User;
import com.marcarndt.morse.telegrambots.api.objects.replykeyboard.ForceReplyKeyboard;
import com.marcarndt.morse.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import com.marcarndt.morse.telegrambots.api.objects.replykeyboard.ReplyKeyboardRemove;
import com.marcarndt.morse.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import com.marcarndt.morse.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import com.marcarndt.morse.telegrambots.bots.TelegramLongPollingCommandBot;
import com.marcarndt.morse.telegrambots.exceptions.TelegramApiException;
import com.marcarndt.morse.telegrambots.exceptions.TelegramApiRequestException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

/**
 * Created by arndt on 2017/04/06.
 */
@Singleton
@Startup
public class MorseBot extends TelegramLongPollingCommandBot {//NOPMD

  /**
   * Logger.
   */
  private static final Logger LOG = Logger.getLogger(MorseBot.class.getName());

  /**
   * State Service.
   */
  @Inject
  private transient StateService stateService;

  /**
   * Commandlets as discovered by CDI.
   */
  @Inject
  @Any
  private transient Instance<Commandlet> commandlets;

  /**
   * All the base commands.
   */
  @Inject
  @Any
  private transient Instance<BaseCommand> commands;

  /**
   * Config.
   */
  @Inject
  private transient MorseBotConfig botConfig;


  /**
   * Instantiates a new Morse bot.
   */
  public MorseBot() {
    super();
  }

  /**
   * Send a message with a reply keyboard to the user.
   *
   * @param user usser
   * @param chat chat
   * @param text to display in the chat
   * @param buttons String list of buttons
   */
  public void sendReplyKeyboardMessage(final User user, final Chat chat, final String text,
      final List<String> buttons) {
    final SendMessage sendMessage = new SendMessage();
    sendMessage.setChatId(chat.getId());
    sendMessage.setText(text + " @" + BaseCommand.getUsername(user));
    sendMessage.setReplyMarkup(sendReplyKeyboardMarkup(buttons, chat.isGroupChat()));
    sendMessage(sendMessage);
  }

  /**
   * Send a message with a reply keyboard to the user.
   *
   * @param user usser
   * @param chat chat
   * @param text to display in the chat
   * @param buttons String array of buttons
   */
  public void sendReplyKeyboardMessage(final User user, final Chat chat, final String text,
      final String... buttons) {
    sendReplyKeyboardMessage(user, chat, text, Arrays.asList(buttons));
  }

  /**
   * Send reply keyboard message.
   *
   * @param message the message
   * @param text the text
   * @param buttons the buttons
   */
  public void sendReplyKeyboardMessage(final Message message, final String text,
      final List<String> buttons) {
    final SendMessage sendMessage = new SendMessage();
    sendMessage.setChatId(message.getChatId());
    sendMessage.setText(text + " @" + BaseCommand.getUsername(message));
    sendMessage.setReplyMarkup(sendReplyKeyboardMarkup(buttons, message.isGroupMessage()));
    sendMessage(sendMessage);
  }

  /**
   * Send reply keyboard message.
   *
   * @param message the message
   * @param text the text
   * @param buttons the buttons
   */
  public void sendReplyKeyboardMessage(final Message message, final String text,
      final String... buttons) {
    sendReplyKeyboardMessage(message, text, Arrays.asList(buttons));
  }

  /**
   * Send reply keyboard markup reply keyboard markup.
   *
   * @param buttons the buttons
   * @param isGroup the is group
   * @return the reply keyboard markup
   */
  public ReplyKeyboardMarkup sendReplyKeyboardMarkup(final List<String> buttons,
      final boolean isGroup) {
    final ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
    markup.setOneTimeKeyboad(true);
    markup.setResizeKeyboard(true);
    markup.setSelective(isGroup);

    final List<KeyboardRow> keyboardRows = new ArrayList<>();
    int count = 0;
    KeyboardRow keyboardRow = new KeyboardRow();
    for (final String button : buttons) {
      keyboardRow.add(new KeyboardButton(button));//NOPMD
      count++;
      if (count % 2 == 0) {
        keyboardRows.add(keyboardRow);
        keyboardRow = new KeyboardRow();//NOPMD
      }
    }
    if (keyboardRow.size() > 0) {
      keyboardRows.add(keyboardRow);
    }

    markup.setKeyboard(keyboardRows);

    return markup;
  }

  /**
   * Send reply message.
   *
   * @param message the message
   * @param text the text
   */
  public void sendReplyMessage(final Message message, final String text) {
    final SendMessage sendMessage = new SendMessage();
    sendMessage.setChatId(message.getChatId());
    sendMessage.setText(text + " @" + BaseCommand.getUsername(message));
    final ForceReplyKeyboard keyboard = new ForceReplyKeyboard();
    keyboard.setSelective(true);
    sendMessage.setReplyMarkup(keyboard);
    sendMessage(sendMessage);
  }

  /**
   * Sets .
   */
  @PostConstruct
  public void setup() {
    for (final BaseCommand baseCommand : commands) {
      register(baseCommand);
    }
    final TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
    try {
      telegramBotsApi.registerBot(this);
    } catch (TelegramApiRequestException e) {
      LOG.log(Level.SEVERE, "Error creating morse bot", e);
    }
  }

  /**
   *
   * @param update the update
   */
  @Override
  public void processNonCommandUpdate(final Update update) {
    LOG.info("Receives update");
    if (update.getMessage().getNewChatMember() != null) {
      sendMessage("Ahhh Yeah, one more human to worship me. I am Bender!",
          update.getMessage().getChatId().toString());
      return;
    }
    if (update.getMessage().getLeftChatMember() != null) {
      sendMessage("Sad. Guess bender was just too much awesome.",
          update.getMessage().getChatId().toString());
      return;
    }
    if (update.getMessage().getContact() != null) {
      final Contact contact = update.getMessage().getContact();
      sendMessage(
          contact.getFirstName() + " - " + contact.getLastName() + " " + contact.getUserID(),
          update.getMessage().getChatId().toString());
      return;
    }
    try {
      final String state = stateService
          .getUserState(update.getMessage().getFrom().getId(), update.getMessage().getChatId());
      handleUpdate(update.getMessage(), state);
      return;
    } catch (MorseBotException e) {
      LOG.info("No state found for user");
    }

    sendMessage("I dont know that command ",
        update.getMessage().getChatId().toString());

  }

  /**
   *
   * @param sendMessage
   * @return
   */
  @Override
  public Message sendMessage(final SendMessage sendMessage) {
    try {
      return super.sendMessage(sendMessage);
    } catch (TelegramApiException e) {
      LOG.log(Level.SEVERE, "Could not send message", e);
      return null;
    }
  }

  /**
   * Send message boolean.
   *
   * @param message the message
   * @param key the key
   * @param html the html
   * @return the boolean
   */
  public boolean sendMessage(final String message, final String key, final boolean html) {
    final SendMessage sendMessage = new SendMessage();
    sendMessage.enableHtml(html);
    sendMessage.setChatId(key);
    sendMessage.setText(message);
    sendMessage.setReplyMarkup(new ReplyKeyboardRemove());
    try {
      super.sendMessage(sendMessage);
      return true;
    } catch (TelegramApiException e) {
      LOG.log(Level.SEVERE, e.getMessage(), e);
      return false;
    }
  }

  /**
   * Send message boolean.
   *
   * @param message the message
   * @param key the key
   * @return the boolean
   */
  public boolean sendMessage(final String message, final String key) {
    return sendMessage(message, key, false);
  }

  private void handleUpdate(final Message message, final String command) {
    if (LOG.isLoggable(Level.INFO)) {
      LOG.info(//NOPMD
          "Searching for commandlet to handle state " + command + " message " + message
              .getText());
    }
    for (final Commandlet commandlet : commandlets) {
      if (commandlet.canHandleCommand(message, command)) {
        if (LOG.isLoggable(Level.INFO)) {
          LOG.info("Executing class " + commandlet.getClass().getName());//NOPMD
        }
        commandlet.handleCommand(message, command,
            stateService.getParameters(message.getFrom().getId(), message.getChatId()),
            this);
        final String newState = commandlet.getNewState(message, command);
        if (newState == null) {
          stateService.deleteState(message.getFrom().getId(), message.getChatId());
        } else {
          stateService.setState(message.getFrom().getId(), message.getChatId(), newState,
              commandlet.getNewStateParams(message, command,
                  stateService.getParameters(message.getFrom().getId(), message.getChatId())));
        }
      }
    }
  }

  /**
   *
   * @return
   */
  public Instance<BaseCommand> getCommands() {
    return commands;
  }

  /**
   *
   * @return
   */
  @Override
  public String getBotUsername() {
    return botConfig.getUsername();
  }

  /**
   *
   * @return
   */
  @Override
  public String getBotToken() {
    return botConfig.getKey();
  }

}



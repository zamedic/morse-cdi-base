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
public class MorseBot extends TelegramLongPollingCommandBot {

  /**
   * Logger
   */
  private static Logger LOG = Logger.getLogger(MorseBot.class.getName());

  /**
   * State Service
   */
  @Inject
  private StateService stateService;

  /**
   * Commandlets as discovered by CDI
   */
  @Inject
  @Any
  private Instance<Commandlet> commandlets;

  /**
   * All the base commands
   */
  @Inject
  @Any
  private Instance<BaseCommand> commands;

  /**
   * Config
   */
  @Inject
  private MorseBotConfig botConfig;


  public MorseBot() {
    super();
  }

  /**
   * Send a message with a reply keyboard to the user
   * @param user usser
   * @param chat chat
   * @param text to display in the chat
   * @param buttons String list of buttons
   */
  public void sendReplyKeyboardMessage(User user, Chat chat, String text,
      List<String> buttons) {
    SendMessage sendMessage = new SendMessage();
    sendMessage.setChatId(chat.getId());
    sendMessage.setText(text + " @" + BaseCommand.getUsername(user));
    sendMessage.setReplyMarkup(sendReplyKeyboardMarkup(buttons, chat.isGroupChat()));
    sendMessage(sendMessage);
  }

  /**
   * Send a message with a reply keyboard to the user
   * @param user usser
   * @param chat chat
   * @param text to display in the chat
   * @param buttons String array of buttons
   */
  public void sendReplyKeyboardMessage(User user, Chat chat, String text,
      String... buttons) {
    sendReplyKeyboardMessage(user, chat, text, Arrays.asList(buttons));
  }

  public void sendReplyKeyboardMessage(Message message, String text,
      List<String> buttons) {
    SendMessage sendMessage = new SendMessage();
    sendMessage.setChatId(message.getChatId());
    sendMessage.setText(text + " @" + BaseCommand.getUsername(message));
    sendMessage.setReplyMarkup(sendReplyKeyboardMarkup(buttons, message.isGroupMessage()));
    sendMessage(sendMessage);
  }

  public void sendReplyKeyboardMessage(Message message, String text,
      String... buttons) {
    sendReplyKeyboardMessage(message, text, Arrays.asList(buttons));
  }

  public ReplyKeyboardMarkup sendReplyKeyboardMarkup(List<String> buttons, boolean isGroup) {
    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
    replyKeyboardMarkup.setOneTimeKeyboad(true);
    replyKeyboardMarkup.setResizeKeyboard(true);
    replyKeyboardMarkup.setSelective(isGroup);

    List<KeyboardRow> keyboardRows = new ArrayList<>();
    int count = 0;
    KeyboardRow keyboardRow = new KeyboardRow();
    for (String button : buttons) {
      keyboardRow.add(new KeyboardButton(button));
      count++;
      if (count % 2 == 0) {
        keyboardRows.add(keyboardRow);
        keyboardRow = new KeyboardRow();
      }
    }
    if (keyboardRow.size() > 0) {
      keyboardRows.add(keyboardRow);
    }

    replyKeyboardMarkup.setKeyboard(keyboardRows);

    return replyKeyboardMarkup;
  }

  public void sendReplyMessage(Message message, String text) {
    SendMessage sendMessage = new SendMessage();
    sendMessage.setChatId(message.getChatId());
    sendMessage.setText(text + " @" + BaseCommand.getUsername(message));
    ForceReplyKeyboard forceReplyKeyboard = new ForceReplyKeyboard();
    forceReplyKeyboard.setSelective(true);
    sendMessage.setReplyMarkup(forceReplyKeyboard);
    sendMessage(sendMessage);
  }

  @PostConstruct
  public void setup() {
    for (BaseCommand baseCommand : commands) {
      register(baseCommand);
    }
    TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
    try {
      telegramBotsApi.registerBot(this);
    } catch (TelegramApiRequestException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void processNonCommandUpdate(Update update) {
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
      Contact contact = update.getMessage().getContact();
      sendMessage(
          contact.getFirstName() + " - " + contact.getLastName() + " " + contact.getUserID(),
          update.getMessage().getChatId().toString());
      return;
    }
    try {
      String state = stateService
          .getUserState(update.getMessage().getFrom().getId(), update.getMessage().getChatId());
      handleUpdate(update.getMessage(), state);
      return;
    } catch (MorseBotException e) {
      LOG.info("No state found for user");
    }

    sendMessage("I dont know that command ",
        update.getMessage().getChatId().toString());

  }

  @Override
  public Message sendMessage(SendMessage sendMessage) {
    try {
      return super.sendMessage(sendMessage);
    } catch (TelegramApiException e) {
      LOG.log(Level.SEVERE, "Could not send message", e);
      return null;
    }
  }

  private void handleUpdate(Message message, String command) {
    LOG.info(
        "Searching for commandlet to handle state " + command.toString() + " message " + message
            .getText());
    for (Commandlet commandlet : commandlets) {
      if (commandlet.canHandleCommand(message, command)) {
        LOG.info("Executing class " + commandlet.getClass().getName());
        commandlet.handleCommand(message, command,
            stateService.getParameters(message.getFrom().getId(), message.getChatId()),
            this);
        String newState = commandlet.getNewState(message, command);
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


  @Override
  public String getBotUsername() {
    return botConfig.getUsername();
  }

  @Override
  public String getBotToken() {
    return botConfig.getKey();
  }

  public boolean sendMessage(String message, String key, boolean html) {
    SendMessage sendMessage = new SendMessage();
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

  public boolean sendMessage(String message, String key) {
    return sendMessage(message, key, false);
  }

}



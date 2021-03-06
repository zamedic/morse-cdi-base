package com.marcarndt.morse.command;

import com.marcarndt.morse.MorseBot;
import com.marcarndt.morse.MorseBotException;
import com.marcarndt.morse.service.StateService;
import com.marcarndt.morse.service.UserService;
import com.marcarndt.morse.telegrambots.api.methods.send.SendMessage;
import com.marcarndt.morse.telegrambots.api.objects.Chat;
import com.marcarndt.morse.telegrambots.api.objects.Message;
import com.marcarndt.morse.telegrambots.api.objects.User;
import com.marcarndt.morse.telegrambots.bots.AbsSender;
import com.marcarndt.morse.telegrambots.bots.commands.BotCommand;
import com.marcarndt.morse.telegrambots.exceptions.TelegramApiException;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;

/**
 * Created by arndt on 2017/04/11.
 */
public abstract class BaseCommand extends BotCommand {

  private static Logger LOG = Logger.getLogger(BaseCommand.class.getName());
  @Inject
  UserService userService;
  @Inject
  StateService stateService;
  @Inject
  MorseBot morseBot;

  public static String getUsername(Message message) {
    return getUsername(message.getFrom());

  }

  /**
   * Gets the username for the User.
   * @param user Chat Object
   * @return String for the user name
   */
  public static String getUsername(User user) {
    if (user.getUserName() != null) {
      return user.getUserName();
    }
    return user.getFirstName();
  }

  /**
   * Handles a general morse bot exception bu sending the error to the user.
   *
   * @param absSender Sender
   * @param chat Chat Object
   * @param exception Exception
   */
  protected void handleException(AbsSender absSender, Chat chat, MorseBotException exception) {
    SendMessage sendMessage = new SendMessage();
    sendMessage.setText(exception.getMessage());
    sendMessage.setChatId(chat.getId());
    try {
      absSender.sendMessage(sendMessage);
    } catch (TelegramApiException e1) {
      LOG.log(Level.SEVERE, e1.getMessage(), exception);
    }
  }

  protected void sendMessage(AbsSender absSender, Chat chat, String message) {
    LOG.info("Sending Message: " + message);
    SendMessage sendMessage = new SendMessage();
    sendMessage.enableHtml(true);
    sendMessage.setText(message);
    sendMessage.setChatId(chat.getId());

    try {
      absSender.sendMessage(sendMessage);
    } catch (TelegramApiException e) {
      LOG.log(Level.SEVERE, e.getMessage(), e);
    }
  }

  @Override
  public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
    try {
      if (!userService.validateUser(user.getId(), getRole())) {
        sendMessage(absSender, chat,
            "User not allowed to perform the " + getCommandIdentifier() + " transaction");
        LOG.warning("User " + user.getId() + " - " + user.getFirstName() + " attempted to perform "
            + getCommandIdentifier() + " but was denied");
        return;
      }
    } catch (MorseBotException e) {
      sendMessage(absSender, chat, e.getMessage());
    }
    String state = performCommand(morseBot, user, chat, arguments);
    stateService.setState(user.getId(), chat.getId(), state);

  }

  public abstract String getRole();

  protected abstract String performCommand(MorseBot morseBot, User user, Chat chat,
      String[] arguments);


}

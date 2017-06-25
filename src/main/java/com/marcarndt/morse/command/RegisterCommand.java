package com.marcarndt.morse.command;

import com.marcarndt.morse.MorseBot;
import com.marcarndt.morse.service.UserService;
import com.marcarndt.morse.telegrambots.api.methods.send.SendMessage;
import com.marcarndt.morse.telegrambots.api.objects.Chat;
import com.marcarndt.morse.telegrambots.api.objects.User;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;

/**
 * Created by arndt on 2017/04/17.
 */
public class RegisterCommand extends BaseCommand {

  /**
   * Logger
   */
  private static final Logger LOG = Logger.getLogger(RegisterCommand.class.getName());

  /**
   * The User service.
   */
  @Inject
  private transient UserService userService;

  /**
   *
   * @return
   */
  @Override
  public String getCommandIdentifier() {
    return "register";
  }

  /**
   *
   * @return
   */
  @Override
  public String getDescription() {
    return "Register yourself with the bot";
  }

  /**
   *
   * @return
   */
  @Override
  public String getRole() {
    return UserService.UNAUTHENTICATED;
  }

  /**
   *
   * @param morseBot
   * @param user
   * @param chat
   * @param arguments
   * @return
   */
  @Override
  protected String performCommand(final MorseBot morseBot, final User user, final Chat chat, final String[] arguments) {
    if(LOG.isLoggable(Level.INFO)) {
      LOG.info("Adding User " + user.getId() + " - " + user.getFirstName());//NOPMD
    }
    userService.addUser(user.getId(), user.getFirstName(), user.getLastName());
    SendMessage sendMessage = new SendMessage();
    sendMessage.setChatId(chat.getId());
    sendMessage.setText("The bot is now aware of you " + user.getFirstName());
    morseBot.sendMessage(sendMessage);

    if (!userService.adminUserExists()) {
      userService.addUser(user.getId(), user.getFirstName(), user.getLastName(), UserService.ADMIN);
      sendMessage = new SendMessage();
      sendMessage.setText("You have been added as an administrator");
      sendMessage.setChatId(chat.getId());
      morseBot.sendMessage(sendMessage);
    }
    return null;

  }


}

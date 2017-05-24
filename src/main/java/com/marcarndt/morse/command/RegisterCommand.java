package com.marcarndt.morse.command;

import com.marcarndt.morse.MorseBot;
import com.marcarndt.morse.service.UserService;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import com.marcarndt.morse.telegrambots.api.methods.send.SendMessage;
import com.marcarndt.morse.telegrambots.api.objects.Chat;
import com.marcarndt.morse.telegrambots.api.objects.User;
import com.marcarndt.morse.telegrambots.bots.AbsSender;
import com.marcarndt.morse.telegrambots.exceptions.TelegramApiException;

/**
 * Created by arndt on 2017/04/17.
 */
public class RegisterCommand extends BaseCommand {

  private static Logger LOG = Logger.getLogger(RegisterCommand.class.getName());

  @Inject
  UserService userService;

  @Override
  public String getCommandIdentifier() {
    return "register";
  }

  @Override
  public String getDescription() {
    return "Register yourself with the bot";
  }

  @Override
  public String getRole() {
    return UserService.UNAUTHENTICATED;
  }

  @Override
  protected String performCommand(MorseBot morseBot, User user, Chat chat, String[] arguments) {
    LOG.info("Adding User " + user.getId() + " - " + user.getFirstName());
    userService.addUser(user.getId(), user.getFirstName(), user.getLastName());
    SendMessage sendMessage = new SendMessage();
    sendMessage.setChatId(chat.getId());
    sendMessage.setText("The bot is now aware of you " + user.getFirstName());
    morseBot.sendMessage(sendMessage);
    return null;

  }


}
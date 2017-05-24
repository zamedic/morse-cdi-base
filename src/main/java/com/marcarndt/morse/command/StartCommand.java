package com.marcarndt.morse.command;


import com.marcarndt.morse.service.UserService;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import com.marcarndt.morse.telegrambots.api.methods.send.SendMessage;
import com.marcarndt.morse.telegrambots.api.objects.Chat;
import com.marcarndt.morse.telegrambots.api.objects.User;
import com.marcarndt.morse.telegrambots.bots.AbsSender;
import com.marcarndt.morse.telegrambots.bots.commands.BotCommand;
import com.marcarndt.morse.telegrambots.exceptions.TelegramApiException;

/**
 * Created by arndt on 2017/04/06.
 */
@Stateless
public class StartCommand extends BotCommand {

  @Inject
  UserService userService;

  private static Logger LOG = Logger.getLogger(StartCommand.class.getName());

  @Override
  public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
    SendMessage sendMessage = new SendMessage();
    sendMessage.setText("For me to send alerts, please use group chat ID: " + chat.getId());
    sendMessage.setChatId(chat.getId());

    try {
      absSender.sendMessage(sendMessage);
    } catch (TelegramApiException e) {
      LOG.log(Level.WARNING, "Error sending response", e);
    }

    if (!userService.adminUserExists()) {
      userService.addUser(user.getId(), user.getFirstName(), user.getLastName(), UserService.ADMIN);
      sendMessage = new SendMessage();
      sendMessage.setText("You have been added as an administrator");
      sendMessage.setChatId(chat.getId());
      try {
        absSender.sendMessage(sendMessage);
      } catch (TelegramApiException e) {
        LOG.log(Level.WARNING, "Error sending response", e);
      }
    }
  }

  @Override
  public String getCommandIdentifier() {
    return "start";
  }

  @Override
  public String getDescription() {
    return "start talking to the bot";
  }

}

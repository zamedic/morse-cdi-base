package com.marcarndt.morse.command;


import com.marcarndt.morse.service.UserService;
import com.marcarndt.morse.telegrambots.api.methods.send.SendMessage;
import com.marcarndt.morse.telegrambots.api.objects.Chat;
import com.marcarndt.morse.telegrambots.api.objects.User;
import com.marcarndt.morse.telegrambots.bots.AbsSender;
import com.marcarndt.morse.telegrambots.bots.commands.BotCommand;
import com.marcarndt.morse.telegrambots.exceptions.TelegramApiException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Created by arndt on 2017/04/06.
 */
@Stateless
public class StartCommand extends BotCommand {

  private static Logger LOG = Logger.getLogger(StartCommand.class.getName());
  @Inject
  UserService userService;

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

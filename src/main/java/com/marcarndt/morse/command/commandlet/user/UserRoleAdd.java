package com.marcarndt.morse.command.commandlet.user;

import com.marcarndt.morse.MorseBot;
import com.marcarndt.morse.MorseBotException;
import com.marcarndt.morse.command.commandlet.Commandlet;
import com.marcarndt.morse.service.UserService;
import com.marcarndt.morse.telegrambots.api.objects.Message;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Created by arndt on 2017/04/18.
 */
@Stateless
public class UserRoleAdd implements Commandlet {

  @Inject
  UserService userService;

  @Override
  public boolean canHandleCommand(Message message, String state) {
    return state.equals(UserRoleListAdd.USER_ADD_ROLE);
  }

  @Override
  public void handleCommand(Message message, String state, List<String> parameters,
      MorseBot morseBot) {

    try {
      userService.addUserToRole(parameters.get(0), message.getText());
      morseBot.sendMessage("USer " + parameters.get(0) + " added to role " + message.getText(),
          message.getChatId().toString());
    } catch (MorseBotException e) {
      morseBot.sendMessage(e.getMessage(), message.getChatId().toString());
    }

  }

  @Override
  public String getNewState(Message message, String command) {
    return null;
  }

  @Override
  public List<String> getNewStateParams(Message message, String state, List<String> params) {
    return null;
  }
}

package com.marcarndt.morse.command.commandlet.user;

import com.marcarndt.morse.MorseBot;
import com.marcarndt.morse.MorseBotException;
import com.marcarndt.morse.command.commandlet.Commandlet;
import com.marcarndt.morse.data.User;
import com.marcarndt.morse.service.UserService;
import com.marcarndt.morse.telegrambots.api.objects.Message;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by arndt on 2017/04/18.
 */
public class UserRoleListAdd implements Commandlet {

  public static final String USER_ADD_ROLE = "User Select Role Add";
  @Inject
  UserService userService;

  @Override
  public boolean canHandleCommand(Message message, String state) {
    return state.equals(UserList.USER_ADD_ROLE);
  }

  @Override
  public void handleCommand(Message message, String state, List<String> parameters,
      MorseBot morseBot) {
    String name = message.getText();

    try {
      User user = userService.getUserByName(name);
      List<String> roles = userService.getUserRoles(user.getUserId());
      List<String> unusedRoles = new ArrayList<>();
      for (String role : userService.getAllRoles()) {
        if (!roles.contains(role)) {
          unusedRoles.add(role.toString());
        }
      }

      morseBot
          .sendReplyKeyboardMessage(message, "Select tole to add", unusedRoles);

    } catch (MorseBotException e) {
      morseBot.sendMessage(e.getMessage(), message.getChatId().toString());
    }
  }

  @Override
  public String getNewState(Message message, String command) {
    return USER_ADD_ROLE;
  }

  @Override
  public List<String> getNewStateParams(Message message, String state, List<String> params) {
    return Arrays.asList(message.getText());
  }
}

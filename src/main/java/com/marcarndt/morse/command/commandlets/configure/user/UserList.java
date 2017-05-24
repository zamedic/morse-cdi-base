package com.marcarndt.morse.command.commandlets.configure.user;

import com.marcarndt.morse.MorseBot;
import com.marcarndt.morse.command.UserAdminCommand;
import com.marcarndt.morse.command.commandlets.Commandlet;
import com.marcarndt.morse.data.User;
import com.marcarndt.morse.service.UserService;
import com.marcarndt.morse.telegrambots.api.objects.Message;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Created by arndt on 2017/04/18.
 */
@Stateless
public class UserList implements Commandlet {

  public static String USER_ADD_ROLE = "User Add Role";
  public static String USER_DELETE_ROLE = "User Delete Role";
  @Inject
  UserService userService;

  @Override
  public boolean canHandleCommand(Message message, String state) {
    return state.equals(UserAdminCommand.USERADMIN) && (
        message.getText().equals(UserAdminCommand.ADD_USER_TO_ROLE) || message.getText()
            .equals(UserAdminCommand.REMOVE_USER_TO_ROLE));
  }

  @Override
  public void handleCommand(Message message, String state, List<String> parameters,
      MorseBot morseBot) {
    List<User> users = userService.getAllUsers();
    List<String> names = users.stream().map(user -> user.getName()).collect(Collectors.toList());
    morseBot
        .sendReplyKeyboardMessage(message, "Select User", names);

  }

  @Override
  public String getNewState(Message message, String command) {
    if (message.getText().equals(UserAdminCommand.ADD_USER_TO_ROLE)) {
      return USER_ADD_ROLE;
    } else {
      return USER_DELETE_ROLE;
    }
  }

  @Override
  public List<String> getNewStateParams(Message message, String state, List<String> params) {
    return null;
  }
}

package com.marcarndt.morse.command;

import com.marcarndt.morse.MorseBot;
import com.marcarndt.morse.service.UserService;
import com.marcarndt.morse.telegrambots.api.objects.Chat;
import com.marcarndt.morse.telegrambots.api.objects.User;

/**
 * Created by arndt on 2017/05/19.
 */
public class UserAdminCommand extends BaseCommand {

  public final static String USERADMIN = "user_admin";
  public final static String ADD_USER_TO_ROLE = "Add user to role";
  public final static String REMOVE_USER_TO_ROLE = "Remove user from role";
  public final static String DELETE_USER = "Delete user";


  @Override
  public String getCommandIdentifier() {
    return "user";
  }

  @Override
  public String getDescription() {
    return "User Administration";
  }

  @Override
  public String getRole() {
    return UserService.ADMIN;
  }

  @Override
  protected String performCommand(MorseBot absSender, User user, Chat chat, String[] arguments) {
    morseBot
        .sendReplyKeyboardMessage(user, chat, ADD_USER_TO_ROLE, REMOVE_USER_TO_ROLE, DELETE_USER);
    return USERADMIN;

  }
}

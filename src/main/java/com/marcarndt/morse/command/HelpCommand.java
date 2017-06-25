package com.marcarndt.morse.command;

import com.marcarndt.morse.MorseBot;
import com.marcarndt.morse.MorseBotException;
import com.marcarndt.morse.service.UserService;
import com.marcarndt.morse.telegrambots.api.objects.Chat;
import com.marcarndt.morse.telegrambots.api.objects.User;

import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Created by arndt on 2017/04/11.
 */
@Stateless
public class HelpCommand extends BaseCommand {

  /**
   * The User service.
   */
  @Inject
  private transient UserService userService;

  /**
   * This is an Authenticated Service.
   *
   * @return Authenticated
   */
  @Override
  public String getRole() {
    return UserService.UNAUTHENTICATED;
  }

  /**
   * Executed the Help Command by returning a list of commands the user has access too.
   *
   * @param morseBot morsebot
   * @param user user
   * @param chat chat
   * @param arguments arguments
   * @return null
   */
  @Override
  public String performCommand(final MorseBot morseBot, final User user, final Chat chat,
      final String[] arguments) {
    final StringBuilder stringBuilder = new StringBuilder();

    for (final BaseCommand command : morseBot.getCommands()) {
      try {
        if (userService.validateUser(user.getId(), command.getRole())) {
          stringBuilder.append(command.getCommandIdentifier()).append(" - ");
          stringBuilder.append(command.getDescription()).append('\n');
        }
      } catch (MorseBotException e) {
        continue;
      }
    }
    sendMessage(morseBot, chat, stringBuilder.toString());
    return null;
  }


  /**
   * Help.
   *
   * @return help
   */
  @Override
  public String getCommandIdentifier() {
    return "help";
  }

  /**
   * get help.
   * @return get help
   */
  @Override
  public String getDescription() {
    return "Get help";
  }
}

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
   *
   * @return
   */
  @Override
  public String getCommandIdentifier() {
    return "help";
  }

  /**
   *
   * @return
   */
  @Override
  public String getDescription() {
    return "Get help";
  }
}

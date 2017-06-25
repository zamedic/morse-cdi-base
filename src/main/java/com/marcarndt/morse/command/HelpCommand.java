package com.marcarndt.morse.command;

import com.marcarndt.morse.MorseBot;
import com.marcarndt.morse.MorseBotException;
import com.marcarndt.morse.service.UserService;
import com.marcarndt.morse.telegrambots.api.objects.Chat;
import com.marcarndt.morse.telegrambots.api.objects.User;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Created by arndt on 2017/04/11.
 */
@Stateless
public class HelpCommand extends BaseCommand {

  private static Logger LOG = Logger.getLogger(HelpCommand.class.getName());

  @Inject
  MorseBot morseBot;

  @Inject
  UserService userService;


  @Override
  public String getRole() {
    return UserService.UNAUTHENTICATED;
  }

  @Override
  public String performCommand(MorseBot morseBot, User user, Chat chat, String[] arguments) {
    StringBuilder stringBuilder = new StringBuilder();

    for (BaseCommand command : morseBot.getCommands()) {
      try {
        if (userService.validateUser(user.getId(), command.getRole())) {
          stringBuilder.append(command.getCommandIdentifier()).append(" - ");
          stringBuilder.append(command.getDescription()).append("\n");
        }
      } catch (MorseBotException e) {
        continue;
      }
    }
    sendMessage(morseBot, chat, stringBuilder.toString());
    return null;
  }


  @Override
  public String getCommandIdentifier() {
    return "help";
  }

  @Override
  public String getDescription() {
    return "Get help";
  }
}

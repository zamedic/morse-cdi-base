package com.marcarndt.morse.command;

import com.marcarndt.morse.MorseBot;
import com.marcarndt.morse.service.UserService;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import com.marcarndt.morse.telegrambots.api.objects.Chat;
import com.marcarndt.morse.telegrambots.api.objects.User;
import com.marcarndt.morse.telegrambots.bots.AbsSender;
import com.marcarndt.morse.telegrambots.bots.commands.BotCommand;

/**
 * Created by arndt on 2017/04/11.
 */
@Stateless
public class HelpCommand extends BaseCommand {
  private static Logger LOG = Logger.getLogger(HelpCommand.class.getName());

  @Inject
  MorseBot morseBot;

  @Override
  public String getRole() {
    return UserService.UNAUTHENTICATED;
  }

  @Override
  public String performCommand(MorseBot morseBot, User user, Chat chat, String[] arguments) {
    StringBuilder stringBuilder = new StringBuilder();

    for (BotCommand command: morseBot.getRegisteredCommands()) {
      stringBuilder.append(command.getCommandIdentifier()).append(" - ");
      stringBuilder.append(command.getDescription()).append("\n");
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

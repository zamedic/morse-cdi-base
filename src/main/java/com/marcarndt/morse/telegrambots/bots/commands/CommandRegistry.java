package com.marcarndt.morse.telegrambots.bots.commands;

import com.marcarndt.morse.telegrambots.api.objects.Message;
import com.marcarndt.morse.telegrambots.bots.AbsSender;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * This class manages all the sshcommands for a bot. You can register and deregister sshcommands on demand
 *
 * @author Timo Schulz (Mit0x2)
 */
public class CommandRegistry implements ICommandRegistry {

  private Map<String, BotCommand> commandRegistryMap = new HashMap<>();
  private boolean allowCommandsWithUsername;
  private String botUsername;
  private BiConsumer<AbsSender, Message> defaultConsumer;

  /**
   * Creates a Command registry
   *
   * @param allowCommandsWithUsername True to allow sshcommands with username, false otherwise
   * @param botUsername Bot username
   */
  public CommandRegistry(boolean allowCommandsWithUsername, String botUsername) {
    this.allowCommandsWithUsername = allowCommandsWithUsername;
    this.botUsername = botUsername;
  }

  @Override
  public void registerDefaultAction(BiConsumer<AbsSender, Message> defaultConsumer) {
    this.defaultConsumer = defaultConsumer;
  }

  @Override
  public boolean register(BotCommand botCommand) {
    if (commandRegistryMap.containsKey(botCommand.getCommandIdentifier())) {
      return false;
    }
    commandRegistryMap.put(botCommand.getCommandIdentifier(), botCommand);
    return true;
  }

  @Override
  public Map<BotCommand, Boolean> registerAll(BotCommand... botCommands) {
    Map<BotCommand, Boolean> resultMap = new HashMap<>(botCommands.length);
    for (BotCommand botCommand : botCommands) {
      resultMap.put(botCommand, register(botCommand));
    }
    return resultMap;
  }

  @Override
  public boolean deregister(BotCommand botCommand) {
    if (commandRegistryMap.containsKey(botCommand.getCommandIdentifier())) {
      commandRegistryMap.remove(botCommand.getCommandIdentifier());
      return true;
    }
    return false;
  }

  @Override
  public Map<BotCommand, Boolean> deregisterAll(BotCommand... botCommands) {
    Map<BotCommand, Boolean> resultMap = new HashMap<>(botCommands.length);
    for (BotCommand botCommand : botCommands) {
      resultMap.put(botCommand, deregister(botCommand));
    }
    return resultMap;
  }

  @Override
  public Collection<BotCommand> getRegisteredCommands() {
    return commandRegistryMap.values();
  }

  @Override
  public BotCommand getRegisteredCommand(String commandIdentifier) {
    return commandRegistryMap.get(commandIdentifier);
  }

  /**
   * Executes a command action if the command is registered.
   * If the command is not registered and there is a default consumer, that action will be
   * performed
   *
   * @param absSender absSender
   * @param message input message
   * @return True if a command or default action is executed, false otherwise
   */
  public boolean executeCommand(AbsSender absSender, Message message) {
    if (message.hasText()) {
      String text = message.getText();
      if (text.startsWith(BotCommand.COMMAND_INIT_CHARACTER)) {
        String commandMessage = text.substring(1);
        String[] commandSplit = commandMessage.split(BotCommand.COMMAND_PARAMETER_SEPARATOR);

        String command = removeUsernameFromCommandIfNeeded(commandSplit[0]);

        if (commandRegistryMap.containsKey(command)) {
          String[] parameters = Arrays.copyOfRange(commandSplit, 1, commandSplit.length);
          commandRegistryMap.get(command)
              .execute(absSender, message.getFrom(), message.getChat(), parameters);
          return true;
        } else if (defaultConsumer != null) {
          defaultConsumer.accept(absSender, message);
          return true;
        }
      }
    }
    return false;
  }

  /**
   * if {@link #allowCommandsWithUsername} is enabled, the username of the bot is removed from
   * the command
   *
   * @param command Command to simplify
   * @return Simplified command
   */
  private String removeUsernameFromCommandIfNeeded(String command) {
    if (allowCommandsWithUsername) {
      return command.replace("@" + botUsername, "").trim();
    }
    return command;
  }
}
package com.marcarndt.morse.telegrambots.bots.commands;

import com.marcarndt.morse.telegrambots.api.objects.Message;
import com.marcarndt.morse.telegrambots.bots.AbsSender;
import java.util.Collection;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * This Interface represents the gateway for registering and deregistering sshcommands.
 *
 * @author Timo Schulz (Mit0x2)
 */
public interface ICommandRegistry {

  /**
   * Register a default action when there is no command register that match the message sent
   *
   * @param defaultConsumer Consumer to evaluate the message
   *
   * Use this method if you want your bot to execute a default action when the user sends a command
   * that is not registered.
   */
  void registerDefaultAction(BiConsumer<AbsSender, Message> defaultConsumer);

  /**
   * register a command
   *
   * @param botCommand the command to register
   * @return whether the command could be registered, was not already registered
   */
  boolean register(BotCommand botCommand);

  /**
   * register multiple sshcommands
   *
   * @param botCommands sshcommands to register
   * @return map with results of the command register per command
   */
  Map<BotCommand, Boolean> registerAll(BotCommand... botCommands);

  /**
   * deregister a command
   *
   * @param botCommand the command to deregister
   * @return whether the command could be deregistered, was registered
   */
  boolean deregister(BotCommand botCommand);

  /**
   * deregister multiple sshcommands
   *
   * @param botCommands sshcommands to deregister
   * @return map with results of the command deregistered per command
   */
  Map<BotCommand, Boolean> deregisterAll(BotCommand... botCommands);

  /**
   * get a collection of all registered sshcommands
   *
   * @return a collection of registered sshcommands
   */
  Collection<BotCommand> getRegisteredCommands();

  /**
   * @param commandIdentifier the slash command the user will type
   * @return botcommand object
   */
  BotCommand getRegisteredCommand(String commandIdentifier);
}
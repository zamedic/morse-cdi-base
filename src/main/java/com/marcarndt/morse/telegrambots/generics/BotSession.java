package com.marcarndt.morse.telegrambots.generics;

import com.marcarndt.morse.telegrambots.bots.DefaultBotOptions;

/**
 * @author Ruben Bermudez
 * @version 1.0
 */
public interface BotSession {

  void setOptions(DefaultBotOptions options);

  void setToken(String token);

  void setCallback(LongPollingBot callback);

  /**
   * Starts the bot
   */
  void start();

  /**
   * Stops the bot
   */
  void stop();

  /**
   * Check if the bot is running
   *
   * @return True if the bot is running, false otherwise
   */
  boolean isRunning();

  /**
   * @deprecated Use @link{{@link #stop()}} instead
   */
  default void close() {
    this.stop();
  }
}

package com.marcarndt.morse.telegrambots.generics;

import com.marcarndt.morse.telegrambots.api.objects.Update;
import com.marcarndt.morse.telegrambots.bots.DefaultBotOptions;
import com.marcarndt.morse.telegrambots.exceptions.TelegramApiRequestException;

/**
 * Callback to handle updates.
 *
 * @author Ruben Bermudez
 * @version 1.0
 */
public interface LongPollingBot {

  /**
   * This method is called when receiving updates via GetUpdates method
   *
   * @param update Update received
   */
  void onUpdateReceived(Update update);

  /**
   * @return bot user name
   */
  String getBotUsername();

  /**
   * @return bot token
   */
  String getBotToken();

  /**
   * Gets options for current bot
   *
   * @return BotOptions object with options information
   */
  DefaultBotOptions getOptions();

  /**
   * @throws TelegramApiRequestException on exception
   */
  void clearWebhook() throws TelegramApiRequestException;

  /**
   * Called when the BotSession is being closed
   */
  default void onClosing() {
  }
}

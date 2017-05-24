package com.marcarndt.morse.telegrambots.generics;

import com.marcarndt.morse.telegrambots.api.methods.BotApiMethod;
import com.marcarndt.morse.telegrambots.api.objects.Update;
import com.marcarndt.morse.telegrambots.exceptions.TelegramApiRequestException;

/**
 * Callback to handle updates.
 *
 * @author Ruben Bermudez
 * @version 1.0
 */
public interface WebhookBot {

  /**
   * @param update Update
   * @return Bot Method
   */
  BotApiMethod onWebhookUpdateReceived(Update update);

  /**
   * Gets bot username of this bot
   *
   * @return Bot username
   */
  String getBotUsername();

  /**
   * Gets bot token to access Telegram API
   *
   * @return Bot token
   */
  String getBotToken();

  /**
   * ExecuteSSH setWebhook method to set up the url of the webhook
   *
   * @param url Url for the webhook
   * @param publicCertificatePath Path to the public key certificate of the webhook
   * @throws TelegramApiRequestException In case of error executing the request
   */
  void setWebhook(String url, String publicCertificatePath) throws TelegramApiRequestException;

  /**
   * Gets in the url for the webhook
   *
   * @return path in the url
   */
  String getBotPath();
}

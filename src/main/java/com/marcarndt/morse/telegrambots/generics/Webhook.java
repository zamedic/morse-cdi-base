package com.marcarndt.morse.telegrambots.generics;

import com.marcarndt.morse.telegrambots.exceptions.TelegramApiRequestException;

/**
 * @author Ruben Bermudez
 * @version 1.0
 */
public interface Webhook {

  void startServer() throws TelegramApiRequestException;

  void registerWebhook(WebhookBot callback);

  void setInternalUrl(String internalUrl);

  void setKeyStore(String keyStore, String keyStorePassword) throws TelegramApiRequestException;
}

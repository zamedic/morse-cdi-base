package com.marcarndt.morse.telegrambots;

import com.marcarndt.morse.telegrambots.exceptions.TelegramApiRequestException;
import com.marcarndt.morse.telegrambots.generics.BotSession;
import com.marcarndt.morse.telegrambots.generics.LongPollingBot;
import com.marcarndt.morse.telegrambots.generics.Webhook;
import com.marcarndt.morse.telegrambots.generics.WebhookBot;
import com.marcarndt.morse.telegrambots.updatesreceivers.DefaultBotSession;
import com.marcarndt.morse.telegrambots.updatesreceivers.DefaultWebhook;
import java.text.MessageFormat;

/**
 * @author Ruben Bermudez
 * @version 1.0
 *
 *          Bots manager
 */
public class TelegramBotsApi {

  private static final String webhookUrlFormat = "{0}callback/";
  private boolean useWebhook; ///< True to enable webhook usage
  private Webhook webhook; ///< Webhook instance
  private String extrenalUrl; ///< External url of the bots
  private String pathToCertificate; ///< Path to public key certificate

  /**
   *
   */
  public TelegramBotsApi() {
  }

  /**
   * Creates an HTTP server to receive webhook request
   *
   * This option requires externally handled HTTPS support (i.e. via proxy)
   *
   * @param externalUrl External base url for the webhook
   * @param internalUrl Internal base url for the webhook
   * @throws TelegramApiRequestException on exception
   */
  public TelegramBotsApi(String externalUrl, String internalUrl)
      throws TelegramApiRequestException {
    if (externalUrl == null || externalUrl.isEmpty()) {
      throw new TelegramApiRequestException("Parameter externalUrl can not be null or empty");
    }
    if (internalUrl == null || internalUrl.isEmpty()) {
      throw new TelegramApiRequestException("Parameter internalUrl can not be null or empty");
    }

    this.useWebhook = true;
    this.extrenalUrl = fixExternalUrl(externalUrl);
    webhook = new DefaultWebhook();
    webhook.setInternalUrl(internalUrl);
    webhook.startServer();
  }

  /**
   * Creates an HTTPS server to receive webhook request
   *
   * @param keyStore KeyStore for the server
   * @param keyStorePassword Key store password for the server
   * @param externalUrl External base url for the webhook
   * @param internalUrl Internal base url for the webhook
   * @throws TelegramApiRequestException on exception
   */
  public TelegramBotsApi(String keyStore, String keyStorePassword, String externalUrl,
      String internalUrl) throws TelegramApiRequestException {
    if (externalUrl == null || externalUrl.isEmpty()) {
      throw new TelegramApiRequestException("Parameter externalUrl can not be null or empty");
    }
    if (internalUrl == null || internalUrl.isEmpty()) {
      throw new TelegramApiRequestException("Parameter internalUrl can not be null or empty");
    }
    if (keyStore == null || keyStore.isEmpty()) {
      throw new TelegramApiRequestException("Parameter keyStore can not be null or empty");
    }
    if (keyStorePassword == null || keyStorePassword.isEmpty()) {
      throw new TelegramApiRequestException("Parameter keyStorePassword can not be null or empty");
    }

    this.useWebhook = true;
    this.extrenalUrl = fixExternalUrl(externalUrl);
    webhook = new DefaultWebhook();
    webhook.setInternalUrl(internalUrl);
    webhook.setKeyStore(keyStore, keyStorePassword);
    webhook.startServer();
  }

  /**
   * Creates an HTTPS server with self-signed certificate to receive webhook request
   *
   * @param keyStore KeyStore for the server
   * @param keyStorePassword Key store password for the server
   * @param externalUrl External base url for the webhook
   * @param internalUrl Internal base url for the webhook
   * @param pathToCertificate Full path until .pem public certificate keys
   * @throws TelegramApiRequestException on exception
   */
  public TelegramBotsApi(String keyStore, String keyStorePassword, String externalUrl,
      String internalUrl, String pathToCertificate) throws TelegramApiRequestException {
    if (externalUrl == null || externalUrl.isEmpty()) {
      throw new TelegramApiRequestException("Parameter externalUrl can not be null or empty");
    }
    if (internalUrl == null || internalUrl.isEmpty()) {
      throw new TelegramApiRequestException("Parameter internalUrl can not be null or empty");
    }
    if (keyStore == null || keyStore.isEmpty()) {
      throw new TelegramApiRequestException("Parameter keyStore can not be null or empty");
    }
    if (keyStorePassword == null || keyStorePassword.isEmpty()) {
      throw new TelegramApiRequestException("Parameter keyStorePassword can not be null or empty");
    }
    if (pathToCertificate == null || pathToCertificate.isEmpty()) {
      throw new TelegramApiRequestException("Parameter pathToCertificate can not be null or empty");
    }

    this.useWebhook = true;
    this.extrenalUrl = fixExternalUrl(externalUrl);
    this.pathToCertificate = pathToCertificate;
    webhook = new DefaultWebhook();
    webhook.setInternalUrl(internalUrl);
    webhook.setKeyStore(keyStore, keyStorePassword);
    webhook.startServer();
  }

  private static final String fixExternalUrl(String externalUrl) {
    if (externalUrl != null && !externalUrl.endsWith("/")) {
      externalUrl = externalUrl + "/";
    }
    return MessageFormat.format(webhookUrlFormat, externalUrl);
  }

  /**
   * Register a bot. The Bot Session is started immediately, and may be disconnected by calling
   * close.
   *
   * @param bot the bot to register
   * @return bot session
   * @throws TelegramApiRequestException on exception
   */
  public BotSession registerBot(LongPollingBot bot) throws TelegramApiRequestException {
    bot.clearWebhook();
    ApiContextInitializer.init();
    BotSession session = new DefaultBotSession();
    session.setToken(bot.getBotToken());
    session.setOptions(bot.getOptions());
    session.setCallback(bot);
    session.start();
    return session;
  }

  /**
   * Register a bot in the api that will receive updates using webhook method
   *
   * @param bot Bot to register
   * @throws TelegramApiRequestException on exception
   */
  public void registerBot(WebhookBot bot) throws TelegramApiRequestException {
    if (useWebhook) {
      webhook.registerWebhook(bot);
      bot.setWebhook(extrenalUrl + bot.getBotPath(), pathToCertificate);
    }
  }
}

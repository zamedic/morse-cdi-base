package com.marcarndt.morse.telegrambots.updatesreceivers;

import com.marcarndt.morse.telegrambots.exceptions.TelegramApiRequestException;
import com.marcarndt.morse.telegrambots.generics.Webhook;
import com.marcarndt.morse.telegrambots.generics.WebhookBot;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.ssl.SSLContextConfigurator;
import org.glassfish.grizzly.ssl.SSLEngineConfigurator;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * @author Ruben Bermudez
 * @version 1.0 Webhook to receive updates
 */
public class DefaultWebhook implements Webhook {

  private String keystoreServerFile;
  private String keystoreServerPwd;
  private String internalUrl;

  private RestApi restApi;

  public DefaultWebhook() throws TelegramApiRequestException {
    this.restApi = new RestApi();
  }

  private static void validateServerKeystoreFile(String keyStore)
      throws TelegramApiRequestException {
    File file = new File(keyStore);
    if (!file.exists() || !file.canRead()) {
      throw new TelegramApiRequestException("Can't find or access server keystore file.");
    }
  }

  public void setInternalUrl(String internalUrl) {
    this.internalUrl = internalUrl;
  }

  public void setKeyStore(String keyStore, String keyStorePassword)
      throws TelegramApiRequestException {
    this.keystoreServerFile = keyStore;
    this.keystoreServerPwd = keyStorePassword;
    validateServerKeystoreFile(keyStore);
  }

  public void registerWebhook(WebhookBot callback) {
    restApi.registerCallback(callback);
  }

  public void startServer() throws TelegramApiRequestException {
    ResourceConfig rc = new ResourceConfig();
    rc.register(restApi);
    rc.register(JacksonFeature.class);

    HttpServer grizzlyServer;
    if (keystoreServerFile != null && keystoreServerPwd != null) {
      SSLContextConfigurator sslContext = new SSLContextConfigurator();

      // set up security context
      sslContext.setKeyStoreFile(keystoreServerFile); // contains server keypair
      sslContext.setKeyStorePass(keystoreServerPwd);

      grizzlyServer = GrizzlyHttpServerFactory.createHttpServer(getBaseURI(), rc, true,
          new SSLEngineConfigurator(sslContext).setClientMode(false).setNeedClientAuth(false));
    } else {
      grizzlyServer = GrizzlyHttpServerFactory.createHttpServer(getBaseURI(), rc);
    }

    try {
      grizzlyServer.start();
    } catch (IOException e) {
      throw new TelegramApiRequestException("Error starting webhook server", e);
    }
  }

  private URI getBaseURI() {
    return URI.create(internalUrl);
  }
}

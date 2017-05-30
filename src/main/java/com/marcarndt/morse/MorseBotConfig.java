package com.marcarndt.morse;

/**
 * Class provides an interface for tall the configureation elements required by the Morse CDI
 * Framework
 */
public interface MorseBotConfig {

  /**
   * @return Telegram Bot User Name
   */
  String getUsername();

  /**
   * @return Telegram Bot Key
   */
  String getKey();

  /**
   * @return Address of the Mongo Server
   */
  String getMongoAddress();

  /**
   * @return Mongo Database Name
   */
  String getMongoDatabase();

  /**
   * @return Proxy for bot connections. Null if no proxy is required
   */
  String getProxyUrl();

  /**
   * @return port for the bot proxy connection. Null is no proxy is required
   */
  int getProxyPort();
}

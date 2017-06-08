package com.marcarndt.morse;

/**
 * Class provides an interface for tall the configureation elements required by the Morse CDI
 * Framework
 */
public interface MorseBotConfig {

  /**
   * Gets username.
   *
   * @return Telegram Bot User Name
   */
  String getUsername();

  /**
   * Gets key.
   *
   * @return Telegram Bot Key
   */
  String getKey();

  /**
   * Gets mongo address.
   *
   * @return Address of the Mongo Server
   */
  String getMongoAddress();

  /**
   * Gets mongo database.
   *
   * @return Mongo Database Name
   */
  String getMongoDatabase();

  /**
   * Gets proxy url.
   *
   * @return Proxy for bot connections. Null if no proxy is required
   */
  String getProxyUrl();

  /**
   * Gets proxy port.
   *
   * @return port for the bot proxy connection. Null is no proxy is required
   */
  int getProxyPort();


  /**
   * Gets mongo user.
   *
   * @return the mongo user
   */
  String getMongoUser();

  String getMongoPassword();
}

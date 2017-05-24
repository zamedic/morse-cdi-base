package com.marcarndt.morse;

/**
 * Created by arndt on 2017/05/19.
 */
public interface MorseBotConfig {

  String getUsername();

  String getKey();

  String getMongoAddress();

  String getMongoDatabase();

  String getProxyUrl();

  int getProxyPort();
}

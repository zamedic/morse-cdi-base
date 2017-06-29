package com.marcarndt.morse;

/**
 * Created by arndt on 2017/05/19.
 */
public class MorseBotException extends Exception {

  public MorseBotException(String s) {
    super(s);
  }

  public MorseBotException(String message, Throwable cause) {
    super(message, cause);
  }
}

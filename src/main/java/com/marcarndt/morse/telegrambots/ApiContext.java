package com.marcarndt.morse.telegrambots;


import com.marcarndt.morse.telegrambots.logging.BotLogger;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ruben Bermudez
 * @version 1.0
 */
public class ApiContext {

  private static Object lock = new Object();
  private static Map<Class, Class> bindings = new HashMap<>();
  private static Map<Class, Class> singletonBindings = new HashMap<>();


  public static <T, S extends T> void register(Class<T> type, Class<S> implementation) {
    if (bindings.containsKey(type)) {
      BotLogger.debug("ApiContext",
          MessageFormat.format("Class {0} already registered", type.getName()));
    }
    bindings.put(type, implementation);
  }

  public static <T, S extends T> void registerSingleton(Class<T> type, Class<S> implementation) {
    if (singletonBindings.containsKey(type)) {
      BotLogger.debug("ApiContext",
          MessageFormat.format("Class {0} already registered", type.getName()));
    }
    singletonBindings.put(type, implementation);
  }



}

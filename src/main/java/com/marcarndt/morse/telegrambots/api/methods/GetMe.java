package com.marcarndt.morse.telegrambots.api.methods;

import com.fasterxml.jackson.core.type.TypeReference;
import com.marcarndt.morse.telegrambots.api.objects.User;
import com.marcarndt.morse.telegrambots.api.objects.replykeyboard.ApiResponse;
import com.marcarndt.morse.telegrambots.exceptions.TelegramApiRequestException;
import com.marcarndt.morse.telegrambots.exceptions.TelegramApiValidationException;
import java.io.IOException;

/**
 * A simple method for testing your bot's auth token. Requires no parameters. Returns basic
 * information about the bot in form of a UserInitialize object
 *
 * @author Ruben Bermudez
 * @version 1.0
 */
public class GetMe extends BotApiMethod<User> {

  public static final String PATH = "getme";

  public GetMe() {
    super();
  }

  @Override
  public String getMethod() {
    return PATH;
  }

  @Override
  public User deserializeResponse(String answer) throws TelegramApiRequestException {
    try {
      ApiResponse<User> result = OBJECT_MAPPER.readValue(answer,
          new TypeReference<ApiResponse<User>>() {
          });
      if (result.getOk()) {
        return result.getResult();
      } else {
        throw new TelegramApiRequestException("Error getting me", result);
      }
    } catch (IOException e2) {
      throw new TelegramApiRequestException("Unable to deserialize response", e2);
    }
  }

  @Override
  public void validate() throws TelegramApiValidationException {
  }
}

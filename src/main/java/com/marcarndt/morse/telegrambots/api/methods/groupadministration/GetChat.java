package com.marcarndt.morse.telegrambots.api.methods.groupadministration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.marcarndt.morse.telegrambots.api.methods.BotApiMethod;
import com.marcarndt.morse.telegrambots.api.objects.Chat;
import com.marcarndt.morse.telegrambots.api.objects.replykeyboard.ApiResponse;
import com.marcarndt.morse.telegrambots.exceptions.TelegramApiRequestException;
import com.marcarndt.morse.telegrambots.exceptions.TelegramApiValidationException;
import java.io.IOException;
import java.util.Objects;

/**
 * @author Ruben Bermudez
 * @version 1.0
 */
public class GetChat extends BotApiMethod<Chat> {

  public static final String PATH = "getChat";

  private static final String CHATID_FIELD = "chat_id";

  @JsonProperty(CHATID_FIELD)
  private String chatId; ///< Unique identifier for the chat to send the message to (Or username for channels)

  public GetChat() {
    super();
  }

  public String getChatId() {
    return chatId;
  }

  public GetChat setChatId(String chatId) {
    this.chatId = chatId;
    return this;
  }

  public GetChat setChatId(Long chatId) {
    Objects.requireNonNull(chatId);
    this.chatId = chatId.toString();
    return this;
  }

  @Override
  public String getMethod() {
    return PATH;
  }

  @Override
  public Chat deserializeResponse(String answer) throws TelegramApiRequestException {
    try {
      ApiResponse<Chat> result = OBJECT_MAPPER.readValue(answer,
          new TypeReference<ApiResponse<Chat>>() {
          });
      if (result.getOk()) {
        return result.getResult();
      } else {
        throw new TelegramApiRequestException("Error getting chat", result);
      }
    } catch (IOException e) {
      throw new TelegramApiRequestException("Unable to deserialize response", e);
    }
  }

  @Override
  public void validate() throws TelegramApiValidationException {
    if (chatId == null) {
      throw new TelegramApiValidationException("ChatId can't be null", this);
    }
  }

  @Override
  public String toString() {
    return "GetChat{" +
        "chatId='" + chatId + '\'' +
        '}';
  }
}

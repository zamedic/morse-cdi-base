package com.marcarndt.morse.telegrambots.api.methods.groupadministration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.marcarndt.morse.telegrambots.api.methods.BotApiMethod;
import com.marcarndt.morse.telegrambots.api.objects.ChatMember;
import com.marcarndt.morse.telegrambots.api.objects.replykeyboard.ApiResponse;
import com.marcarndt.morse.telegrambots.exceptions.TelegramApiRequestException;
import com.marcarndt.morse.telegrambots.exceptions.TelegramApiValidationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @author Ruben Bermudez
 * @version 1.0
 */
public class GetChatAdministrators extends BotApiMethod<ArrayList<ChatMember>> {

  public static final String PATH = "getChatAdministrators";

  private static final String CHATID_FIELD = "chat_id";

  @JsonProperty(CHATID_FIELD)
  private String chatId; ///< Unique identifier for the chat to send the message to (Or username for channels)

  public GetChatAdministrators() {
    super();
  }

  public String getChatId() {
    return chatId;
  }

  public GetChatAdministrators setChatId(Long chatId) {
    Objects.requireNonNull(chatId);
    this.chatId = chatId.toString();
    return this;
  }

  public GetChatAdministrators setChatId(String chatId) {
    this.chatId = chatId;
    return this;
  }

  @Override
  public String getMethod() {
    return PATH;
  }

  @Override
  public ArrayList<ChatMember> deserializeResponse(String answer)
      throws TelegramApiRequestException {
    try {
      ApiResponse<ArrayList<ChatMember>> result = OBJECT_MAPPER.readValue(answer,
          new TypeReference<ApiResponse<ArrayList<ChatMember>>>() {
          });
      if (result.getOk()) {
        return result.getResult();
      } else {
        throw new TelegramApiRequestException("Error getting chat administrators", result);
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
    return "GetChatAdministrators{" +
        "chatId='" + chatId + '\'' +
        '}';
  }
}

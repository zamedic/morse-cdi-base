package com.marcarndt.morse.telegrambots.api.methods.updatingmessages;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.marcarndt.morse.telegrambots.api.methods.BotApiMethod;
import com.marcarndt.morse.telegrambots.api.objects.Message;
import com.marcarndt.morse.telegrambots.api.objects.replykeyboard.ApiResponse;
import com.marcarndt.morse.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import com.marcarndt.morse.telegrambots.exceptions.TelegramApiRequestException;
import com.marcarndt.morse.telegrambots.exceptions.TelegramApiValidationException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;

/**
 * Use this method to edit only the reply markup of messages sent by the bot or via the bot
 * (for inline bots). On success, if edited message is sent by the bot, the edited Message is
 * returned, therwise True is returned.
 *
 * @author Ruben Bermudez
 * @version 1.0
 */
public class EditMessageReplyMarkup extends BotApiMethod<Serializable> {

  public static final String PATH = "editmessagereplymarkup";

  private static final String CHATID_FIELD = "chat_id";
  private static final String MESSAGEID_FIELD = "message_id";
  private static final String INLINE_MESSAGE_ID_FIELD = "inline_message_id";
  private static final String REPLYMARKUP_FIELD = "reply_markup";

  /**
   * Required if inline_message_id is not specified. Unique identifier for the chat to send the
   * message to (Or username for channels)
   */
  @JsonProperty(CHATID_FIELD)
  private String chatId;
  /**
   * Required if inline_message_id is not specified. Unique identifier of the sent message
   */
  @JsonProperty(MESSAGEID_FIELD)
  private Integer messageId;
  /**
   * Required if chat_id and message_id are not specified. Identifier of the inline message
   */
  @JsonProperty(INLINE_MESSAGE_ID_FIELD)
  private String inlineMessageId;
  @JsonProperty(REPLYMARKUP_FIELD)
  private InlineKeyboardMarkup replyMarkup; ///< Optional. A JSON-serialized object for an inline keyboard.

  public EditMessageReplyMarkup() {
    super();
  }

  public String getChatId() {
    return chatId;
  }

  public EditMessageReplyMarkup setChatId(Long chatId) {
    Objects.requireNonNull(chatId);
    this.chatId = chatId.toString();
    return this;
  }

  public EditMessageReplyMarkup setChatId(String chatId) {
    this.chatId = chatId;
    return this;
  }

  public Integer getMessageId() {
    return messageId;
  }

  public EditMessageReplyMarkup setMessageId(Integer messageId) {
    this.messageId = messageId;
    return this;
  }

  public String getInlineMessageId() {
    return inlineMessageId;
  }

  public EditMessageReplyMarkup setInlineMessageId(String inlineMessageId) {
    this.inlineMessageId = inlineMessageId;
    return this;
  }

  public InlineKeyboardMarkup getReplyMarkup() {
    return replyMarkup;
  }

  public EditMessageReplyMarkup setReplyMarkup(InlineKeyboardMarkup replyMarkup) {
    this.replyMarkup = replyMarkup;
    return this;
  }

  @Override
  public String getMethod() {
    return PATH;
  }

  @Override
  public Serializable deserializeResponse(String answer) throws TelegramApiRequestException {
    try {
      ApiResponse<Message> result = OBJECT_MAPPER.readValue(answer,
          new TypeReference<ApiResponse<Message>>() {
          });
      if (result.getOk()) {
        return result.getResult();
      } else {
        throw new TelegramApiRequestException("Error editing message reply markup", result);
      }
    } catch (IOException e) {
      try {
        ApiResponse<Boolean> result = OBJECT_MAPPER.readValue(answer,
            new TypeReference<ApiResponse<Boolean>>() {
            });
        if (result.getOk()) {
          return result.getResult();
        } else {
          throw new TelegramApiRequestException("Error editing message reply markup", result);
        }
      } catch (IOException e2) {
        throw new TelegramApiRequestException("Unable to deserialize response", e);
      }
    }
  }

  @Override
  public void validate() throws TelegramApiValidationException {
    if (inlineMessageId == null) {
      if (chatId == null) {
        throw new TelegramApiValidationException(
            "ChatId parameter can't be empty if inlineMessageId is not present", this);
      }
      if (messageId == null) {
        throw new TelegramApiValidationException(
            "MessageId parameter can't be empty if inlineMessageId is not present", this);
      }
    } else {
      if (chatId != null) {
        throw new TelegramApiValidationException(
            "ChatId parameter must be empty if inlineMessageId is provided", this);
      }
      if (messageId != null) {
        throw new TelegramApiValidationException(
            "MessageId parameter must be empty if inlineMessageId is provided", this);
      }
    }
    if (replyMarkup != null) {
      replyMarkup.validate();
    }
  }

  @Override
  public String toString() {
    return "EditMessageReplyMarkup{" +
        "chatId=" + chatId +
        ", messageId=" + messageId +
        ", inlineMessageId=" + inlineMessageId +
        ", replyMarkup=" + replyMarkup +
        '}';
  }
}

package com.marcarndt.morse.telegrambots.api.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.marcarndt.morse.telegrambots.api.interfaces.BotApiObject;

/**
 * @author Ruben Bermudez
 * @version 1.0
 *
 *          This object contains information about one member of the chat.
 */
public class ChatMember implements BotApiObject {

  private static final String USER_FIELD = "user";
  private static final String STATUS_FIELD = "status";

  @JsonProperty(USER_FIELD)
  private User user; ///< Information about the user
  @JsonProperty(STATUS_FIELD)
  private String status; ///< The member's status in the chat. Can be "creator", "administrator", "member", "left" or "kicked"

  public ChatMember() {
    super();
  }

  public User getUser() {
    return user;
  }

  public String getStatus() {
    return status;
  }

  @Override
  public String toString() {
    return "ChatMember{" +
        "user=" + user +
        ", status='" + status + '\'' +
        '}';
  }
}

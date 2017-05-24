package com.marcarndt.morse.telegrambots.api.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.marcarndt.morse.telegrambots.api.interfaces.BotApiObject;

/**
 * @author Ruben Bermudez
 * @version 1.0
 *
 *          This object represents a Telegram user or bot.
 */
public class User implements BotApiObject {

  private static final String ID_FIELD = "id";
  private static final String FIRSTNAME_FIELD = "first_name";
  private static final String LASTNAME_FIELD = "last_name";
  private static final String USERNAME_FIELD = "username";

  @JsonProperty(ID_FIELD)
  private Integer id; ///< Unique identifier for this user or bot
  @JsonProperty(FIRSTNAME_FIELD)
  private String firstName; ///< UserInitialize‘s or bot’s first name
  @JsonProperty(LASTNAME_FIELD)
  private String lastName; ///< Optional. UserInitialize‘s or bot’s last name
  @JsonProperty(USERNAME_FIELD)
  private String userName; ///< Optional. UserInitialize‘s or bot’s username

  public User() {
    super();
  }

  public Integer getId() {
    return id;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getUserName() {
    return userName;
  }

  @Override
  public String toString() {
    return "UserInitialize{" +
        "id=" + id +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", userName='" + userName + '\'' +
        '}';
  }
}

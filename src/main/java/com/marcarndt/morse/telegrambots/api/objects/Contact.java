package com.marcarndt.morse.telegrambots.api.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.marcarndt.morse.telegrambots.api.interfaces.BotApiObject;

/**
 * @author Ruben Bermudez
 * @version 1.0
 *
 *          This object represents a phone contact.
 */
public class Contact implements BotApiObject {

  private static final String PHONENUMBER_FIELD = "phone_number";
  private static final String FIRSTNAME_FIELD = "first_name";
  private static final String LASTNAME_FIELD = "last_name";
  private static final String USERID_FIELD = "user_id";

  @JsonProperty(PHONENUMBER_FIELD)
  private String phoneNumber; ///< Contact's phone number
  @JsonProperty(FIRSTNAME_FIELD)
  private String firstName; ///< Contact's first name
  @JsonProperty(LASTNAME_FIELD)
  private String lastName; ///< Optional. Contact's last name
  @JsonProperty(USERID_FIELD)
  private Integer userID; ///< Optional. Contact's user identifier in Telegram

  public Contact() {
    super();
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public Integer getUserID() {
    return userID;
  }

  @Override
  public String toString() {
    return "Contact{" +
        "phoneNumber='" + phoneNumber + '\'' +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", userID=" + userID +
        '}';
  }
}

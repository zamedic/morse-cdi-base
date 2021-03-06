package com.marcarndt.morse.data;

import java.util.List;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * Created by arndt on 2017/04/17.
 */
@Entity
public class UserChatState {

  long chatId;
  String state;
  List<String> fields;
  @Id
  private ObjectId objectId;
  private int userId;

  public UserChatState() {
  }

  public UserChatState(int userId, long chatId,
      String state, List<String> parameters) {
    this.userId = userId;
    this.chatId = chatId;
    this.state = state;
    this.fields = parameters;
  }


  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public long getChatId() {
    return chatId;
  }

  public void setChatId(long chatId) {
    this.chatId = chatId;
  }

  public List<String> getFields() {
    return fields;
  }

  public void setFields(List<String> fields) {
    this.fields = fields;
  }
}


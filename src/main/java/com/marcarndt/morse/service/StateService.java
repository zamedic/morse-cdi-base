package com.marcarndt.morse.service;

import com.marcarndt.morse.MorseBotException;
import com.marcarndt.morse.data.UserChatState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by arndt on 2017/04/17.
 */
public class StateService {

  /**
   * The Mongo service.
   */
  @Inject
  private transient MongoService mongoService;

  /**
   * Gets user state.
   *
   * @param userid Telegram user id
   * @param chatid Telegram chat id
   * @return current user state
   * @throws MorseBotException on exception
   */
  public String getUserState(int userid, long chatid) throws MorseBotException {
    UserChatState userChatState = getUserChatState(userid, chatid);
    if (userChatState == null) {
      throw new MorseBotException("I am not sure how to handle that.");
    }
    return userChatState.getState();
  }


  /**
   * Sets state.
   *
   * @param userid the userid
   * @param chatid the chatid
   * @param state the state
   */
  public void setState(int userid, long chatid, String state) {
    setState(userid, chatid, state, new ArrayList<String>());
  }

  /**
   * Sets state.
   *
   * @param userid the userid
   * @param chatid the chatid
   * @param state the state
   * @param parameters the parameters
   */
  public void setState(int userid, long chatid, String state, List<String> parameters) {
    checkAndDeleteState(userid, chatid);

    UserChatState userChatState = new UserChatState(userid, chatid, state, parameters);
    mongoService.getDatastore().save(userChatState);
  }

  /**
   * Sets state.
   *
   * @param userid Telegram user ID
   * @param chatid Telegram Chat ID
   * @param state New State
   * @param parameters additional parameters
   */
  public void setState(int userid, long chatid, String state, String... parameters) {
    setState(userid, chatid, state, Arrays.asList(parameters));
  }

  private void checkAndDeleteState(int userid, long chatid) {
    UserChatState userChatState = getUserChatState(userid, chatid);
    if (userChatState != null) {
      mongoService.getDatastore().delete(userChatState);
    }
  }



  /**
   * Gets parameters.
   *
   * @param userId the user id
   * @param chatId the chat id
   * @return the parameters
   */
  public List<String> getParameters(int userId, long chatId) {
    UserChatState userChatState = getUserChatState(userId, chatId);
    return userChatState.getFields();
  }

  private UserChatState getUserChatState(int userid, long chatid) {
    return mongoService.getDatastore().createQuery(UserChatState.class).field("userId")
        .equal(userid).field("chatId").equal(chatid).get();
  }

  /**
   * Delete state.
   *
   * @param id the id
   * @param chatId the chat id
   */
  public void deleteState(Integer id, Long chatId) {
    checkAndDeleteState(id, chatId);
  }

}

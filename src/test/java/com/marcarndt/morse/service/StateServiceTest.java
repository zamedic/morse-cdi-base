package com.marcarndt.morse.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.marcarndt.morse.MorseBotException;
import com.marcarndt.morse.data.UserChatState;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.FieldEnd;
import org.mongodb.morphia.query.Query;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Created by arndt on 2017/06/24.
 */
@RunWith(PowerMockRunner.class)
public class StateServiceTest {

  /**
   * The Mongo service.
   */
  @Mock
  private transient MongoService mongoService;
  /**
   * The Datastore.
   */
  @Mock
  private transient Datastore datastore;
  /**
   * The Query.
   */
  @Mock
  private transient Query query;
  /**
   * The Field end.
   */
  @Mock
  private transient FieldEnd fieldEnd;
  /**
   * The User chat state.
   */
  @Mock
  private transient UserChatState userChatState;
  /**
   * The State service.
   */
  @InjectMocks
  private transient StateService stateService;

  /**
   * Setup.
   */
  @Before
  public void setUp() {
    when(mongoService.getDatastore()).thenReturn(datastore);
    when(datastore.createQuery(UserChatState.class)).thenReturn(query);
    when(query.field("userId")).thenReturn(fieldEnd);
    when(fieldEnd.equal(1234)).thenReturn(query);
    when(query.field("chatId")).thenReturn(fieldEnd);
    when(fieldEnd.equal(5678l)).thenReturn(query);
    when(query.get()).thenReturn(userChatState);
  }

  /**
   * Gets user state.
   *
   * @throws Exception the exception
   */
  @Test
  public void getUserState() {

    when(userChatState.getState()).thenReturn("awesome");

    String response = null;
    try {
      response = stateService.getUserState(1234, 5678l);
    } catch (MorseBotException e) {
      fail(e.getMessage());
    }
    assertEquals("State Response", "awesome", response);

  }

  /**
   * Sets state.
   *
   * @throws Exception the exception
   */
  @Test
  public void setState() {

    stateService.setState(1234, 5678l, "TEST");
    verify(datastore).delete(userChatState);
  }

  /**
   * Sets state 1.
   *
   * @throws Exception the exception
   */
  public void setState1() {
    //TODO
  }

  /**
   * Sets state 2.
   *
   * @throws Exception the exception
   */
  public void setState2() {
    //TODO
  }

  /**
   * Gets parameters.
   *
   * @throws Exception the exception
   */
  public void getParameters() {
    //TODO
  }

  /**
   * Delete state.
   *
   * @throws Exception the exception
   */
  public void deleteState() {
    //TODO
  }

}
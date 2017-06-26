package com.marcarndt.morse.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import com.marcarndt.morse.MorseBotException;
import com.marcarndt.morse.data.User;
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
 * Created by arndt on 2017/06/26.
 */
@RunWith(PowerMockRunner.class)
public class UserServiceTest {

  /**
   * Mongo Service
   */
  @Mock
  private transient  MongoService mongoService;
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
   * The Query 2.
   */
  @Mock
  private transient Query query2;

  /**
   * The User service.
   */
  @InjectMocks
  private transient UserService userService;

  /**
   * Sets up.
   */
  @Before
  public void setUp() {
    when(mongoService.getDatastore()).thenReturn(datastore);
    when(datastore.createQuery(User.class)).thenReturn(query);
    when(query.field("userId")).thenReturn(fieldEnd);
    when(fieldEnd.equal(1234)).thenReturn(query2);
  }

  /**
   * Validate user un authenticated.
   */
  @Test
  public void validateUserUnAuthenticated() {
    try {
      assertTrue("Ensure user successfully authenticated. ",userService.validateUser(1234, UserService.UNAUTHENTICATED));
    } catch (MorseBotException e) {
      fail(e.getMessage());
    }
  }

  /**
   * Validate user not exists.
   */
  @Test
  public void validateUserNotExists() {
    when(query2.get()).thenReturn(null);
    try {
      userService.validateUser(1234, UserService.USER);
      fail("User should have failed authentication");
    } catch (MorseBotException e) {
      assertEquals("Ensure user fails authentication with the correct error. ","You are not known to me. Use /register to make me aware of your presence. ",
          e.getMessage());
    }
  }

  /**
   * Validate user exists user role.
   */
  @Test
  public void validateUserExistsUserRole() {
    final User user = new User();
    when(query2.get()).thenReturn(user);
    try {
      assertTrue("Ensure user authenticates",userService.validateUser(1234, UserService.USER));
    } catch (MorseBotException e) {
      fail(e.getMessage());
    }
  }


}
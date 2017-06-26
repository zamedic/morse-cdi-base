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

  @Mock
  MongoService mongoService;
  @Mock
  Datastore datastore;
  @Mock
  Query query;
  @Mock
  FieldEnd fieldEnd;
  @Mock
  Query query2;

  @InjectMocks
  UserService userService;

  @Before
  public void setUp() {
    when(mongoService.getDatastore()).thenReturn(datastore);
    when(datastore.createQuery(User.class)).thenReturn(query);
    when(query.field("userId")).thenReturn(fieldEnd);
    when(fieldEnd.equal(1234)).thenReturn(query2);
  }

  @Test
  public void validateUserUnAuthenticated() {
    try {
      assertTrue(userService.validateUser(1234, UserService.UNAUTHENTICATED));
    } catch (MorseBotException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void validateUserNotExists() {
    when(query2.get()).thenReturn(null);
    try {
      userService.validateUser(1234, UserService.USER);
      fail();
    } catch (MorseBotException e) {
      assertEquals("You are not known to me. Use /register to make me aware of your presence. ",
          e.getMessage());
    }
  }

  @Test
  public void validateUserExistsUserRole() {
    User user = new User();
    when(query2.get()).thenReturn(user);
    try {
      assertTrue(userService.validateUser(1234, UserService.USER));
    } catch (MorseBotException e) {
      fail(e.getMessage());
    }
  }

 
}
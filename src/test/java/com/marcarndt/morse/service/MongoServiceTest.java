package com.marcarndt.morse.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import com.marcarndt.morse.MorseBotConfig;
import com.marcarndt.morse.data.User;
import com.marcarndt.morse.data.UserChatState;
import com.marcarndt.morse.data.UserRole;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import java.util.HashSet;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.annotations.Entity;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.reflections.Reflections;

/**
 * Created by arndt on 2017/06/08.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({MongoService.class, MongoClient.class, MongoClientURI.class})
public class MongoServiceTest {

  /**
   * The Morphia.
   */
  @Mock
  private transient Morphia morphia;
  /**
   * The Reflections.
   */
  @Mock
  private transient Reflections reflections;
  /**
   * The Morse bot config.
   */
  @Mock
  private transient MorseBotConfig morseBotConfig;
  /**
   * The Mongo client.
   */
  @Mock
  private transient MongoClient mongoClient;
  /**
   * The Datastore.
   */
  @Mock
  private transient Datastore datastore;
  /**
   * The Mongo client uri.
   */
  @Mock
  private transient MongoClientURI mongoClientURI;
  /**
   * The Mongo service.
   */
  @InjectMocks
  private transient MongoService mongoService;


  /**
   * Connect.
   *
   * @throws Exception the exception
   */
  @Test
  public void connect() {
    try {
      whenNew(Morphia.class).withNoArguments().thenReturn(morphia);
      whenNew(Reflections.class).withParameterTypes(Object[].class)
          .withArguments(ArgumentMatchers.any()).thenReturn(reflections);
      final Set<Class<?>> classes = new HashSet<>();
      classes.add(User.class);
      classes.add(UserChatState.class);
      classes.add(UserRole.class);
      when(reflections.getTypesAnnotatedWith(Entity.class)).thenReturn(classes);
      when(morseBotConfig.getMongoUser()).thenReturn("testUser");
      when(morseBotConfig.getMongoDatabase()).thenReturn("testDB");
      when(morseBotConfig.getMongoPassword()).thenReturn("testPassword");
      when(morseBotConfig.getMongoAddress()).thenReturn("testAddress");

      whenNew(MongoClientURI.class).withParameterTypes(String.class)
          .withArguments("mongodb://testUser:testPassword@testAddress").thenReturn(mongoClientURI);

      whenNew(MongoClient.class).withParameterTypes(MongoClientURI.class)
          .withArguments(mongoClientURI).thenReturn(mongoClient);

      when(morphia.createDatastore(mongoClient, "testDB")).thenReturn(datastore);

      mongoService.connect();
      final Datastore localDataStore = mongoService.getDatastore();

      Assert.assertEquals(datastore, localDataStore);

      verify(morphia).map(User.class);
      verify(morphia).map(UserChatState.class);
      verify(morphia).map(UserRole.class);
      verify(datastore).ensureIndexes();
    } catch (Exception e) {//NOPMD
      Assert.fail(e.getMessage());
    }
  }


}
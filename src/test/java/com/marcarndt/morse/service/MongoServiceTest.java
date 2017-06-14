package com.marcarndt.morse.service;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import com.marcarndt.morse.MorseBotConfig;
import com.marcarndt.morse.data.User;
import com.marcarndt.morse.data.UserChatState;
import com.marcarndt.morse.data.UserRole;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.annotations.Entity;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.reflections.Reflections;

/**
 * Created by arndt on 2017/06/08.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({MongoService.class, MongoCredential.class, ServerAddress.class, MongoClient.class, ArrayList.class})
public class MongoServiceTest {

  @Mock
  Morphia morphia;
  @Mock
  Reflections reflections;
  @Mock
  MorseBotConfig morseBotConfig;
  @Mock
  MongoCredential  mongoCredential;
  @Mock
  ServerAddress serverAddress;
  @Mock
  MongoClient mongoClient;
  @Mock
  Datastore datastore;
  @InjectMocks
  MongoService mongoService;


  @Test
  public void connect() throws Exception {
    whenNew(Morphia.class).withNoArguments().thenReturn(morphia);
    whenNew(Reflections.class).withParameterTypes(Object[].class).withArguments(ArgumentMatchers.any()).thenReturn(reflections);
    PowerMockito.mockStatic(MongoCredential.class);

    Set<Class<?>> classes = new HashSet<>();
    classes.add(User.class);
    classes.add(UserChatState.class);
    classes.add(UserRole.class);
    when(reflections.getTypesAnnotatedWith(Entity.class)).thenReturn(classes);
    when(morseBotConfig.getMongoUser()).thenReturn("testUser");
    when(morseBotConfig.getMongoDatabase()).thenReturn("testDB");
    when(morseBotConfig.getMongoPassword()).thenReturn("testPassword");
    when(morseBotConfig.getMongoAddress()).thenReturn("testAddress");

    when(MongoCredential.createCredential("testUser","testDB","testPassword".toCharArray())).thenReturn(mongoCredential);
    List<MongoCredential> credentialsList = new ArrayList<MongoCredential>();
    credentialsList.add(mongoCredential);


    whenNew(ServerAddress.class).withArguments("testAddress").thenReturn(serverAddress);

    whenNew(MongoClient.class).withParameterTypes(ServerAddress.class,List.class).withArguments(eq(serverAddress),eq(credentialsList)).thenReturn(mongoClient);

    when(morphia.createDatastore(mongoClient, "testDB")).thenReturn(datastore);

    mongoService.connect();

    verify(morphia).map(User.class);
    verify(morphia).map(UserChatState.class);
    verify(morphia).map(UserRole.class);
    verify(datastore).ensureIndexes();
  }

  @Test
  public void getDatastore() throws Exception {
  }

}
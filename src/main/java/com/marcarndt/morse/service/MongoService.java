package com.marcarndt.morse.service;

import com.marcarndt.morse.MorseBotConfig;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.enterprise.inject.spi.Extension;
import javax.inject.Inject;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.annotations.Entity;
import org.reflections.Reflections;

/**
 * Created by arndt on 2017/04/16.
 */
@Singleton
public class MongoService {

  @Inject
  MorseBotConfig config;

  private Datastore datastore;

  @PostConstruct
  public void connect() {
    Morphia morphia = new Morphia();

    Reflections reflections = new Reflections();
    Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(Entity.class);
    for (Class<?> entity : annotated) {
      morphia.map(entity);
    }

    List<MongoCredential> credentialsList = new ArrayList<MongoCredential>();
    credentialsList.add(MongoCredential.createCredential(config.getMongoUser(),config.getMongoAddress(),config.getMongoPassword().toCharArray()));
    ServerAddress addr = new ServerAddress(config.getMongoAddress());

    datastore = morphia
        .createDatastore(new MongoClient(addr,credentialsList),config.getMongoDatabase());
    datastore.ensureIndexes();

  }

  public Datastore getDatastore() {
    return datastore;
  }
}

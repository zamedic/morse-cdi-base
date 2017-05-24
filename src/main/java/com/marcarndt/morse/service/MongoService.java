package com.marcarndt.morse.service;

import com.marcarndt.morse.MorseBotConfig;
import com.mongodb.MongoClient;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
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
    morphia.mapPackage("com.marcarndt.morsemonkey.service.data");

    Reflections reflections = new Reflections();
    Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(Entity.class);
    for (Class<?> entity : annotated) {
      morphia.map(entity);
    }

    datastore = morphia
        .createDatastore(new MongoClient(config.getMongoAddress()), config.getMongoDatabase());
    datastore.ensureIndexes();

  }

  public Datastore getDatastore() {
    return datastore;
  }
}

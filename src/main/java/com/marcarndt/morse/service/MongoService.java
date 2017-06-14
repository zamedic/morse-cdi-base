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

  /**
   * CDI Injected Config details
   */
  @Inject
  private MorseBotConfig config;

  /**
   * Datastore
   */
  private Datastore datastore;

  /**
   * Connect.
   */
  @PostConstruct
  public void connect() {
    final Morphia morphia = new Morphia();

    final Reflections reflections = new Reflections();
    final Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(Entity.class);
    for (final Class<?> entity : annotated) {
      morphia.map(entity);
    }

    final List<MongoCredential> credentialsList = new ArrayList<MongoCredential>();
    final MongoCredential mongoCredential = MongoCredential
        .createCredential(config.getMongoUser(), config.getMongoDatabase(),
            config.getMongoPassword().toCharArray());
    credentialsList.add(mongoCredential);
    final ServerAddress addr = new ServerAddress(config.getMongoAddress());
    final MongoClient client = new MongoClient(addr, credentialsList);

    datastore = morphia
        .createDatastore(client, config.getMongoDatabase());
    datastore.ensureIndexes();

  }

  /**
   * Gets datastore.
   *
   * @return the datastore
   */
  public Datastore getDatastore() {
    return datastore;
  }
}

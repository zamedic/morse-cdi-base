package com.marcarndt.morse.service;

import com.marcarndt.morse.MorseBotConfig;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
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
   * Logger
   */
  private static final Logger LOG = Logger.getLogger(MongoService.class.getName());

  /**
   * CDI Injected Config details
   */
  @Inject
  private transient MorseBotConfig config;

  /**
   * Datastore
   */
  private transient Datastore datastore;

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

    MongoClientURI uri = null;
    try {
      final String connectionString =
          "mongodb://" + config.getMongoUser() + ":" + URLEncoder
              .encode(config.getMongoPassword(), "UTF-8") + "@" + config
              .getMongoAddress();
      if (LOG.isLoggable(Level.INFO)) {
        LOG.info("Connecting to " + connectionString);//NOPMD
      }
      uri = new MongoClientURI(connectionString);
    } catch (UnsupportedEncodingException e) {
      LOG.log(Level.SEVERE, "Unabele create client connection", e);
    }
    final MongoClient client = new MongoClient(uri);

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

package com.marcarndt.morse.telegrambots.bots;

import com.marcarndt.morse.MorseBotConfig;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.apache.http.HttpHost;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import com.marcarndt.morse.telegrambots.updatesreceivers.ExponentialBackOff;

/**
 * @author Ruben Bermudez
 * @version 1.0
 * @brief Configurations for the Bot
 * @date 21 of July of 2016
 */
@Stateless
public class DefaultBotOptions {

  private final Logger LOG = Logger.getLogger(DefaultBotOptions.class.getName());

  @Inject
  MorseBotConfig botConfig;


  private int maxThreads; ///< Max number of threads used for async methods executions (default 1)
  private ExponentialBackOff exponentialBackOff;
  private Integer maxWebhookConnections;
  private List<String> allowedUpdates;
  private CredentialsProvider httpProxyCredentials;
  private RequestConfig requestConfig;

  @PostConstruct
  public void setup() {
    if (botConfig.getProxyUrl() != null && botConfig.getProxyPort() != 0) {
      HttpHost httpHost = new HttpHost(botConfig.getProxyUrl(), botConfig.getProxyPort());
      requestConfig = RequestConfig.custom().setProxy(httpHost).build();
    } else {
      requestConfig = RequestConfig.DEFAULT;
    }
  }

  public DefaultBotOptions() {
    maxThreads = 1;
  }

  public void setMaxThreads(int maxThreads) {
    this.maxThreads = maxThreads;
  }

  public int getMaxThreads() {
    return maxThreads;
  }

  public RequestConfig getRequestConfig() {
    return requestConfig;
  }

  public Integer getMaxWebhookConnections() {
    return maxWebhookConnections;
  }

  public void setMaxWebhookConnections(Integer maxWebhookConnections) {
    this.maxWebhookConnections = maxWebhookConnections;
  }

  public List<String> getAllowedUpdates() {
    return allowedUpdates;
  }

  public void setAllowedUpdates(List<String> allowedUpdates) {
    this.allowedUpdates = allowedUpdates;
  }


  public ExponentialBackOff getExponentialBackOff() {
    return exponentialBackOff;
  }

  /**
   * @param exponentialBackOff ExponentialBackOff to be used when long polling fails
   * @implSpec Default implementation assumes starting at 500ms and max time of 60 minutes
   */
  public void setExponentialBackOff(ExponentialBackOff exponentialBackOff) {
    this.exponentialBackOff = exponentialBackOff;
  }

  public CredentialsProvider getHttpProxyCredentials() {
    return httpProxyCredentials;
  }

  public void setHttpProxyCredentials(CredentialsProvider httpProxyCredentials) {
    this.httpProxyCredentials = httpProxyCredentials;
  }
}

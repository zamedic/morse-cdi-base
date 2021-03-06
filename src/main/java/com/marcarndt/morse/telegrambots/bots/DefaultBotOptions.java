package com.marcarndt.morse.telegrambots.bots;

import com.marcarndt.morse.MorseBotConfig;
import com.marcarndt.morse.telegrambots.updatesreceivers.ExponentialBackOff;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.apache.http.HttpHost;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;

/**
 * @author Ruben Bermudez
 * @version 1.0
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

  public DefaultBotOptions() {
    maxThreads = 1;
  }

  @PostConstruct
  public void setup() {
    if (botConfig.getProxyUrl() != null && botConfig.getProxyPort() != 0) {
      HttpHost httpHost = new HttpHost(botConfig.getProxyUrl(), botConfig.getProxyPort());
      requestConfig = RequestConfig.custom().setProxy(httpHost).build();
    } else {
      requestConfig = RequestConfig.DEFAULT;
    }
  }

  public int getMaxThreads() {
    return maxThreads;
  }

  public void setMaxThreads(int maxThreads) {
    this.maxThreads = maxThreads;
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

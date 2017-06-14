package com.marcarndt.morse;

import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.marcarndt.morse.telegrambots.api.objects.Chat;
import com.marcarndt.morse.telegrambots.api.objects.Message;
import com.marcarndt.morse.telegrambots.api.objects.User;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Created by arndt on 2017/05/24.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Executors.class, HttpClientBuilder.class})
public class MorseBotTest {


  @Mock
  ExecutorService executorService;

  @Mock
  HttpClientBuilder httpClientBuilder;

  @Mock
  CloseableHttpClient closeableHttpClient;

  @Mock
  CloseableHttpResponse closeableHttpResponse;

  @Mock
  HttpEntity httpEntity;

  @Mock
  User user;

  @Mock
  Chat chat;

  @Mock
  Message message;

  @Mock
  MorseBotConfig botConfig;

  @InjectMocks
  MorseBot morseBot;

  @Before
  public void setupUnit() {
    PowerMockito.mockStatic(Executors.class);
    PowerMockito.mockStatic(HttpClientBuilder.class);

    when(chat.getId()).thenReturn(1l);
    when(botConfig.getKey()).thenReturn("test");

    when(Executors.newFixedThreadPool(anyInt())).thenReturn(executorService);
    when(HttpClientBuilder.create()).thenReturn(httpClientBuilder);
    when(httpClientBuilder.setSSLHostnameVerifier(any())).thenReturn(httpClientBuilder);
    when(httpClientBuilder.setConnectionTimeToLive(70, TimeUnit.SECONDS))
        .thenReturn(httpClientBuilder);
    when(httpClientBuilder.setMaxConnTotal(100)).thenReturn(httpClientBuilder);
    when(httpClientBuilder.setDefaultCredentialsProvider(any())).thenReturn(httpClientBuilder);
    when(httpClientBuilder.build()).thenReturn(closeableHttpClient);
    try {
      when(closeableHttpClient.execute(any())).thenReturn(closeableHttpResponse);
    } catch (IOException e) {
      fail();
    }
    when(closeableHttpResponse.getEntity()).thenReturn(httpEntity);
    when(message.getFrom()).thenReturn(user);
  }

  @Test
  public void sendReplyKeyboardMessage() throws Exception {
    morseBot.sendReplyKeyboardMessage(user, chat, "Test Message", "button1", "button2");
  }

  @Test
  public void sendReplyKeyboardMessage1() throws Exception {
    List buttons = Arrays.asList("test", "test2");
    morseBot.sendReplyKeyboardMessage(user, chat, "test", buttons);
  }

  @Test
  public void sendReplyKeyboardMessage2() throws Exception {
    morseBot.sendReplyKeyboardMessage(message, "test", "test", "test2");
  }

  @Test
  public void sendReplyKeyboardMessage3() throws Exception {
    List buttons = Arrays.asList("test", "test2");
    morseBot.sendReplyKeyboardMessage(message, "test", buttons);
  }

  @Test
  public void sendReplyKeyboardMarkup() throws Exception {
    List buttons = Arrays.asList("test", "test2");
    morseBot.sendReplyKeyboardMarkup(buttons,false);
  }

  @Test
  public void sendReplyMessage() throws Exception {
  }

  @Test
  public void setup() throws Exception {
  }

  @Test
  public void processNonCommandUpdate() throws Exception {
  }

  @Test
  public void sendMessage() throws Exception {
  }

  @Test
  public void getBotUsername() throws Exception {
  }

  @Test
  public void getBotToken() throws Exception {
  }

  @Test
  public void sendMessage1() throws Exception {
  }

  @Test
  public void sendMessage2() throws Exception {
  }

}
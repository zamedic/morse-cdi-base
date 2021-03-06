package com.marcarndt.morse.telegrambots.bots;

import com.marcarndt.morse.telegrambots.ApiConstants;
import com.marcarndt.morse.telegrambots.api.methods.updates.SetWebhook;
import com.marcarndt.morse.telegrambots.exceptions.TelegramApiRequestException;
import com.marcarndt.morse.telegrambots.generics.LongPollingBot;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Ruben Bermudez
 * @version 1.0
 */
public abstract class TelegramLongPollingBot extends DefaultAbsSender implements LongPollingBot {

  @Override
  public void clearWebhook() throws TelegramApiRequestException {
    try (CloseableHttpClient httpclient = HttpClientBuilder.create()
        .setSSLHostnameVerifier(new NoopHostnameVerifier())
        .build()) {
      String url = ApiConstants.BASE_URL + getBotToken() + "/" + SetWebhook.PATH;
      HttpGet httpGet = new HttpGet(url);
      httpGet.setConfig(getOptions().getRequestConfig());
      try (CloseableHttpResponse response = httpclient.execute(httpGet)) {
        HttpEntity ht = response.getEntity();
        BufferedHttpEntity buf = new BufferedHttpEntity(ht);
        String responseContent = EntityUtils.toString(buf, StandardCharsets.UTF_8);
        JSONObject jsonObject = new JSONObject(responseContent);
        if (!jsonObject.getBoolean(ApiConstants.RESPONSE_FIELD_OK)) {
          throw new TelegramApiRequestException("Error removing old webhook", jsonObject);
        }
      }
    } catch (JSONException e) {
      throw new TelegramApiRequestException("Error deserializing setWebhook method response", e);
    } catch (IOException e) {
      throw new TelegramApiRequestException("Error executing setWebook method", e);
    }
  }
}

package com.marcarndt.morse.telegrambots.bots;

import com.marcarndt.morse.telegrambots.ApiConstants;
import com.marcarndt.morse.telegrambots.api.methods.updates.SetWebhook;
import com.marcarndt.morse.telegrambots.exceptions.TelegramApiRequestException;
import com.marcarndt.morse.telegrambots.generics.WebhookBot;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.inject.Inject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Ruben Bermudez
 * @version 1.0 Base abstract class for a bot that will receive updates using a <a
 *          href="https://core.telegram.org/bots/api#setwebhook">webhook</a>
 */

public abstract class TelegramWebhookBot extends DefaultAbsSender implements WebhookBot {

  @Inject
  private DefaultBotOptions botOptions;

  @Override
  public void setWebhook(String url, String publicCertificatePath)
      throws TelegramApiRequestException {
    try (CloseableHttpClient httpclient = HttpClientBuilder.create()
        .setSSLHostnameVerifier(new NoopHostnameVerifier()).build()) {
      String requestUrl = ApiConstants.BASE_URL + getBotToken() + "/" + SetWebhook.PATH;

      HttpPost httppost = new HttpPost(requestUrl);
      httppost.setConfig(botOptions.getRequestConfig());
      MultipartEntityBuilder builder = MultipartEntityBuilder.create();
      builder.addTextBody(SetWebhook.URL_FIELD, url);
      if (botOptions.getMaxWebhookConnections() != null) {
        builder.addTextBody(SetWebhook.MAXCONNECTIONS_FIELD,
            botOptions.getMaxWebhookConnections().toString());
      }
      if (botOptions.getAllowedUpdates() != null) {
        builder.addTextBody(SetWebhook.ALLOWEDUPDATES_FIELD,
            new JSONArray(botOptions.getMaxWebhookConnections()).toString());
      }
      if (publicCertificatePath != null) {
        File certificate = new File(publicCertificatePath);
        if (certificate.exists()) {
          builder.addBinaryBody(SetWebhook.CERTIFICATE_FIELD, certificate, ContentType.TEXT_PLAIN,
              certificate.getName());
        }
      }
      HttpEntity multipart = builder.build();
      httppost.setEntity(multipart);
      try (CloseableHttpResponse response = httpclient.execute(httppost)) {
        HttpEntity ht = response.getEntity();
        BufferedHttpEntity buf = new BufferedHttpEntity(ht);
        String responseContent = EntityUtils.toString(buf, StandardCharsets.UTF_8);
        JSONObject jsonObject = new JSONObject(responseContent);
        if (!jsonObject.getBoolean(ApiConstants.RESPONSE_FIELD_OK)) {
          throw new TelegramApiRequestException("Error setting webhook", jsonObject);
        }
      }
    } catch (JSONException e) {
      throw new TelegramApiRequestException("Error deserializing setWebhook method response", e);
    } catch (IOException e) {
      throw new TelegramApiRequestException("Error executing setWebook method", e);
    }

  }
}

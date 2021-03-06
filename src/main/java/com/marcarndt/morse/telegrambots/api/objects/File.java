package com.marcarndt.morse.telegrambots.api.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.marcarndt.morse.telegrambots.api.interfaces.BotApiObject;
import java.security.InvalidParameterException;
import java.text.MessageFormat;

/**
 * This object represents a file ready to be downloaded
 *
 * @author Ruben Bermudez
 * @version 1.0
 */
public class File implements BotApiObject {

  private static final String FILE_ID = "file_id";
  private static final String FILE_SIZE_FIELD = "file_size";
  private static final String FILE_PATH_FIELD = "file_path";

  @JsonProperty(FILE_ID)
  private String fileId; ///< Unique identifier for this file
  @JsonProperty(FILE_SIZE_FIELD)
  private Integer fileSize; ///< Optional. ChefFile size, if known
  @JsonProperty(FILE_PATH_FIELD)
  private String filePath; ///< Optional. ChefFile path. Use https://api.telegram.org/file/bot<token>/<file_path> to get the file.

  public File() {
    super();
  }

  public static final String getFileUrl(String botToken, String filePath) {
    if (botToken == null || botToken.isEmpty()) {
      throw new InvalidParameterException("Bot token can't be empty");
    }
    return MessageFormat.format("https://api.telegram.org/file/bot{0}/{1}", botToken, filePath);
  }

  public String getFileId() {
    return fileId;
  }

  public Integer getFileSize() {
    return fileSize;
  }

  public String getFilePath() {
    return filePath;
  }

  @Override
  public String toString() {
    return "ChefFile{" +
        "fileId='" + fileId + '\'' +
        ", fileSize=" + fileSize +
        ", filePath='" + filePath + '\'' +
        '}';
  }

  public String getFileUrl(String botToken) {
    return getFileUrl(botToken, filePath);
  }
}

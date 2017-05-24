package com.marcarndt.morse.telegrambots.api.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.marcarndt.morse.telegrambots.api.interfaces.BotApiObject;

/**
 * @author Ruben Bermudez
 * @version 1.0
 *
 *          This object represents a general file (as opposed to photos and audio files). Telegram
 *          users can send files of any type of up to 1.5 GB in size.
 */
public class Document implements BotApiObject {

  private static final String FILEID_FIELD = "file_id";
  private static final String THUMB_FIELD = "thumb";
  private static final String FILENAME_FIELD = "file_name";
  private static final String MIMETYPE_FIELD = "mime_type";
  private static final String FILESIZE_FIELD = "file_size";

  @JsonProperty(FILEID_FIELD)
  private String fileId; ///< Unique identifier for this file
  @JsonProperty(THUMB_FIELD)
  private PhotoSize thumb; ///< Document thumbnail as defined by sender
  @JsonProperty(FILENAME_FIELD)
  private String fileName; ///< Optional. Original filename as defined by sender
  @JsonProperty(MIMETYPE_FIELD)
  private String mimeType; ///< Optional. Mime type of a file as defined by sender
  @JsonProperty(FILESIZE_FIELD)
  private Integer fileSize; ///< Optional. ChefFile size

  public Document() {
    super();
  }

  public String getFileId() {
    return fileId;
  }

  public PhotoSize getThumb() {
    return thumb;
  }

  public String getFileName() {
    return fileName;
  }

  public String getMimeType() {
    return mimeType;
  }

  public Integer getFileSize() {
    return fileSize;
  }

  @Override
  public String toString() {
    return "Document{" +
        "fileId='" + fileId + '\'' +
        ", thumb=" + thumb +
        ", fileName='" + fileName + '\'' +
        ", mimeType='" + mimeType + '\'' +
        ", fileSize=" + fileSize +
        '}';
  }
}

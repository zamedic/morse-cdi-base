package com.marcarndt.morse.telegrambots.api.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.marcarndt.morse.telegrambots.api.interfaces.BotApiObject;

/**
 * @author Ruben Bermudez
 * @version 1.0
 *
 *          This object represents one size of a photo or a file / sticker thumbnail.
 */
public class PhotoSize implements BotApiObject {

  private static final String FILEID_FIELD = "file_id";
  private static final String WIDTH_FIELD = "width";
  private static final String HEIGHT_FIELD = "height";
  private static final String FILESIZE_FIELD = "file_size";
  private static final String FILEPATH_FIELD = "file_path";

  @JsonProperty(FILEID_FIELD)
  private String fileId; ///< Unique identifier for this file
  @JsonProperty(WIDTH_FIELD)
  private Integer width; ///< Photo width
  @JsonProperty(HEIGHT_FIELD)
  private Integer height; ///< Photo height
  @JsonProperty(FILESIZE_FIELD)
  private Integer fileSize; ///< Optional. ChefFile size
  @JsonProperty(FILEPATH_FIELD)
  private String filePath; ///< Undocumented field. Optional. Can contain the path to download the file direclty without calling to getFile

  public PhotoSize() {
    super();
  }

  public String getFileId() {
    return fileId;
  }

  public Integer getWidth() {
    return width;
  }

  public Integer getHeight() {
    return height;
  }

  public Integer getFileSize() {
    return fileSize;
  }

  public String getFilePath() {
    return filePath;
  }

  public boolean hasFilePath() {
    return filePath != null && !filePath.isEmpty();
  }

  @Override
  public String toString() {
    return "PhotoSize{" +
        "fileId='" + fileId + '\'' +
        ", width=" + width +
        ", height=" + height +
        ", fileSize=" + fileSize +
        '}';
  }
}

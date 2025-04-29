package com.everyonewaiter.domain.image;

import com.everyonewaiter.global.support.DateFormatter;
import com.everyonewaiter.global.support.Tsid;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import lombok.NonNull;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

public class ImageMultipartFile implements MultipartFile {

  private final String name;
  private final String originalFilename;
  private final String contentType;
  private final byte[] content;

  public ImageMultipartFile(String prefix, ImageFormat imageFormat, byte[] content) {
    this.name = "image";
    this.originalFilename = generateFileName(prefix, imageFormat);
    this.contentType = imageFormat.getContentType();
    this.content = content;
  }

  @Override
  public @NonNull String getName() {
    return this.name;
  }

  @Override
  public String getOriginalFilename() {
    return this.originalFilename;
  }

  @Override
  public String getContentType() {
    return this.contentType;
  }

  @Override
  public boolean isEmpty() {
    return this.content.length == 0;
  }

  @Override
  public long getSize() {
    return this.content.length;
  }

  @Override
  public byte @NonNull [] getBytes() {
    return this.content;
  }

  @Override
  public @NonNull InputStream getInputStream() {
    return new ByteArrayInputStream(this.content);
  }

  @Override
  public void transferTo(@NonNull File dest) throws IOException {
    try (FileOutputStream outputStream = new FileOutputStream(dest)) {
      outputStream.write(this.content);
    }
  }

  private String generateFileName(String prefix, ImageFormat imageFormat) {
    String formattedPrefix = StringUtils.hasText(prefix) ? prefix + "/" : "";
    String formattedDate = LocalDateTime.now().format(DateFormatter.YEAR_MONTH) + "/";
    return formattedPrefix + formattedDate + Tsid.nextString() + "." + imageFormat.getExtension();
  }

}

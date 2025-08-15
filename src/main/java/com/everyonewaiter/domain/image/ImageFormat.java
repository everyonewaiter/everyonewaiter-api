package com.everyonewaiter.domain.image;

import com.sksamuel.scrimage.nio.ImageWriter;
import com.sksamuel.scrimage.nio.JpegWriter;
import com.sksamuel.scrimage.nio.PngWriter;
import com.sksamuel.scrimage.webp.WebpWriter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;

@Getter
@RequiredArgsConstructor
public enum ImageFormat {

  PNG("png", MediaType.IMAGE_PNG_VALUE, PngWriter.NoCompression),
  JPG("jpg", MediaType.IMAGE_JPEG_VALUE, JpegWriter.Default),
  WEBP("webp", "image/webp", WebpWriter.DEFAULT),
  ;

  private final String extension;
  private final String contentType;
  private final ImageWriter writer;

}

package com.everyonewaiter.adapter.integration.image;

import com.everyonewaiter.application.image.required.ImageFormatConverter;
import com.everyonewaiter.domain.image.FailedConvertImageFormatException;
import com.everyonewaiter.domain.image.ImageFormat;
import com.everyonewaiter.domain.image.ImageMultipartFile;
import com.sksamuel.scrimage.ImmutableImage;
import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
class ImageFormatConverterImpl implements ImageFormatConverter {

  @Override
  public MultipartFile convertToWebp(MultipartFile file, String prefix) {
    try {
      ImageFormat webp = ImageFormat.WEBP;

      byte[] content = ImmutableImage.loader()
          .fromStream(file.getInputStream())
          .bytes(webp.getWriter());

      return new ImageMultipartFile(prefix, webp, content);
    } catch (IOException exception) {
      throw new FailedConvertImageFormatException();
    }
  }

}

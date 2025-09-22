package com.everyonewaiter.application.image;

import static java.util.Objects.requireNonNull;
import static java.util.Objects.requireNonNullElse;
import static org.slf4j.LoggerFactory.getLogger;

import com.everyonewaiter.application.image.provided.ImageManager;
import com.everyonewaiter.application.image.required.ImageClient;
import com.everyonewaiter.application.image.required.ImageFormatConverter;
import com.everyonewaiter.application.image.required.PDFToImageConverter;
import com.everyonewaiter.domain.image.FailedUploadImageException;
import com.everyonewaiter.domain.image.UnsupportedFileTypeException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
class ImageService implements ImageManager {

  private static final Logger LOGGER = getLogger(ImageService.class);

  private final ImageClient imageClient;
  private final ImageFormatConverter imageFormatConverter;
  private final PDFToImageConverter pdfToImageConverter;

  @Override
  public String upload(String prefix, MultipartFile file) {
    MultipartFile imageFile = convertFileToImage(file, prefix);
    String imageFileName = requireNonNull(imageFile.getOriginalFilename());
    File tempFile = new File(imageFileName.replace("/", "."));

    try {
      imageFile.transferTo(tempFile);

      imageClient.upload(tempFile, imageFileName, imageFile.getContentType());

      return imageFileName;
    } catch (IOException exception) {
      LOGGER.error("이미지 업로드 중 임시 파일 생성에 실패하였습니다. {}", imageFileName, exception);

      throw new FailedUploadImageException();
    } finally {
      deleteTempFile(tempFile);
    }
  }

  private MultipartFile convertFileToImage(MultipartFile file, String prefix) {
    String contentType = requireNonNullElse(file.getContentType(), "empty");

    return switch (contentType) {
      case MediaType.IMAGE_PNG_VALUE,
           MediaType.IMAGE_JPEG_VALUE -> imageFormatConverter.convertToWebp(file, prefix);
      case MediaType.APPLICATION_PDF_VALUE -> pdfToImageConverter.convertFirstPage(file, prefix);
      default -> throw new UnsupportedFileTypeException();
    };
  }

  @Override
  public void delete(String imageName) {
    imageClient.delete(imageName);
  }

  private void deleteTempFile(File tempFile) {
    try {
      Files.deleteIfExists(tempFile.toPath());
    } catch (IOException exception) {
      LOGGER.error("파일 삭제에 실패하였습니다. {}", tempFile.getName());
    }
  }

}

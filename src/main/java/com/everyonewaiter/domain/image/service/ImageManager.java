package com.everyonewaiter.domain.image.service;

import com.everyonewaiter.global.exception.BusinessException;
import com.everyonewaiter.global.exception.ErrorCode;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class ImageManager {

  private static final Logger LOGGER = LoggerFactory.getLogger(ImageManager.class);

  private final PDFToImageConverter pdfToImageConverter;
  private final ImageFormatConverter imageFormatConverter;
  private final ImageClient imageClient;

  public String upload(MultipartFile file, String prefix) {
    MultipartFile imageFile = convertFileToImage(file, prefix);
    String imageFileName = Objects.requireNonNull(imageFile.getOriginalFilename());
    File tempFile = new File(imageFileName.replace("/", "."));

    try {
      imageFile.transferTo(tempFile);
      imageClient.upload(tempFile, imageFileName, file.getContentType());
      return imageFileName;
    } catch (IOException exception) {
      throw new BusinessException(ErrorCode.FAILED_UPLOAD_IMAGE);
    } finally {
      deleteTempFile(tempFile);
    }
  }

  private MultipartFile convertFileToImage(MultipartFile file, String prefix) {
    String contentType = Objects.requireNonNullElse(file.getContentType(), "empty");
    return switch (contentType) {
      case MediaType.IMAGE_PNG_VALUE,
           MediaType.IMAGE_JPEG_VALUE -> imageFormatConverter.convertToWebp(file, prefix);
      case MediaType.APPLICATION_PDF_VALUE -> pdfToImageConverter.convertFirstPage(file, prefix);
      default -> throw new BusinessException(ErrorCode.ALLOW_IMAGE_AND_PDF_FILE);
    };
  }

  public void delete(String imageName) {
    try {
      imageClient.delete(imageName);
    } catch (BusinessException exception) {
      LOGGER.error("[이미지 삭제 실패] {}", imageName, exception);
    }
  }

  private void deleteTempFile(File tempFile) {
    try {
      Files.deleteIfExists(tempFile.toPath());
    } catch (IOException exception) {
      LOGGER.error("파일 삭제에 실패하였습니다. {}", tempFile.getName());
    }
  }

}

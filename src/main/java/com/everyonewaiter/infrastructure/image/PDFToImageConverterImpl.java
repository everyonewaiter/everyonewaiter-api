package com.everyonewaiter.infrastructure.image;

import com.everyonewaiter.domain.image.ImageFormat;
import com.everyonewaiter.domain.image.ImageMultipartFile;
import com.everyonewaiter.domain.image.service.PDFToImageConverter;
import com.everyonewaiter.global.exception.BusinessException;
import com.everyonewaiter.global.exception.ErrorCode;
import com.sksamuel.scrimage.ImmutableImage;
import java.awt.image.BufferedImage;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
class PDFToImageConverterImpl implements PDFToImageConverter {

  @Override
  public MultipartFile convertFirstPage(MultipartFile file, String prefix) {
    return convertFirstPage(file, prefix, ImageFormat.WEBP);
  }

  @Override
  public MultipartFile convertFirstPage(MultipartFile file, String prefix, ImageFormat format) {
    try (PDDocument document = PDDocument.load(file.getInputStream())) {
      PDFRenderer pdfRenderer = new PDFRenderer(document);
      BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(0, 300, ImageType.RGB);
      byte[] content = ImmutableImage.fromAwt(bufferedImage).bytes(format.getWriter());
      return new ImageMultipartFile(prefix, format, content);
    } catch (IOException exception) {
      throw new BusinessException(ErrorCode.FAILED_CONVERT_PDF_TO_IMAGE);
    }
  }

}

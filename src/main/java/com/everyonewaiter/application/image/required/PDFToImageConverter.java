package com.everyonewaiter.application.image.required;

import com.everyonewaiter.domain.image.ImageFormat;
import org.springframework.web.multipart.MultipartFile;

public interface PDFToImageConverter {

  MultipartFile convertFirstPage(MultipartFile file, String prefix);

  MultipartFile convertFirstPage(MultipartFile file, String prefix, ImageFormat format);

}

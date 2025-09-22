package com.everyonewaiter.application.image.required;

import org.springframework.web.multipart.MultipartFile;

public interface ImageFormatConverter {

  MultipartFile convertToWebp(MultipartFile file, String prefix);

}

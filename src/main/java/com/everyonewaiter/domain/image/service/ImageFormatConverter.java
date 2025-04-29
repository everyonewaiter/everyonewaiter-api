package com.everyonewaiter.domain.image.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageFormatConverter {

  MultipartFile convertToWebp(MultipartFile file, String prefix);

}

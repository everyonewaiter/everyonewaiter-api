package com.everyonewaiter.application.image.provided;

import org.springframework.web.multipart.MultipartFile;

public interface ImageManager {

  String upload(String prefix, MultipartFile file);

  void delete(String imageName);

}

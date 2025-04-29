package com.everyonewaiter.domain.image.service;

import java.io.File;

public interface ImageClient {

  void upload(File imageFile, String imageName, String contentType);

  void delete(String imageName);

}

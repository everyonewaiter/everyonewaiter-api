package com.everyonewaiter.application.image.required;

import java.io.File;

public interface ImageClient {

  void upload(File imageFile, String imageName, String contentType);

  void delete(String imageName);

}

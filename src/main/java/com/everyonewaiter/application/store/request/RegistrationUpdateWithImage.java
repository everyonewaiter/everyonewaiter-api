package com.everyonewaiter.application.store.request;

import org.springframework.web.multipart.MultipartFile;

public record RegistrationUpdateWithImage(
    String name,
    String ceoName,
    String address,
    String landline,
    String license,
    MultipartFile file
) {

}

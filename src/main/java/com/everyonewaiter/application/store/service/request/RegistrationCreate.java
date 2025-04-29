package com.everyonewaiter.application.store.service.request;

import org.springframework.web.multipart.MultipartFile;

public record RegistrationCreate(
    String name,
    String ceoName,
    String address,
    String landline,
    String license,
    MultipartFile file
) {

}

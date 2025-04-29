package com.everyonewaiter.application.store.request;

public record RegistrationUpdate(
    String name,
    String ceoName,
    String address,
    String landline,
    String license
) {

}

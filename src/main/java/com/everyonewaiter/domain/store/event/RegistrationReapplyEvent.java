package com.everyonewaiter.domain.store.event;

import java.util.Optional;

public record RegistrationReapplyEvent(
    String storeName,
    String rejectReason,
    Optional<String> licenseImage
) {

}

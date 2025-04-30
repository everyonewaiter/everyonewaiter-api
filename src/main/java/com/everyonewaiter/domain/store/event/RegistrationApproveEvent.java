package com.everyonewaiter.domain.store.event;

import com.everyonewaiter.domain.store.entity.BusinessLicense;

public record RegistrationApproveEvent(Long accountId, BusinessLicense businessLicense) {

}

package com.everyonewaiter.application.store;

import com.everyonewaiter.application.store.request.RegistrationCreate;
import com.everyonewaiter.application.store.response.RegistrationDetailResponse;
import com.everyonewaiter.domain.image.service.ImageUploader;
import com.everyonewaiter.domain.store.entity.BusinessLicense;
import com.everyonewaiter.domain.store.entity.Registration;
import com.everyonewaiter.domain.store.repository.RegistrationRepository;
import com.everyonewaiter.global.exception.BusinessException;
import com.everyonewaiter.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreService {

  private final ImageUploader imageUploader;
  private final RegistrationRepository registrationRepository;

  @Transactional
  public Long apply(Long accountId, RegistrationCreate request) {
    BusinessLicense businessLicense = BusinessLicense.create(
        request.name(),
        request.ceoName(),
        request.address(),
        request.landline(),
        request.license(),
        imageUploader.upload(request.file(), "license")
    );
    Registration registration = Registration.create(accountId, businessLicense);
    return registrationRepository.save(registration).getId();
  }

  public RegistrationDetailResponse readRegistration(Long registrationId, Long accountId) {
    return registrationRepository.findByIdAndAccountId(registrationId, accountId)
        .map(RegistrationDetailResponse::from)
        .orElseThrow(() -> new BusinessException(ErrorCode.STORE_REGISTRATION_NOT_FOUND));
  }

}

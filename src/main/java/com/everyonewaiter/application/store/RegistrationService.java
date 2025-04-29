package com.everyonewaiter.application.store;

import com.everyonewaiter.application.store.request.RegistrationCreate;
import com.everyonewaiter.application.store.request.RegistrationPage;
import com.everyonewaiter.application.store.request.RegistrationUpdate;
import com.everyonewaiter.application.store.request.RegistrationUpdateWithImage;
import com.everyonewaiter.application.store.response.RegistrationDetailResponse;
import com.everyonewaiter.domain.image.service.ImageManager;
import com.everyonewaiter.domain.store.entity.BusinessLicense;
import com.everyonewaiter.domain.store.entity.Registration;
import com.everyonewaiter.domain.store.repository.RegistrationRepository;
import com.everyonewaiter.global.exception.BusinessException;
import com.everyonewaiter.global.exception.ErrorCode;
import com.everyonewaiter.global.support.Paging;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegistrationService {

  private final ImageManager imageManager;
  private final RegistrationRepository registrationRepository;

  @Transactional
  public Long apply(Long accountId, RegistrationCreate request) {
    BusinessLicense businessLicense = BusinessLicense.create(
        request.name(),
        request.ceoName(),
        request.address(),
        request.landline(),
        request.license(),
        imageManager.upload(request.file(), "license")
    );
    Registration registration = Registration.create(accountId, businessLicense);
    return registrationRepository.save(registration).getId();
  }

  @Transactional
  public void reapply(Long registrationId, Long accountId, RegistrationUpdate request) {
    registrationRepository.findByIdAndAccountId(registrationId, accountId)
        .ifPresent(registration -> {
          registration.reapply(
              request.name(),
              request.ceoName(),
              request.address(),
              request.landline(),
              request.license()
          );
          registrationRepository.save(registration);
        });
  }

  @Transactional
  public void reapply(Long registrationId, Long accountId, RegistrationUpdateWithImage request) {
    registrationRepository.findByIdAndAccountId(registrationId, accountId)
        .ifPresent(registration -> {
          registration.reapply(
              request.name(),
              request.ceoName(),
              request.address(),
              request.landline(),
              request.license(),
              imageManager.upload(request.file(), "license")
          );
          registrationRepository.save(registration);
        });
  }

  public Paging<RegistrationDetailResponse> readAll(Long accountId, RegistrationPage request) {
    return registrationRepository.findAllByAccountId(accountId, request.pagination())
        .map(RegistrationDetailResponse::from);
  }

  public RegistrationDetailResponse read(Long registrationId, Long accountId) {
    return registrationRepository.findByIdAndAccountId(registrationId, accountId)
        .map(RegistrationDetailResponse::from)
        .orElseThrow(() -> new BusinessException(ErrorCode.STORE_REGISTRATION_NOT_FOUND));
  }

}

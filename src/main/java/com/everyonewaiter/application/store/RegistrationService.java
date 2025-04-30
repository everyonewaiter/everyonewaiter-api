package com.everyonewaiter.application.store;

import com.everyonewaiter.application.store.request.RegistrationAdminPage;
import com.everyonewaiter.application.store.request.RegistrationAdminReject;
import com.everyonewaiter.application.store.request.RegistrationCreate;
import com.everyonewaiter.application.store.request.RegistrationPage;
import com.everyonewaiter.application.store.request.RegistrationUpdate;
import com.everyonewaiter.application.store.request.RegistrationUpdateWithImage;
import com.everyonewaiter.application.store.response.RegistrationAdmin;
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
    Registration registration =
        registrationRepository.findByIdAndAccountIdOrThrow(registrationId, accountId);
    registration.reapply(
        request.name(),
        request.ceoName(),
        request.address(),
        request.landline(),
        request.license()
    );
    registrationRepository.save(registration);
  }

  @Transactional
  public void reapply(Long registrationId, Long accountId, RegistrationUpdateWithImage request) {
    Registration registration =
        registrationRepository.findByIdAndAccountIdOrThrow(registrationId, accountId);
    registration.reapply(
        request.name(),
        request.ceoName(),
        request.address(),
        request.landline(),
        request.license(),
        imageManager.upload(request.file(), "license")
    );
    registrationRepository.save(registration);
  }

  @Transactional
  public void approve(Long registrationId) {
    Registration registration = registrationRepository.findByIdOrThrow(registrationId);
    registration.approve();
    registrationRepository.save(registration);
  }

  @Transactional
  public void reject(Long registrationId, RegistrationAdminReject request) {
    Registration registration = registrationRepository.findByIdOrThrow(registrationId);
    registration.reject(request.rejectReason());
    registrationRepository.save(registration);
  }

  public Paging<RegistrationDetailResponse> readAll(Long accountId, RegistrationPage request) {
    return registrationRepository.findAllByAccountId(accountId, request.pagination())
        .map(RegistrationDetailResponse::from);
  }

  public Paging<RegistrationAdmin.PageViewResponse> readAllByAdmin(RegistrationAdminPage request) {
    return registrationRepository.findAllByAdmin(
            request.email(),
            request.name(),
            request.status(),
            request.pagination()
        )
        .map(RegistrationAdmin.PageViewResponse::from);
  }

  public RegistrationDetailResponse read(Long registrationId, Long accountId) {
    return registrationRepository.findByIdAndAccountId(registrationId, accountId)
        .map(RegistrationDetailResponse::from)
        .orElseThrow(() -> new BusinessException(ErrorCode.STORE_REGISTRATION_NOT_FOUND));
  }

  public RegistrationAdmin.DetailViewResponse readByAdmin(Long registrationId) {
    return registrationRepository.findByAdmin(registrationId)
        .map(RegistrationAdmin.DetailViewResponse::from)
        .orElseThrow(() -> new BusinessException(ErrorCode.STORE_REGISTRATION_NOT_FOUND));
  }

}

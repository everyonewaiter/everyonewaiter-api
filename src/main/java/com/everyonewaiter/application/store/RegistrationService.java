package com.everyonewaiter.application.store;

import com.everyonewaiter.application.store.request.RegistrationAdminRead;
import com.everyonewaiter.application.store.request.RegistrationAdminWrite;
import com.everyonewaiter.application.store.request.RegistrationRead;
import com.everyonewaiter.application.store.request.RegistrationWrite;
import com.everyonewaiter.application.store.response.RegistrationAdminResponse;
import com.everyonewaiter.application.store.response.RegistrationResponse;
import com.everyonewaiter.domain.image.service.ImageManager;
import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;
import com.everyonewaiter.domain.shared.Paging;
import com.everyonewaiter.domain.store.entity.BusinessLicense;
import com.everyonewaiter.domain.store.entity.Registration;
import com.everyonewaiter.domain.store.repository.RegistrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegistrationService {

  private final ImageManager imageManager;
  private final RegistrationRepository registrationRepository;

  @Transactional
  public Long apply(Long accountId, RegistrationWrite.Create request) {
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
  public void reapply(Long registrationId, Long accountId, RegistrationWrite.Update request) {
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
  public void reapply(
      Long registrationId,
      Long accountId,
      RegistrationWrite.UpdateWithImage request
  ) {
    Registration registration =
        registrationRepository.findByIdAndAccountIdOrThrow(registrationId, accountId);
    registration.reapply(
        request.name(),
        request.ceoName(),
        request.address(),
        request.landline(),
        request.license()
    );
    registration.updateLicenseImage(imageManager.upload(request.file(), "license"));
    registrationRepository.save(registration);
  }

  @Transactional
  public void approve(Long registrationId) {
    Registration registration = registrationRepository.findByIdOrThrow(registrationId);
    registration.approve();
    registrationRepository.save(registration);
  }

  @Transactional
  public void reject(Long registrationId, RegistrationAdminWrite.Reject request) {
    Registration registration = registrationRepository.findByIdOrThrow(registrationId);
    registration.reject(request.rejectReason());
    registrationRepository.save(registration);
  }

  public Paging<RegistrationResponse.Detail> readAll(
      Long accountId,
      RegistrationRead.PageView request
  ) {
    return registrationRepository
        .findAllByAccountId(accountId, request.pagination())
        .map(RegistrationResponse.Detail::from);
  }

  public Paging<RegistrationAdminResponse.PageView> readAllByAdmin(
      RegistrationAdminRead.PageView request
  ) {
    return registrationRepository.findAllByAdmin(
            request.email(),
            request.name(),
            request.status(),
            request.pagination()
        )
        .map(RegistrationAdminResponse.PageView::from);
  }

  public RegistrationResponse.Detail read(Long registrationId, Long accountId) {
    return registrationRepository.findByIdAndAccountId(registrationId, accountId)
        .map(RegistrationResponse.Detail::from)
        .orElseThrow(() -> new BusinessException(ErrorCode.STORE_REGISTRATION_NOT_FOUND));
  }

  public RegistrationAdminResponse.DetailView readByAdmin(Long registrationId) {
    return registrationRepository.findByAdmin(registrationId)
        .map(RegistrationAdminResponse.DetailView::from)
        .orElseThrow(() -> new BusinessException(ErrorCode.STORE_REGISTRATION_NOT_FOUND));
  }

}

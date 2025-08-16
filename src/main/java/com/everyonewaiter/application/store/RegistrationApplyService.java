package com.everyonewaiter.application.store;

import com.everyonewaiter.application.image.provided.ImageManager;
import com.everyonewaiter.application.store.provided.RegistrationApplier;
import com.everyonewaiter.application.store.provided.RegistrationFinder;
import com.everyonewaiter.application.store.required.RegistrationRepository;
import com.everyonewaiter.domain.account.Account;
import com.everyonewaiter.domain.store.Registration;
import com.everyonewaiter.domain.store.RegistrationApplyRequest;
import com.everyonewaiter.domain.store.RegistrationReapplyRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
@Transactional
@RequiredArgsConstructor
class RegistrationApplyService implements RegistrationApplier {

  private final ImageManager imageManager;
  private final RegistrationFinder registrationFinder;
  private final RegistrationRepository registrationRepository;

  @Override
  public Registration apply(Account account, RegistrationApplyRequest applyRequest) {
    String licenseImage = imageManager.upload("license", applyRequest.file());

    Registration registration = Registration.create(account, applyRequest, licenseImage);

    return registrationRepository.save(registration);
  }

  @Override
  public Registration reapply(
      Long registrationId,
      Long accountId,
      RegistrationApplyRequest applyRequest
  ) {
    Registration registration = registrationFinder.findOrThrow(registrationId, accountId);

    registration.reapply(applyRequest, imageManager.upload("license", applyRequest.file()));

    return registrationRepository.save(registration);
  }

  @Override
  public Registration reapply(
      Long registrationId,
      Long accountId,
      RegistrationReapplyRequest reapplyRequest
  ) {
    Registration registration = registrationFinder.findOrThrow(registrationId, accountId);

    registration.reapply(reapplyRequest);

    return registrationRepository.save(registration);
  }

}

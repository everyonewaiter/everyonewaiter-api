package com.everyonewaiter.application.store;

import com.everyonewaiter.application.store.provided.RegistrationFinder;
import com.everyonewaiter.application.store.required.RegistrationRepository;
import com.everyonewaiter.application.support.ReadOnlyTransactional;
import com.everyonewaiter.domain.shared.Paging;
import com.everyonewaiter.domain.store.Registration;
import com.everyonewaiter.domain.store.RegistrationAdminPageRequest;
import com.everyonewaiter.domain.store.RegistrationPageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
@ReadOnlyTransactional
@RequiredArgsConstructor
class RegistrationQueryService implements RegistrationFinder {

  private final RegistrationRepository registrationRepository;

  @Override
  public Registration findOrThrow(Long registrationId) {
    return registrationRepository.findByIdOrThrow(registrationId);
  }

  @Override
  public Registration findOrThrow(Long registrationId, Long accountId) {
    return registrationRepository.findByIdAndAccountIdOrThrow(registrationId, accountId);
  }

  @Override
  public Paging<Registration> findAll(Long accountId, RegistrationPageRequest pageRequest) {
    return registrationRepository.findAllByAccountId(accountId, pageRequest);
  }

  @Override
  public Registration findByAdminOrThrow(Long registrationId) {
    return registrationRepository.findByAdminOrThrow(registrationId);
  }

  @Override
  public Paging<Registration> findAllByAdmin(RegistrationAdminPageRequest pageRequest) {
    return registrationRepository.findAllByAdmin(pageRequest);
  }

}

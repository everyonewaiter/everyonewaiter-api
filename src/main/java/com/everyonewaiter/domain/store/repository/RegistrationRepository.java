package com.everyonewaiter.domain.store.repository;

import com.everyonewaiter.domain.shared.Pagination;
import com.everyonewaiter.domain.shared.Paging;
import com.everyonewaiter.domain.store.entity.Registration;
import com.everyonewaiter.domain.store.view.RegistrationAdminView;
import jakarta.annotation.Nullable;
import java.util.Optional;

public interface RegistrationRepository {

  Paging<Registration> findAllByAccountId(Long accountId, Pagination pagination);

  Paging<RegistrationAdminView.Page> findAllByAdmin(
      @Nullable String email,
      @Nullable String name,
      @Nullable Registration.Status status,
      Pagination pagination
  );

  Registration findByIdOrThrow(Long registrationId);

  Optional<Registration> findByIdAndAccountId(Long registrationId, Long accountId);

  Registration findByIdAndAccountIdOrThrow(Long registrationId, Long accountId);

  Optional<RegistrationAdminView.Detail> findByAdmin(Long registrationId);

  Registration save(Registration registration);

}

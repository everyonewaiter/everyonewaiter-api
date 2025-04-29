package com.everyonewaiter.domain.store.repository;

import com.everyonewaiter.domain.store.entity.Registration;
import com.everyonewaiter.domain.store.view.RegistrationAdminDetailView;
import com.everyonewaiter.domain.store.view.RegistrationAdminPageView;
import com.everyonewaiter.global.support.Pagination;
import com.everyonewaiter.global.support.Paging;
import jakarta.annotation.Nullable;
import java.util.Optional;

public interface RegistrationRepository {

  Paging<Registration> findAllByAccountId(Long accountId, Pagination pagination);

  Paging<RegistrationAdminPageView> findAllByAdmin(
      @Nullable String email,
      @Nullable String name,
      @Nullable Registration.Status status,
      Pagination pagination
  );

  Optional<Registration> findByIdAndAccountId(Long registrationId, Long accountId);

  Optional<RegistrationAdminDetailView> findByAdmin(Long registrationId);

  Registration save(Registration registration);

}

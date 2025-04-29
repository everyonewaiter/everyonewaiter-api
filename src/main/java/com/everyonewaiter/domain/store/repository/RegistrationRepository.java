package com.everyonewaiter.domain.store.repository;

import com.everyonewaiter.domain.store.entity.Registration;
import com.everyonewaiter.global.support.Pagination;
import com.everyonewaiter.global.support.Paging;
import java.util.Optional;

public interface RegistrationRepository {

  Paging<Registration> findAllByAccountId(Long accountId, Pagination pagination);

  Optional<Registration> findByIdAndAccountId(Long registrationId, Long accountId);

  Registration save(Registration registration);

}

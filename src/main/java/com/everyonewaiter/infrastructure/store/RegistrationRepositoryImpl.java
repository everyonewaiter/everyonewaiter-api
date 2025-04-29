package com.everyonewaiter.infrastructure.store;

import static com.everyonewaiter.domain.store.entity.QBusinessLicense.businessLicense;
import static com.everyonewaiter.domain.store.entity.QRegistration.registration;

import com.everyonewaiter.domain.store.entity.Registration;
import com.everyonewaiter.domain.store.repository.RegistrationRepository;
import com.everyonewaiter.global.support.Pagination;
import com.everyonewaiter.global.support.Paging;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class RegistrationRepositoryImpl implements RegistrationRepository {

  private final JPAQueryFactory queryFactory;
  private final RegistrationJpaRepository registrationJpaRepository;

  @Override
  public Paging<Registration> findAllByAccountId(Long accountId, Pagination pagination) {
    List<Registration> registrations = queryFactory
        .select(registration)
        .from(registration)
        .leftJoin(registration.businessLicense, businessLicense).fetchJoin()
        .where(registration.accountId.eq(accountId))
        .orderBy(registration.id.desc())
        .limit(pagination.limit())
        .offset(pagination.offset())
        .fetch();

    Long count = queryFactory
        .select(registration.count())
        .from(registration)
        .where(registration.accountId.eq(accountId))
        .orderBy(registration.id.desc())
        .limit(pagination.countLimit())
        .fetchOne();

    return new Paging<>(registrations, Objects.requireNonNull(count), pagination);
  }

  @Override
  public Optional<Registration> findByIdAndAccountId(Long registrationId, Long accountId) {
    return registrationJpaRepository.findByIdAndAccountId(registrationId, accountId);
  }

  @Override
  public Registration save(Registration registration) {
    return registrationJpaRepository.save(registration);
  }

}

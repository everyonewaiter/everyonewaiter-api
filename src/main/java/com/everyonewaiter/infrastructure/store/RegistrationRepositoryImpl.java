package com.everyonewaiter.infrastructure.store;

import static com.everyonewaiter.domain.account.entity.QAccount.account;
import static com.everyonewaiter.domain.store.entity.QBusinessLicense.businessLicense;
import static com.everyonewaiter.domain.store.entity.QRegistration.registration;

import com.everyonewaiter.domain.store.entity.Registration;
import com.everyonewaiter.domain.store.repository.RegistrationRepository;
import com.everyonewaiter.domain.store.view.RegistrationAdminPageView;
import com.everyonewaiter.global.support.Pagination;
import com.everyonewaiter.global.support.Paging;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

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
        .innerJoin(registration.businessLicense, businessLicense).fetchJoin()
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
  public Paging<RegistrationAdminPageView> findAllByAdmin(
      @Nullable String email,
      @Nullable String name,
      @Nullable Registration.Status status,
      Pagination pagination
  ) {
    List<RegistrationAdminPageView> views = queryFactory
        .select(
            Projections.constructor(
                RegistrationAdminPageView.class,
                registration.id,
                registration.accountId,
                account.email,
                registration.businessLicense.name,
                registration.status,
                registration.createdAt,
                registration.updatedAt
            )
        )
        .from(registration)
        .innerJoin(registration.businessLicense, businessLicense)
        .innerJoin(account).on(registration.accountId.eq(account.id))
        .where(
            emailStratsWith(email),
            nameStartsWith(name),
            statusEq(status)
        )
        .orderBy(registration.id.desc())
        .limit(pagination.limit())
        .offset(pagination.offset())
        .fetch();

    Long count = queryFactory
        .select(registration.count())
        .from(registration)
        .innerJoin(registration.businessLicense, businessLicense)
        .innerJoin(account).on(registration.accountId.eq(account.id))
        .where(
            emailStratsWith(email),
            nameStartsWith(name),
            statusEq(status)
        )
        .orderBy(registration.id.desc())
        .limit(pagination.countLimit())
        .fetchOne();

    return new Paging<>(views, Objects.requireNonNull(count), pagination);
  }

  @Override
  public Optional<Registration> findByIdAndAccountId(Long registrationId, Long accountId) {
    return registrationJpaRepository.findByIdAndAccountId(registrationId, accountId);
  }

  @Override
  public Registration save(Registration registration) {
    return registrationJpaRepository.save(registration);
  }

  private BooleanExpression emailStratsWith(@Nullable String email) {
    return StringUtils.hasText(email) ? account.email.startsWith(email) : null;
  }

  private BooleanExpression nameStartsWith(@Nullable String name) {
    return StringUtils.hasText(name) ? registration.businessLicense.name.startsWith(name) : null;
  }

  private BooleanExpression statusEq(@Nullable Registration.Status status) {
    return status != null ? registration.status.eq(status) : null;
  }

}

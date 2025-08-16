package com.everyonewaiter.adapter.persistence.store;

import static com.everyonewaiter.domain.account.QAccount.account;
import static com.everyonewaiter.domain.store.QRegistration.registration;
import static java.util.Objects.requireNonNull;

import com.everyonewaiter.application.store.required.RegistrationRepository;
import com.everyonewaiter.domain.account.Account;
import com.everyonewaiter.domain.shared.Pagination;
import com.everyonewaiter.domain.shared.Paging;
import com.everyonewaiter.domain.store.Registration;
import com.everyonewaiter.domain.store.RegistrationAdminPageRequest;
import com.everyonewaiter.domain.store.RegistrationNotFoundException;
import com.everyonewaiter.domain.store.RegistrationPageRequest;
import com.everyonewaiter.domain.store.RegistrationStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.Nullable;
import java.util.List;
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
  public Registration findByIdOrThrow(Long registrationId) {
    return registrationJpaRepository.findById(registrationId)
        .orElseThrow(RegistrationNotFoundException::new);
  }

  @Override
  public Registration findByIdAndAccountIdOrThrow(Long registrationId, Long accountId) {
    return registrationJpaRepository.findByIdAndAccountId(registrationId, accountId)
        .orElseThrow(RegistrationNotFoundException::new);
  }

  @Override
  public Paging<Registration> findAllByAccountId(
      Long accountId,
      RegistrationPageRequest pageRequest
  ) {
    Pagination pagination = new Pagination(pageRequest.getPage(), pageRequest.getSize());

    List<Registration> registrations = queryFactory
        .select(registration)
        .from(registration)
        .where(registration.account.id.eq(accountId))
        .orderBy(registration.id.desc())
        .limit(pagination.limit())
        .offset(pagination.offset())
        .fetch();

    Long count = queryFactory
        .select(registration.count())
        .from(registration)
        .where(registration.account.id.eq(accountId))
        .orderBy(registration.id.desc())
        .limit(pagination.countLimit())
        .fetchOne();

    return new Paging<>(registrations, requireNonNull(count), pagination);
  }

  @Override
  public Registration findByAdminOrThrow(Long registrationId) {
    return Optional.ofNullable(
            queryFactory
                .select(registration)
                .from(registration)
                .innerJoin(registration.account, account).fetchJoin()
                .where(registration.id.eq(registrationId))
                .fetchOne()
        )
        .orElseThrow(RegistrationNotFoundException::new);
  }

  @Override
  public Paging<Registration> findAllByAdmin(
      RegistrationAdminPageRequest pageRequest
  ) {
    PathBuilder<Account> accountPath = new PathBuilder<>(account.getType(), account.getMetadata());
    PathBuilder<Registration> registrationPath =
        new PathBuilder<>(registration.getType(), registration.getMetadata());

    String email = pageRequest.getEmail();
    String name = pageRequest.getName();
    RegistrationStatus status = pageRequest.getStatus();
    Pagination pagination = new Pagination(pageRequest.getPage(), pageRequest.getSize());

    List<Registration> registrations = queryFactory
        .select(registration)
        .from(registration)
        .innerJoin(registration.account, account).fetchJoin()
        .where(
            emailStratsWith(accountPath, email),
            nameStartsWith(registrationPath, name),
            statusEq(status)
        )
        .orderBy(registration.id.desc())
        .limit(pagination.limit())
        .offset(pagination.offset())
        .fetch();

    Long count = queryFactory
        .select(registration.count())
        .from(registration)
        .innerJoin(registration.account, account).fetchJoin()
        .where(
            emailStratsWith(accountPath, email),
            nameStartsWith(registrationPath, name),
            statusEq(status)
        )
        .orderBy(registration.id.desc())
        .limit(pagination.countLimit())
        .fetchOne();

    return new Paging<>(registrations, requireNonNull(count), pagination);
  }

  @Override
  public Registration save(Registration registration) {
    return registrationJpaRepository.save(registration);
  }

  @Nullable
  private BooleanExpression emailStratsWith(
      PathBuilder<Account> accountPath,
      @Nullable String email
  ) {
    return StringUtils.hasText(email)
        ? accountPath.get("email").getString("address").startsWith(email)
        : null;
  }

  @Nullable
  private BooleanExpression nameStartsWith(
      PathBuilder<Registration> registrationPath,
      @Nullable String name
  ) {
    return StringUtils.hasText(name)
        ? registrationPath.get("detail").getString("name").startsWith(name)
        : null;
  }

  @Nullable
  private BooleanExpression statusEq(@Nullable RegistrationStatus status) {
    return status != null ? registration.status.eq(status) : null;
  }

}

package com.everyonewaiter.infrastructure.store;

import static com.everyonewaiter.domain.account.QAccount.account;
import static com.everyonewaiter.domain.store.entity.QRegistration.registration;

import com.everyonewaiter.domain.account.Account;
import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;
import com.everyonewaiter.domain.shared.Pagination;
import com.everyonewaiter.domain.shared.Paging;
import com.everyonewaiter.domain.store.entity.Registration;
import com.everyonewaiter.domain.store.repository.RegistrationRepository;
import com.everyonewaiter.domain.store.view.RegistrationAdminView;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
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
  public Paging<RegistrationAdminView.Page> findAllByAdmin(
      @Nullable String email,
      @Nullable String name,
      @Nullable Registration.Status status,
      Pagination pagination
  ) {
    PathBuilder<Account> accountPath = new PathBuilder<>(account.getType(), account.getMetadata());

    List<RegistrationAdminView.Page> views = queryFactory
        .select(
            Projections.constructor(
                RegistrationAdminView.Page.class,
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
        .innerJoin(account).on(registration.accountId.eq(account.id))
        .where(
            emailStratsWith(accountPath, email),
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
        .innerJoin(account).on(registration.accountId.eq(account.id))
        .where(
            emailStratsWith(accountPath, email),
            nameStartsWith(name),
            statusEq(status)
        )
        .orderBy(registration.id.desc())
        .limit(pagination.countLimit())
        .fetchOne();

    return new Paging<>(views, Objects.requireNonNull(count), pagination);
  }

  @Override
  public Registration findByIdOrThrow(Long registrationId) {
    return registrationJpaRepository.findById(registrationId)
        .orElseThrow(() -> new BusinessException(ErrorCode.STORE_REGISTRATION_NOT_FOUND));
  }

  @Override
  public Optional<Registration> findByIdAndAccountId(Long registrationId, Long accountId) {
    return registrationJpaRepository.findByIdAndAccountId(registrationId, accountId);
  }

  @Override
  public Registration findByIdAndAccountIdOrThrow(Long registrationId, Long accountId) {
    return findByIdAndAccountId(registrationId, accountId)
        .orElseThrow(() -> new BusinessException(ErrorCode.STORE_REGISTRATION_NOT_FOUND));
  }

  @Override
  public Optional<RegistrationAdminView.Detail> findByAdmin(Long registrationId) {
    return Optional.ofNullable(
        queryFactory
            .select(
                Projections.constructor(
                    RegistrationAdminView.Detail.class,
                    registration.id,
                    registration.accountId,
                    account.email,
                    registration.businessLicense.name,
                    registration.businessLicense.ceoName,
                    registration.businessLicense.address,
                    registration.businessLicense.landline,
                    registration.businessLicense.license,
                    registration.businessLicense.licenseImage,
                    registration.status,
                    registration.createdAt,
                    registration.updatedAt
                )
            )
            .from(registration)
            .where(registration.id.eq(registrationId))
            .innerJoin(account).on(registration.accountId.eq(account.id))
            .fetchOne()
    );
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
  private BooleanExpression nameStartsWith(@Nullable String name) {
    return StringUtils.hasText(name) ? registration.businessLicense.name.startsWith(name) : null;
  }

  @Nullable
  private BooleanExpression statusEq(@Nullable Registration.Status status) {
    return status != null ? registration.status.eq(status) : null;
  }

}

package com.everyonewaiter.infrastructure.account;

import static com.everyonewaiter.domain.account.entity.QAccount.account;
import static com.everyonewaiter.domain.store.entity.QStore.store;

import com.everyonewaiter.domain.account.entity.Account;
import com.everyonewaiter.domain.account.repository.AccountRepository;
import com.everyonewaiter.domain.account.view.AccountAdminPageView;
import com.everyonewaiter.global.exception.BusinessException;
import com.everyonewaiter.global.exception.ErrorCode;
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
class AccountRepositoryImpl implements AccountRepository {

  private final JPAQueryFactory queryFactory;
  private final AccountJpaRepository accountJpaRepository;

  @Override
  public boolean existsByEmail(String email) {
    return accountJpaRepository.existsByEmail(email);
  }

  @Override
  public boolean existsByPhoneNumber(String phoneNumber) {
    return accountJpaRepository.existsByPhoneNumber(phoneNumber);
  }

  @Override
  public Paging<AccountAdminPageView> findAllByAdmin(
      @Nullable String email,
      @Nullable Account.State state,
      @Nullable Account.Permission permission,
      @Nullable Boolean hasStore,
      Pagination pagination
  ) {
    List<AccountAdminPageView> views = queryFactory
        .select(
            Projections.constructor(
                AccountAdminPageView.class,
                account.id,
                account.email,
                account.state,
                account.permission,
                store.id.isNotNull(),
                account.createdAt,
                account.updatedAt
            )
        )
        .distinct()
        .from(account)
        .leftJoin(store).on(account.id.eq(store.accountId))
        .where(
            emailStratsWith(email),
            stateEq(state),
            permissionEq(permission),
            hasStoreEq(hasStore)
        )
        .orderBy(account.id.desc())
        .limit(pagination.limit())
        .offset(pagination.offset())
        .fetch();

    Long count = queryFactory
        .select(account.countDistinct())
        .from(account)
        .leftJoin(store).on(account.id.eq(store.accountId))
        .where(
            emailStratsWith(email),
            stateEq(state),
            permissionEq(permission),
            hasStoreEq(hasStore)
        )
        .orderBy(account.id.desc())
        .limit(pagination.countLimit())
        .fetchOne();

    return new Paging<>(views, Objects.requireNonNull(count), pagination);
  }

  @Override
  public Optional<Account> findByEmail(String email) {
    return accountJpaRepository.findByEmail(email);
  }

  @Override
  public Account findByEmailOrThrow(String email) {
    return findByEmail(email).orElseThrow(() -> new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND));
  }

  @Override
  public Optional<Account> findById(Long accountId) {
    return accountJpaRepository.findById(accountId);
  }

  @Override
  public Account findByIdOrThrow(Long accountId) {
    return findById(accountId)
        .orElseThrow(() -> new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND));
  }

  @Override
  public Account save(Account account) {
    return accountJpaRepository.save(account);
  }

  private BooleanExpression emailStratsWith(@Nullable String email) {
    return StringUtils.hasText(email) ? account.email.startsWith(email) : null;
  }

  private BooleanExpression stateEq(@Nullable Account.State state) {
    return state != null ? account.state.eq(state) : null;
  }

  private BooleanExpression permissionEq(@Nullable Account.Permission permission) {
    return permission != null ? account.permission.eq(permission) : null;
  }

  private BooleanExpression hasStoreEq(@Nullable Boolean hasStore) {
    if (hasStore == null) {
      return null;
    }
    return hasStore ? store.id.isNotNull() : store.id.isNull();
  }

}

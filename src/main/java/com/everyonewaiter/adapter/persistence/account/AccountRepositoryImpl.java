package com.everyonewaiter.adapter.persistence.account;

import static com.everyonewaiter.domain.account.QAccount.account;
import static com.everyonewaiter.domain.store.QStore.store;
import static java.util.Objects.requireNonNull;
import static org.springframework.util.StringUtils.hasText;

import com.everyonewaiter.application.account.required.AccountRepository;
import com.everyonewaiter.domain.account.Account;
import com.everyonewaiter.domain.account.AccountAdminPageRequest;
import com.everyonewaiter.domain.account.AccountAdminPageView;
import com.everyonewaiter.domain.account.AccountNotFoundException;
import com.everyonewaiter.domain.account.AccountPermission;
import com.everyonewaiter.domain.account.AccountState;
import com.everyonewaiter.domain.shared.Email;
import com.everyonewaiter.domain.shared.Pagination;
import com.everyonewaiter.domain.shared.Paging;
import com.everyonewaiter.domain.shared.PhoneNumber;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class AccountRepositoryImpl implements AccountRepository {

  private final JPAQueryFactory queryFactory;
  private final AccountJpaRepository accountJpaRepository;

  @Override
  public boolean exists(Email email) {
    return accountJpaRepository.existsByEmail(email);
  }

  @Override
  public boolean exists(Email email, AccountState state) {
    return accountJpaRepository.existsByEmailAndState(email, state);
  }

  @Override
  public boolean exists(PhoneNumber phoneNumber) {
    return accountJpaRepository.existsByPhoneNumber(phoneNumber);
  }

  @Override
  public boolean exists(PhoneNumber phoneNumber, AccountState state) {
    return accountJpaRepository.existsByPhoneNumberAndState(phoneNumber, state);
  }

  @Override
  public Paging<AccountAdminPageView> findAll(AccountAdminPageRequest pageRequest) {
    String email = pageRequest.getEmail();
    AccountState state = pageRequest.getState();
    AccountPermission permission = pageRequest.getPermission();
    Boolean hasStore = pageRequest.getHasStore();
    Pagination pagination = new Pagination(pageRequest.getPage(), pageRequest.getSize());

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
        .leftJoin(store).on(account.id.eq(store.account.id))
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
        .leftJoin(store).on(account.id.eq(store.account.id))
        .where(
            emailStratsWith(email),
            stateEq(state),
            permissionEq(permission),
            hasStoreEq(hasStore)
        )
        .orderBy(account.id.desc())
        .limit(pagination.countLimit())
        .fetchOne();

    return new Paging<>(views, requireNonNull(count), pagination);
  }

  @Override
  public Optional<Account> find(Long accountId) {
    return accountJpaRepository.findById(accountId);
  }

  @Override
  public Account findOrThrow(Long accountId) {
    return find(accountId).orElseThrow(AccountNotFoundException::new);
  }

  @Override
  public Optional<Account> find(Email email) {
    return accountJpaRepository.findByEmail(email);
  }

  @Override
  public Account findOrThrow(Email email) {
    return find(email).orElseThrow(AccountNotFoundException::new);
  }

  @Override
  public Account findOrThrow(PhoneNumber phoneNumber) {
    return accountJpaRepository.findByPhoneNumber(phoneNumber)
        .orElseThrow(AccountNotFoundException::new);
  }

  @Override
  public Account save(Account account) {
    return accountJpaRepository.save(account);
  }

  @Nullable
  private BooleanExpression emailStratsWith(@Nullable String email) {
    return hasText(email) ? account.email.address.startsWith(email) : null;
  }

  @Nullable
  private BooleanExpression stateEq(@Nullable AccountState state) {
    return state != null ? account.state.eq(state) : null;
  }

  @Nullable
  private BooleanExpression permissionEq(@Nullable AccountPermission permission) {
    return permission != null ? account.permission.eq(permission) : null;
  }

  @Nullable
  private BooleanExpression hasStoreEq(@Nullable Boolean hasStore) {
    if (hasStore == null) {
      return null;
    }
    return hasStore ? store.id.isNotNull() : store.id.isNull();
  }

}

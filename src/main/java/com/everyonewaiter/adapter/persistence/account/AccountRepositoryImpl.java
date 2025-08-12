package com.everyonewaiter.adapter.persistence.account;

import static com.everyonewaiter.domain.account.entity.QAccount.account;
import static com.everyonewaiter.domain.store.entity.QStore.store;

import com.everyonewaiter.application.account.required.AccountRepository;
import com.everyonewaiter.domain.account.AccountNotFoundException;
import com.everyonewaiter.domain.account.entity.Account;
import com.everyonewaiter.domain.account.view.AccountAdminView;
import com.everyonewaiter.domain.shared.Email;
import com.everyonewaiter.domain.shared.Pagination;
import com.everyonewaiter.domain.shared.Paging;
import com.everyonewaiter.domain.shared.PhoneNumber;
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
class AccountRepositoryImpl implements AccountRepository {

  private final JPAQueryFactory queryFactory;
  private final AccountJpaRepository accountJpaRepository;

  @Override
  public boolean exists(Email email) {
    return accountJpaRepository.existsByEmail(email);
  }

  @Override
  public boolean existsInactive(Email email) {
    return accountJpaRepository.existsByEmailAndState(email, Account.State.INACTIVE);
  }

  @Override
  public boolean exists(PhoneNumber phoneNumber) {
    return accountJpaRepository.existsByPhoneNumber(phoneNumber);
  }

  @Override
  public Paging<AccountAdminView.Page> findAllByAdmin(
      @Nullable String email,
      @Nullable Account.State state,
      @Nullable Account.Permission permission,
      @Nullable Boolean hasStore,
      Pagination pagination
  ) {
    PathBuilder<Account> accountPath = new PathBuilder<>(account.getType(), account.getMetadata());

    List<AccountAdminView.Page> views = queryFactory
        .select(
            Projections.constructor(
                AccountAdminView.Page.class,
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
            emailStratsWith(accountPath, email),
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
            emailStratsWith(accountPath, email),
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
  public Optional<Account> findById(Long accountId) {
    return accountJpaRepository.findById(accountId);
  }

  @Override
  public Account findByIdOrThrow(Long accountId) {
    return findById(accountId).orElseThrow(AccountNotFoundException::new);
  }

  @Override
  public Optional<Account> findByEmail(Email email) {
    return accountJpaRepository.findByEmail(email);
  }

  @Override
  public Account findByEmailOrThrow(Email email) {
    return findByEmail(email).orElseThrow(AccountNotFoundException::new);
  }

  @Override
  public Account findByPhoneOrThrow(PhoneNumber phoneNumber) {
    return accountJpaRepository.findByPhoneNumber(phoneNumber)
        .orElseThrow(AccountNotFoundException::new);
  }

  @Override
  public Account save(Account account) {
    return accountJpaRepository.save(account);
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
  private BooleanExpression stateEq(@Nullable Account.State state) {
    return state != null ? account.state.eq(state) : null;
  }

  @Nullable
  private BooleanExpression permissionEq(@Nullable Account.Permission permission) {
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

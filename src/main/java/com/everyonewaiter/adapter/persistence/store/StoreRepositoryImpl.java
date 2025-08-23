package com.everyonewaiter.adapter.persistence.store;

import static com.everyonewaiter.domain.account.QAccount.account;
import static com.everyonewaiter.domain.store.QSetting.setting;
import static com.everyonewaiter.domain.store.QStore.store;

import com.everyonewaiter.application.store.required.StoreRepository;
import com.everyonewaiter.domain.shared.PhoneNumber;
import com.everyonewaiter.domain.store.Store;
import com.everyonewaiter.domain.store.StoreNotFoundException;
import com.everyonewaiter.domain.store.StoreStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class StoreRepositoryImpl implements StoreRepository {

  private final JPAQueryFactory queryFactory;
  private final StoreJpaRepository storeJpaRepository;

  @Override
  public boolean exists(Long storeId) {
    return storeJpaRepository.existsById(storeId);
  }

  @Override
  public boolean exists(Long storeId, Long accountId) {
    return storeJpaRepository.existsByIdAndAccountId(storeId, accountId);
  }

  @Override
  public boolean exists(Long storeId, StoreStatus status) {
    return storeJpaRepository.existsByIdAndStatus(storeId, status);
  }

  @Override
  public List<Store> findAll(Long accountId) {
    return queryFactory
        .select(store)
        .from(store)
        .where(store.account.id.eq(accountId))
        .fetch();
  }

  @Override
  public List<Store> findAll(PhoneNumber phoneNumber) {
    return queryFactory
        .select(store)
        .from(store)
        .innerJoin(store.account, account).fetchJoin()
        .where(store.account.phoneNumber.eq(phoneNumber))
        .fetch();
  }

  @Override
  public Store findOrThrow(Long storeId) {
    return Optional.ofNullable(
            queryFactory
                .select(store)
                .from(store)
                .innerJoin(store.account, account).fetchJoin()
                .innerJoin(store.setting, setting).fetchJoin()
                .where(store.id.eq(storeId))
                .fetchFirst()
        )
        .orElseThrow(StoreNotFoundException::new);
  }

  @Override
  public Store findOrThrow(Long storeId, Long accountId) {
    return storeJpaRepository.findByIdAndAccountId(storeId, accountId)
        .orElseThrow(StoreNotFoundException::new);
  }

  @Override
  public Store findOrThrow(Long storeId, PhoneNumber phoneNumber) {
    return Optional.ofNullable(
            queryFactory
                .select(store)
                .from(store)
                .innerJoin(store.account, account).fetchJoin()
                .where(
                    store.id.eq(storeId),
                    store.account.phoneNumber.eq(phoneNumber)
                )
                .fetchFirst()
        )
        .orElseThrow(StoreNotFoundException::new);
  }

  @Override
  public Store save(Store store) {
    return storeJpaRepository.save(store);
  }

}

package com.everyonewaiter.infrastructure.store;

import static com.everyonewaiter.domain.store.entity.QStore.store;

import com.everyonewaiter.domain.store.entity.Store;
import com.everyonewaiter.domain.store.repository.StoreRepository;
import com.everyonewaiter.domain.store.view.StoreView;
import com.everyonewaiter.global.exception.BusinessException;
import com.everyonewaiter.global.exception.ErrorCode;
import com.querydsl.core.types.Projections;
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
  public boolean existsById(Long storeId) {
    Integer existsStore = queryFactory
        .selectOne()
        .from(store)
        .where(store.id.eq(storeId))
        .fetchFirst();
    return existsStore != null;
  }

  @Override
  public boolean existsByIdAndAccountId(Long storeId, Long accountId) {
    return storeJpaRepository.existsByIdAndAccountId(storeId, accountId);
  }

  @Override
  public List<StoreView.Simple> findAllSimpleViewByAccountId(Long accountId) {
    return queryFactory
        .select(
            Projections.constructor(
                StoreView.Simple.class,
                store.id,
                store.businessLicense.name
            )
        )
        .from(store)
        .where(store.accountId.eq(accountId))
        .fetch();
  }

  @Override
  public StoreView.SimpleWithStatus findSimpleWithStatusViewById(Long storeId) {
    return Optional.ofNullable(
            queryFactory
                .select(
                    Projections.constructor(
                        StoreView.SimpleWithStatus.class,
                        store.id,
                        store.businessLicense.name,
                        store.status
                    )
                )
                .from(store)
                .where(store.id.eq(storeId))
                .fetchFirst()
        )
        .orElseThrow(() -> new BusinessException(ErrorCode.STORE_NOT_FOUND));
  }

  @Override
  public Optional<Store> findByIdAndAccountId(Long storeId, Long accountId) {
    return storeJpaRepository.findByIdAndAccountId(storeId, accountId);
  }

  @Override
  public Store findByIdOrThrow(Long storeId) {
    return storeJpaRepository.findById(storeId)
        .orElseThrow(() -> new BusinessException(ErrorCode.STORE_NOT_FOUND));
  }

  @Override
  public Store findByIdAndAccountIdOrThrow(Long storeId, Long accountId) {
    return findByIdAndAccountId(storeId, accountId)
        .orElseThrow(() -> new BusinessException(ErrorCode.STORE_NOT_FOUND));
  }

  @Override
  public Store save(Store store) {
    return storeJpaRepository.save(store);
  }

}

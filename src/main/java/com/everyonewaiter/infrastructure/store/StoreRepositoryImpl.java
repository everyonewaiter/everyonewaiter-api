package com.everyonewaiter.infrastructure.store;

import static com.everyonewaiter.domain.store.entity.QSetting.setting;
import static com.everyonewaiter.domain.store.entity.QStore.store;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;
import com.everyonewaiter.domain.store.entity.Store;
import com.everyonewaiter.domain.store.repository.StoreRepository;
import com.everyonewaiter.domain.store.view.StoreView;
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
  public Store findByIdOrThrow(Long storeId) {
    return Optional.ofNullable(
            queryFactory
                .select(store)
                .from(store)
                .innerJoin(store.setting, setting).fetchJoin()
                .where(store.id.eq(storeId))
                .fetchFirst()
        )
        .orElseThrow(() -> new BusinessException(ErrorCode.STORE_NOT_FOUND));
  }

  @Override
  public Store findByIdAndAccountIdOrThrow(Long storeId, Long accountId) {
    return storeJpaRepository.findByIdAndAccountId(storeId, accountId)
        .orElseThrow(() -> new BusinessException(ErrorCode.STORE_NOT_FOUND));
  }

  @Override
  public Store save(Store store) {
    return storeJpaRepository.save(store);
  }

}

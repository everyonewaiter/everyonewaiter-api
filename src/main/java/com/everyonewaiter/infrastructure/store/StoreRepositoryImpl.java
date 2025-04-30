package com.everyonewaiter.infrastructure.store;

import static com.everyonewaiter.domain.store.entity.QStore.store;

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
  public Optional<Store> findByIdAndAccountId(Long storeId, Long accountId) {
    return storeJpaRepository.findByIdAndAccountId(storeId, accountId);
  }

  @Override
  public Store save(Store store) {
    return storeJpaRepository.save(store);
  }

}

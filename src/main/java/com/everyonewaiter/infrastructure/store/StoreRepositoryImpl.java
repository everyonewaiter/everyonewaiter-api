package com.everyonewaiter.infrastructure.store;

import static com.everyonewaiter.domain.store.entity.QStore.store;

import com.everyonewaiter.domain.store.entity.Store;
import com.everyonewaiter.domain.store.repository.StoreRepository;
import com.everyonewaiter.domain.store.view.StoreSimpleView;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class StoreRepositoryImpl implements StoreRepository {

  private final JPAQueryFactory queryFactory;
  private final StoreJpaRepository storeJpaRepository;

  @Override
  public List<StoreSimpleView> findAllSimpleViewByAccountId(Long accountId) {
    return queryFactory
        .select(
            Projections.constructor(
                StoreSimpleView.class,
                store.id,
                store.businessLicense.name
            )
        )
        .from(store)
        .where(store.accountId.eq(accountId))
        .fetch();
  }

  @Override
  public Store save(Store store) {
    return storeJpaRepository.save(store);
  }

}

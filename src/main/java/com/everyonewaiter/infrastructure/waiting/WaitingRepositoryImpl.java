package com.everyonewaiter.infrastructure.waiting;

import static com.everyonewaiter.domain.store.entity.QStore.store;
import static com.everyonewaiter.domain.waiting.entity.QWaiting.waiting;

import com.everyonewaiter.domain.waiting.entity.Waiting;
import com.everyonewaiter.domain.waiting.repository.WaitingRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class WaitingRepositoryImpl implements WaitingRepository {

  private final JPAQueryFactory queryFactory;
  private final WaitingJpaRepository waitingJpaRepository;

  @Override
  public int countByStoreId(Long storeId) {
    Long waitingCount = queryFactory
        .select(waiting.count())
        .from(waiting)
        .innerJoin(waiting.store, store)
        .where(
            waiting.store.id.eq(storeId),
            waiting.createdAt.gt(store.lastOpenedAt)
        )
        .fetchFirst();
    return Math.toIntExact(Objects.requireNonNullElse(waitingCount, 0L));
  }

  @Override
  public int countByStoreIdAndState(Long storeId, Waiting.State state) {
    Long waitingCount = queryFactory
        .select(waiting.count())
        .from(waiting)
        .innerJoin(waiting.store, store)
        .where(
            waiting.store.id.eq(storeId),
            waiting.state.eq(state),
            waiting.createdAt.gt(store.lastOpenedAt)
        )
        .fetchFirst();
    return Math.toIntExact(Objects.requireNonNullElse(waitingCount, 0L));
  }

  @Override
  public boolean existsByPhoneNumberAndState(String phoneNumber, Waiting.State state) {
    return waitingJpaRepository.existsByPhoneNumberAndState(phoneNumber, state);
  }

  @Override
  public List<Waiting> findAllByStoreIdAndState(
      Long storeId,
      Waiting.State state
  ) {
    return queryFactory
        .select(waiting)
        .from(waiting)
        .innerJoin(waiting.store, store)
        .where(
            waiting.store.id.eq(storeId),
            waiting.state.eq(state),
            waiting.createdAt.gt(store.lastOpenedAt)
        )
        .fetch();
  }

  @Override
  public Waiting save(Waiting waiting) {
    return waitingJpaRepository.save(waiting);
  }

}

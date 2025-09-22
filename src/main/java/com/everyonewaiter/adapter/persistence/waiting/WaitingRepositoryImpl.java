package com.everyonewaiter.adapter.persistence.waiting;

import static com.everyonewaiter.domain.store.QStore.store;
import static com.everyonewaiter.domain.waiting.QWaiting.waiting;
import static java.util.Objects.requireNonNullElse;

import com.everyonewaiter.application.waiting.required.WaitingRepository;
import com.everyonewaiter.domain.shared.PhoneNumber;
import com.everyonewaiter.domain.waiting.Waiting;
import com.everyonewaiter.domain.waiting.WaitingNotFoundException;
import com.everyonewaiter.domain.waiting.WaitingState;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class WaitingRepositoryImpl implements WaitingRepository {

  private final JPAQueryFactory queryFactory;
  private final WaitingJpaRepository waitingJpaRepository;

  @Override
  public int findLastNumber(Long storeId) {
    Long waitingCount = queryFactory
        .select(waiting.count())
        .from(waiting)
        .innerJoin(waiting.store, store)
        .where(
            waiting.store.id.eq(storeId),
            waiting.createdAt.gt(store.lastOpenedAt)
        )
        .fetchFirst();

    return Math.toIntExact(requireNonNullElse(waitingCount, 0L));
  }

  @Override
  public int count(Long storeId, WaitingState state) {
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

    return Math.toIntExact(requireNonNullElse(waitingCount, 0L));
  }

  @Override
  public int countLessThanId(Long waitingId, Long storeId, WaitingState state) {
    Long waitingCount = queryFactory
        .select(waiting.count())
        .from(waiting)
        .innerJoin(waiting.store, store)
        .where(
            waiting.id.lt(waitingId),
            waiting.store.id.eq(storeId),
            waiting.state.eq(state),
            waiting.createdAt.gt(store.lastOpenedAt)
        )
        .fetchFirst();

    return Math.toIntExact(requireNonNullElse(waitingCount, 0L));
  }

  @Override
  public boolean exists(PhoneNumber phoneNumber, WaitingState state) {
    return waitingJpaRepository.existsByPhoneNumberAndState(phoneNumber, state);
  }

  @Override
  public boolean exists(Long storeId, WaitingState state) {
    return waitingJpaRepository.existsByStoreIdAndState(storeId, state);
  }

  @Override
  public List<Waiting> findAll(Long storeId, WaitingState state) {
    return queryFactory
        .select(waiting)
        .from(waiting)
        .innerJoin(waiting.store, store).fetchJoin()
        .where(
            store.id.eq(storeId),
            waiting.state.eq(state),
            waiting.createdAt.gt(store.lastOpenedAt)
        )
        .orderBy(waiting.id.asc())
        .fetch();
  }

  @Override
  public Waiting findOrThrow(Long waitingId, Long storeId) {
    return Optional.ofNullable(
            queryFactory
                .select(waiting)
                .from(waiting)
                .innerJoin(waiting.store, store).fetchJoin()
                .where(
                    waiting.id.eq(waitingId),
                    store.id.eq(storeId)
                )
                .fetchFirst()
        )
        .orElseThrow(WaitingNotFoundException::new);
  }

  @Override
  public Waiting findOrThrow(Long storeId, String accessKey) {
    return Optional.ofNullable(
            queryFactory
                .select(waiting)
                .from(waiting)
                .innerJoin(waiting.store, store).fetchJoin()
                .where(
                    store.id.eq(storeId),
                    waiting.accessKey.eq(accessKey)
                )
                .fetchFirst()
        )
        .orElseThrow(WaitingNotFoundException::new);
  }

  @Override
  public Waiting save(Waiting waiting) {
    return waitingJpaRepository.save(waiting);
  }

}

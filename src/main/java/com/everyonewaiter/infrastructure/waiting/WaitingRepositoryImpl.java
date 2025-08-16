package com.everyonewaiter.infrastructure.waiting;

import static com.everyonewaiter.domain.store.QStore.store;
import static com.everyonewaiter.domain.waiting.entity.QWaiting.waiting;

import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.ErrorCode;
import com.everyonewaiter.domain.waiting.entity.Waiting;
import com.everyonewaiter.domain.waiting.repository.WaitingRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
  public int countByIdAndStoreIdAndState(Long waitingId, Long storeId, Waiting.State state) {
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
    return Math.toIntExact(Objects.requireNonNullElse(waitingCount, 0L));
  }

  @Override
  public boolean existsByPhoneNumberAndState(String phoneNumber, Waiting.State state) {
    return waitingJpaRepository.existsByPhoneNumberAndState(phoneNumber, state);
  }

  @Override
  public boolean existsByStoreIdAndState(Long storeId, Waiting.State state) {
    return waitingJpaRepository.existsByStoreIdAndState(storeId, state);
  }

  @Override
  public List<Waiting> findAllByStoreIdAndState(Long storeId, Waiting.State state) {
    return queryFactory
        .select(waiting)
        .from(waiting)
        .innerJoin(waiting.store, store).fetchJoin()
        .where(
            store.id.eq(storeId),
            waiting.state.eq(state),
            waiting.createdAt.gt(store.lastOpenedAt)
        )
        .fetch();
  }

  @Override
  public Waiting findByIdAndStoreIdOrThrow(Long waitingId, Long storeId) {
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
        .orElseThrow(() -> new BusinessException(ErrorCode.WAITING_NOT_FOUND));
  }

  @Override
  public Waiting findByStoreIdAndAccessKey(Long storeId, String accessKey) {
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
        .orElseThrow(() -> new BusinessException(ErrorCode.WAITING_NOT_FOUND));
  }

  @Override
  public Waiting save(Waiting waiting) {
    return waitingJpaRepository.save(waiting);
  }

}

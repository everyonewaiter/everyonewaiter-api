package com.everyonewaiter.infrastructure.pos;

import static com.everyonewaiter.domain.pos.entity.QPosTable.posTable;
import static com.everyonewaiter.domain.pos.entity.QPosTableActivity.posTableActivity;

import com.everyonewaiter.domain.pos.entity.PosTableActivity;
import com.everyonewaiter.domain.pos.repository.PosTableActivityRepository;
import com.everyonewaiter.global.exception.BusinessException;
import com.everyonewaiter.global.exception.ErrorCode;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class PosTableActivityRepositoryImpl implements PosTableActivityRepository {

  private final JPAQueryFactory queryFactory;
  private final PosTableActivityJpaRepository posTableActivityJpaRepository;

  @Override
  public Optional<PosTableActivity> findByStoreIdAndTableNo(Long storeId, int tableNo) {
    return Optional.ofNullable(
        queryFactory
            .select(posTableActivity)
            .from(posTableActivity)
            .innerJoin(posTableActivity.posTable, posTable).fetchJoin()
            .where(
                posTableActivity.store.id.eq(storeId),
                posTableActivity.active.isTrue(),
                posTableActivity.posTable.tableNo.eq(tableNo)
            )
            .fetchFirst()
    );
  }

  @Override
  public PosTableActivity findByStoreIdAndTableNoOrThrow(Long storeId, int tableNo) {
    return findByStoreIdAndTableNo(storeId, tableNo)
        .orElseThrow(() -> new BusinessException(ErrorCode.POS_TABLE_ACTIVE_ACTIVITY_NOT_FOUND));
  }

  @Override
  public PosTableActivity save(PosTableActivity posTableActivity) {
    return posTableActivityJpaRepository.save(posTableActivity);
  }

}

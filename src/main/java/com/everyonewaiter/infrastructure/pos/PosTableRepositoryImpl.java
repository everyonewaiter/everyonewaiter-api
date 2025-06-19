package com.everyonewaiter.infrastructure.pos;

import static com.everyonewaiter.domain.pos.entity.QPosTable.posTable;

import com.everyonewaiter.domain.pos.entity.PosTable;
import com.everyonewaiter.domain.pos.repository.PosTableRepository;
import com.everyonewaiter.global.exception.BusinessException;
import com.everyonewaiter.global.exception.ErrorCode;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class PosTableRepositoryImpl implements PosTableRepository {

  private final JPAQueryFactory queryFactory;
  private final PosTableJpaRepository posTableJpaRepository;

  @Override
  public void close(Long storeId) {
    queryFactory
        .update(posTable)
        .set(posTable.active, false)
        .where(
            posTable.store.id.eq(storeId),
            posTable.active.isTrue()
        )
        .execute();
  }

  @Override
  public PosTable save(PosTable posTable) {
    return posTableJpaRepository.save(posTable);
  }

  @Override
  public void saveAll(List<PosTable> posTables) {
    posTableJpaRepository.saveAll(posTables);
  }

  @Override
  public PosTable findActiveByStoreIdAndTableNo(Long storeId, int tableNo) {
    return posTableJpaRepository.findByStoreIdAndTableNoAndActive(storeId, tableNo, true)
        .orElseThrow(() -> new BusinessException(ErrorCode.POS_TABLE_NOT_FOUND));
  }

  @Override
  public List<PosTable> findAllActiveByStoreId(Long storeId) {
    return posTableJpaRepository.findAllByStoreIdAndActive(storeId, true);
  }

}

package com.everyonewaiter.infrastructure.pos;

import static com.everyonewaiter.domain.pos.QPosTable.posTable;

import com.everyonewaiter.domain.pos.PosTable;
import com.everyonewaiter.domain.pos.PosTableNotFoundException;
import com.everyonewaiter.domain.pos.repository.PosTableRepository;
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
  public List<PosTable> findAllActive(Long storeId) {
    return posTableJpaRepository.findAllByStoreIdAndActive(storeId, true);
  }

  @Override
  public PosTable findActiveOrThrow(Long storeId, int tableNo) {
    return posTableJpaRepository.findByStoreIdAndTableNoAndActive(storeId, tableNo, true)
        .orElseThrow(PosTableNotFoundException::new);
  }

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

}

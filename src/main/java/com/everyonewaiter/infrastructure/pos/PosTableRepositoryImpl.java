package com.everyonewaiter.infrastructure.pos;

import static com.everyonewaiter.domain.pos.entity.QPosTable.posTable;

import com.everyonewaiter.domain.pos.entity.PosTable;
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
  public void close(Long storeId) {
    queryFactory
        .update(posTable)
        .set(posTable.state, PosTable.State.INACTIVE)
        .where(
            posTable.storeId.eq(storeId),
            posTable.state.eq(PosTable.State.ACTIVE)
        )
        .execute();
  }

  @Override
  public void saveAll(List<PosTable> tables) {
    posTableJpaRepository.saveAll(tables);
  }

  @Override
  public List<PosTable> findAllActiveByStoreId(Long storeId) {
    return posTableJpaRepository.findAllByStoreIdAndState(storeId, PosTable.State.ACTIVE);
  }

}

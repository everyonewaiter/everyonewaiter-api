package com.everyonewaiter.infrastructure.pos;

import com.everyonewaiter.domain.pos.entity.PosTableActivity;
import com.everyonewaiter.domain.pos.repository.PosTableActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class PosTableActivityRepositoryImpl implements PosTableActivityRepository {

  private final PosTableActivityJpaRepository posTableActivityJpaRepository;

  @Override
  public PosTableActivity save(PosTableActivity posTableActivity) {
    return posTableActivityJpaRepository.save(posTableActivity);
  }

}

package com.everyonewaiter.infrastructure.order;

import com.everyonewaiter.domain.order.entity.StaffCall;
import com.everyonewaiter.domain.order.repository.StaffCallRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class StaffCallRepositoryImpl implements StaffCallRepository {

  private final StaffCallJpaRepository staffCallJpaRepository;

  @Override
  public StaffCall save(StaffCall staffCall) {
    return staffCallJpaRepository.save(staffCall);
  }

}

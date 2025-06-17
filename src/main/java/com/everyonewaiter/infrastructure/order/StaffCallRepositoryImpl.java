package com.everyonewaiter.infrastructure.order;

import com.everyonewaiter.domain.order.entity.StaffCall;
import com.everyonewaiter.domain.order.repository.StaffCallRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class StaffCallRepositoryImpl implements StaffCallRepository {

  private final StaffCallJpaRepository staffCallJpaRepository;

  @Override
  public boolean existsIncompleteByStoreId(Long storeId) {
    return staffCallJpaRepository.existsByStoreIdAndState(storeId, StaffCall.State.INCOMPLETE);
  }

  @Override
  public List<StaffCall> findAllIncompleteByStoreId(Long storeId) {
    return staffCallJpaRepository.findAllByStoreIdAndState(storeId, StaffCall.State.INCOMPLETE);
  }

  @Override
  public StaffCall save(StaffCall staffCall) {
    return staffCallJpaRepository.save(staffCall);
  }

}

package com.everyonewaiter.application.pos;

import com.everyonewaiter.application.pos.provided.PosTableOrderManager;
import com.everyonewaiter.application.pos.required.PosTableRepository;
import com.everyonewaiter.application.support.DistributedLock;
import com.everyonewaiter.domain.order.OrderMemoUpdateRequest;
import com.everyonewaiter.domain.order.OrderUpdateRequests;
import com.everyonewaiter.domain.pos.PosTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
@Transactional
@RequiredArgsConstructor
class PosTableOrderManagementService implements PosTableOrderManager {

  private final PosTableRepository posTableRepository;

  @Override
  @DistributedLock(key = "#storeId + '-' + #tableNo")
  public PosTable cancel(Long storeId, int tableNo, Long orderId) {
    PosTable posTable = posTableRepository.findActiveOrThrow(storeId, tableNo);

    posTable.cancelOrder(orderId);

    return posTableRepository.save(posTable);
  }

  @Override
  @DistributedLock(key = "#storeId + '-' + #tableNo")
  public PosTable update(
      Long storeId,
      int tableNo,
      Long orderId,
      OrderMemoUpdateRequest updateRequest
  ) {
    PosTable posTable = posTableRepository.findActiveOrThrow(storeId, tableNo);

    posTable.updateOrder(orderId, updateRequest);

    return posTableRepository.save(posTable);
  }

  @Override
  @DistributedLock(key = "#storeId + '-' + #tableNo")
  public PosTable update(Long storeId, int tableNo, OrderUpdateRequests updateRequests) {
    PosTable posTable = posTableRepository.findActiveOrThrow(storeId, tableNo);

    posTable.updateOrder(updateRequests);

    return posTableRepository.save(posTable);
  }

}

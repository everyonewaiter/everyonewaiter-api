package com.everyonewaiter.application.pos;

import com.everyonewaiter.application.pos.required.PosTableRepository;
import com.everyonewaiter.domain.order.entity.Receipt;
import com.everyonewaiter.domain.order.service.ReceiptFactory;
import com.everyonewaiter.domain.pos.PosTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PosService {

  private final ReceiptFactory receiptFactory;
  private final PosTableRepository posTableRepository;

  @Transactional(readOnly = true)
  public void resendReceipt(Long storeId, int tableNo) {
    PosTable posTable = posTableRepository.findActiveOrThrow(storeId, tableNo);

    Receipt receipt = receiptFactory.createReceipt(
        storeId,
        posTable.getPrintEnabledOrderedOrderMenus()
    );
    posTable.resendReceipt(receipt);

    posTableRepository.save(posTable);
  }

}

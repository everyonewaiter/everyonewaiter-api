package com.everyonewaiter.application.pos.provided;

import com.everyonewaiter.domain.pos.PosTable;
import com.everyonewaiter.domain.pos.PosTableDiscountRequest;
import jakarta.validation.Valid;
import java.util.Optional;

public interface PosTableManager {

  Optional<PosTable> moveTable(Long storeId, int sourceTableNo, int targetTableNo);

  PosTable discount(Long storeId, int tableNo, @Valid PosTableDiscountRequest discountRequest);

  PosTable completeActivity(Long storeId, int tableNo);

}

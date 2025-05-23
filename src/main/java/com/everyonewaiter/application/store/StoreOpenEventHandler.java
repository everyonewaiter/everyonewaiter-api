package com.everyonewaiter.application.store;

import com.everyonewaiter.domain.device.entity.Device;
import com.everyonewaiter.domain.device.repository.DeviceRepository;
import com.everyonewaiter.domain.pos.entity.PosTable;
import com.everyonewaiter.domain.pos.repository.PosTableRepository;
import com.everyonewaiter.domain.store.event.StoreOpenEvent;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
class StoreOpenEventHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(StoreOpenEventHandler.class);

  private final DeviceRepository deviceRepository;
  private final PosTableRepository posTableRepository;

  @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
  public void consume(StoreOpenEvent event) {
    Long storeId = event.storeId();
    LOGGER.info("[매장 영업 시작 이벤트] storeId: {}", storeId);

    Map<Integer, PosTable> tables = new LinkedHashMap<>();
    List<Device> devices = deviceRepository.findAllActiveByPurpose(storeId, Device.Purpose.TABLE);
    for (Device device : devices) {
      tables.computeIfAbsent(
          device.getTableNo(),
          tableNo -> PosTable.create(storeId, "T", tableNo)
      );
    }

    for (int i = 1; i <= event.extraTableCount(); i++) {
      PosTable table = PosTable.create(storeId, "추가", String.valueOf(i), 10000 + i);
      tables.put(table.getTableNo(), table);
    }

    if (!tables.isEmpty()) {
      posTableRepository.saveAll(tables.values().stream().toList());
    }
  }

}

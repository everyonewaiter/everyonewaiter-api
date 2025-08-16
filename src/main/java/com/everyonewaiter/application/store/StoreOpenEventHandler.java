package com.everyonewaiter.application.store;

import static com.everyonewaiter.domain.device.DevicePurpose.TABLE;
import static com.everyonewaiter.domain.device.DeviceState.ACTIVE;

import com.everyonewaiter.application.device.provided.DeviceFinder;
import com.everyonewaiter.domain.device.Device;
import com.everyonewaiter.domain.pos.entity.PosTable;
import com.everyonewaiter.domain.pos.repository.PosTableRepository;
import com.everyonewaiter.domain.store.entity.Store;
import com.everyonewaiter.domain.store.event.StoreOpenEvent;
import com.everyonewaiter.domain.store.repository.StoreRepository;
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

  private final StoreRepository storeRepository;
  private final DeviceFinder deviceFinder;
  private final PosTableRepository posTableRepository;

  @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
  public void consume(StoreOpenEvent event) {
    Long storeId = event.storeId();
    Store store = storeRepository.findByIdOrThrow(storeId);
    LOGGER.info("[매장 영업 시작 이벤트] storeId: {}", storeId);

    Map<Integer, PosTable> tables = new LinkedHashMap<>();
    List<Device> devices = deviceFinder.findAll(storeId, TABLE, ACTIVE);
    for (Device device : devices) {
      tables.computeIfAbsent(
          device.getTableNo(),
          tableNo -> PosTable.create(store, "T", tableNo)
      );
    }

    for (int i = 1; i <= store.getExtraTableCount(); i++) {
      PosTable table = PosTable.create(store, "추가", String.valueOf(i), 10000 + i);
      tables.put(table.getTableNo(), table);
    }

    if (!tables.isEmpty()) {
      posTableRepository.saveAll(tables.values().stream().toList());
    }
  }

}

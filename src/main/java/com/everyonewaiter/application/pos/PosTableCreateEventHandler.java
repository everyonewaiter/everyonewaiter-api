package com.everyonewaiter.application.pos;

import static com.everyonewaiter.domain.device.DevicePurpose.TABLE;
import static com.everyonewaiter.domain.device.DeviceState.ACTIVE;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.transaction.event.TransactionPhase.BEFORE_COMMIT;

import com.everyonewaiter.application.device.provided.DeviceFinder;
import com.everyonewaiter.application.pos.required.PosTableRepository;
import com.everyonewaiter.application.store.provided.StoreFinder;
import com.everyonewaiter.domain.device.Device;
import com.everyonewaiter.domain.pos.PosTable;
import com.everyonewaiter.domain.store.Store;
import com.everyonewaiter.domain.store.StoreOpenEvent;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
class PosTableCreateEventHandler {

  private static final Logger LOGGER = getLogger(PosTableCreateEventHandler.class);

  private final StoreFinder storeFinder;
  private final DeviceFinder deviceFinder;
  private final PosTableRepository posTableRepository;

  @TransactionalEventListener(phase = BEFORE_COMMIT)
  public void handle(StoreOpenEvent event) {
    Long storeId = event.storeId();

    LOGGER.info("[매장 영업 시작 이벤트] storeId: {}", storeId);

    Store store = storeFinder.findOrThrow(storeId);
    List<Device> devices = deviceFinder.findAll(storeId, TABLE, ACTIVE);

    Map<Integer, PosTable> tables = new LinkedHashMap<>();
    for (Device device : devices) {
      int tableNo = device.getTableNo();

      tables.computeIfAbsent(tableNo, tn -> PosTable.create(store, tn));
    }

    for (int i = 1; i <= store.getSetting().getExtraTableCount(); i++) {
      PosTable table = PosTable.create(store, 10000 + i);

      tables.put(table.getTableNo(), table);
    }

    if (tables.isEmpty()) {
      return;
    }

    posTableRepository.saveAll(tables.values().stream().toList());
  }

}

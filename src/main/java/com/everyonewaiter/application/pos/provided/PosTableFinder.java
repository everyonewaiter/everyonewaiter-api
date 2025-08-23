package com.everyonewaiter.application.pos.provided;

import com.everyonewaiter.domain.pos.PosView;
import java.util.List;
import java.util.Optional;

public interface PosTableFinder {

  List<PosView.PosTableDetail> findAllActive(Long storeId);

  Optional<PosView.PosTableActivityDetail> findActiveActivity(Long storeId, int tableNo);

}

package com.everyonewaiter.application.pos.provided;

import com.everyonewaiter.domain.pos.PosTableActivity;

public interface PosTableActivityFinder {

  PosTableActivity findActiveOrCreate(Long storeId, int tableNo);

}

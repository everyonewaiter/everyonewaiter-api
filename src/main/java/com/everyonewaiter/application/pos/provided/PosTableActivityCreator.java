package com.everyonewaiter.application.pos.provided;

import com.everyonewaiter.domain.pos.PosTableActivity;

public interface PosTableActivityCreator {

  PosTableActivity create(Long storeId, int tableNo);

}

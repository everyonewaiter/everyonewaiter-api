package com.everyonewaiter.application.waiting.provided;

import com.everyonewaiter.domain.waiting.Waiting;
import com.everyonewaiter.domain.waiting.WaitingMyTurnView;
import com.everyonewaiter.domain.waiting.WaitingRegisterRequest;
import jakarta.validation.Valid;

public interface WaitingCustomer {

  Waiting register(Long storeId, @Valid WaitingRegisterRequest registerRequest);

  Waiting cancel(Long storeId, String accessKey);

  WaitingMyTurnView getMyTurn(Long storeId, String accessKey);

}

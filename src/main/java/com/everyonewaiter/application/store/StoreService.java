package com.everyonewaiter.application.store;

import com.everyonewaiter.application.store.response.StoreResponse;
import com.everyonewaiter.domain.store.repository.StoreRepository;
import com.everyonewaiter.domain.store.view.StoreView;
import com.everyonewaiter.global.exception.BusinessException;
import com.everyonewaiter.global.exception.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreService {

  private final StoreRepository storeRepository;

  public StoreResponse.Simples readAllSimpleView(Long accountId) {
    List<StoreView.Simple> views = storeRepository.findAllSimpleViewByAccountId(accountId);
    return StoreResponse.Simples.from(views);
  }

  public StoreResponse.Detail read(Long storeId, Long accountId) {
    return storeRepository.findByIdAndAccountId(storeId, accountId)
        .map(StoreResponse.Detail::from)
        .orElseThrow(() -> new BusinessException(ErrorCode.STORE_NOT_FOUND));
  }

}

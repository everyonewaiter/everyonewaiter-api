package com.everyonewaiter.application.store;

import com.everyonewaiter.application.store.response.StoreResponse;
import com.everyonewaiter.domain.store.repository.StoreRepository;
import com.everyonewaiter.domain.store.view.StoreSimpleView;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreService {

  private final StoreRepository storeRepository;

  public StoreResponse.Simples readAllSimpleView(Long accountId) {
    List<StoreSimpleView> views = storeRepository.findAllSimpleViewByAccountId(accountId);
    return StoreResponse.Simples.from(views);
  }

}

package com.everyonewaiter.application.store;

import com.everyonewaiter.application.store.request.StoreWrite;
import com.everyonewaiter.application.store.response.StoreResponse;
import com.everyonewaiter.domain.store.entity.CountryOfOrigin;
import com.everyonewaiter.domain.store.entity.StaffCallOption;
import com.everyonewaiter.domain.store.entity.Store;
import com.everyonewaiter.domain.store.repository.StoreRepository;
import com.everyonewaiter.domain.store.service.StoreValidator;
import com.everyonewaiter.domain.store.view.StoreView;
import com.everyonewaiter.global.exception.BusinessException;
import com.everyonewaiter.global.exception.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreService {

  private final StoreValidator storeValidator;
  private final StoreRepository storeRepository;

  public void checkExistsStore(Long storeId) {
    storeValidator.validateExists(storeId);
  }

  public void checkStoreOwner(Long storeId, Long accountId) {
    storeValidator.validateOwner(storeId, accountId);
  }

  public StoreResponse.Simples readAllSimpleView(Long accountId) {
    List<StoreView.Simple> views = storeRepository.findAllSimpleViewByAccountId(accountId);
    return StoreResponse.Simples.from(views);
  }

  public StoreResponse.Detail read(Long storeId, Long accountId) {
    return storeRepository.findByIdAndAccountId(storeId, accountId)
        .map(StoreResponse.Detail::from)
        .orElseThrow(() -> new BusinessException(ErrorCode.STORE_NOT_FOUND));
  }

  @Transactional
  public void update(Long storeId, Long accountId, StoreWrite.Update request) {
    Store store = storeRepository.findByIdAndAccountIdOrThrow(storeId, accountId);
    store.update(
        request.landline(),
        request.setting().extraTableCount(),
        request.setting().printerLocation(),
        request.setting().showMenuPopup(),
        request.setting().showOrderTotalPrice(),
        request.setting().countryOfOrigins()
            .stream()
            .map(countryOfOrigin ->
                new CountryOfOrigin(countryOfOrigin.item(), countryOfOrigin.origin())
            )
            .toList(),
        request.setting().staffCallOptions()
            .stream()
            .map(staffCallOption -> new StaffCallOption(staffCallOption.optionName()))
            .toList()
    );
    storeRepository.save(store);
  }

}

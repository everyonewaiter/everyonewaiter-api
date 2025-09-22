package com.everyonewaiter.adapter.web.api.owner;

import com.everyonewaiter.adapter.web.api.dto.StoreDetailResponse;
import com.everyonewaiter.adapter.web.api.dto.StoreSimpleResponses;
import com.everyonewaiter.application.store.provided.StoreFinder;
import com.everyonewaiter.application.store.provided.StoreManager;
import com.everyonewaiter.domain.account.Account;
import com.everyonewaiter.domain.auth.AuthenticationAccount;
import com.everyonewaiter.domain.store.Store;
import com.everyonewaiter.domain.store.StoreUpdateRequest;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/stores")
class StoreManagementApi implements StoreManagementApiSpecification {

  private final StoreFinder storeFinder;
  private final StoreManager storeManager;

  @Override
  @GetMapping
  public ResponseEntity<StoreSimpleResponses> getStores(
      @AuthenticationAccount Account account) {
    List<Store> stores = storeFinder.findAll(account.getId());

    return ResponseEntity.ok(StoreSimpleResponses.from(stores));
  }

  @Override
  @GetMapping("/accounts/{accountId}")
  public ResponseEntity<StoreSimpleResponses> getStores(@PathVariable Long accountId) {
    List<Store> stores = storeFinder.findAll(accountId);

    return ResponseEntity.ok(StoreSimpleResponses.from(stores));
  }

  @Override
  @GetMapping("/{storeId}")
  public ResponseEntity<StoreDetailResponse> getStore(@PathVariable Long storeId) {
    Store store = storeFinder.findOrThrow(storeId);

    return ResponseEntity.ok(StoreDetailResponse.from(store));
  }

  @Override
  @PutMapping("/{storeId}")
  public ResponseEntity<Void> update(
      @PathVariable Long storeId,
      @RequestBody @Valid StoreUpdateRequest updateRequest,
      @AuthenticationAccount Account account
  ) {
    storeManager.update(storeId, account.getId(), updateRequest);

    return ResponseEntity.noContent().build();
  }

}

package com.everyonewaiter.adapter.web.api.owner;

import com.everyonewaiter.adapter.web.api.owner.request.StoreWriteRequest;
import com.everyonewaiter.application.store.StoreService;
import com.everyonewaiter.application.store.response.StoreResponse;
import com.everyonewaiter.domain.account.Account;
import com.everyonewaiter.domain.auth.AuthenticationAccount;
import jakarta.validation.Valid;
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
class StoreManagementController implements StoreManagementControllerSpecification {

  private final StoreService storeService;

  @Override
  @GetMapping
  public ResponseEntity<StoreResponse.Simples> getStores(@AuthenticationAccount Account account) {
    return ResponseEntity.ok(storeService.readAllSimpleView(account.getId()));
  }

  @Override
  @GetMapping("/accounts/{accountId}")
  public ResponseEntity<StoreResponse.Simples> getStores(@PathVariable Long accountId) {
    return ResponseEntity.ok(storeService.readAllSimpleView(accountId));
  }

  @Override
  @GetMapping("/{storeId}")
  public ResponseEntity<StoreResponse.Detail> getStore(@PathVariable Long storeId) {
    StoreResponse.Detail response = storeService.read(storeId);
    return ResponseEntity.ok(response);
  }

  @Override
  @PutMapping("/{storeId}")
  public ResponseEntity<Void> update(
      @PathVariable Long storeId,
      @RequestBody @Valid StoreWriteRequest.Update request,
      @AuthenticationAccount Account account
  ) {
    storeService.update(storeId, account.getId(), request.toDomainDto());
    return ResponseEntity.noContent().build();
  }

}

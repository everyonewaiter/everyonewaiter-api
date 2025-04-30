package com.everyonewaiter.presentation.owner;

import com.everyonewaiter.application.store.StoreService;
import com.everyonewaiter.application.store.response.StoreResponse;
import com.everyonewaiter.domain.account.entity.Account;
import com.everyonewaiter.global.annotation.AuthenticationAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stores")
class StoreController implements StoreControllerSpecification {

  private final StoreService storeService;

  @Override
  @GetMapping
  public ResponseEntity<StoreResponse.Simples> getStores(@AuthenticationAccount Account account) {
    return ResponseEntity.ok(storeService.readAllSimpleView(account.getId()));
  }

}

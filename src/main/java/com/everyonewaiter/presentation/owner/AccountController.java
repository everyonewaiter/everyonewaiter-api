package com.everyonewaiter.presentation.owner;

import com.everyonewaiter.application.account.service.AccountService;
import com.everyonewaiter.presentation.owner.request.AccountWrite;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
class AccountController implements AccountControllerSpecification {

  private final AccountService accountService;

  @Override
  @PostMapping
  public ResponseEntity<Void> signUp(@RequestBody @Valid AccountWrite.CreateRequest request) {
    Long accountId = accountService.create(request.toAccountCreate());
    return ResponseEntity.created(URI.create(accountId.toString())).build();
  }

}

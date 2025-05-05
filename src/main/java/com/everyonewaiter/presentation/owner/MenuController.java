package com.everyonewaiter.presentation.owner;

import com.everyonewaiter.application.menu.CategoryService;
import com.everyonewaiter.domain.account.entity.Account;
import com.everyonewaiter.global.annotation.AuthenticationAccount;
import com.everyonewaiter.global.annotation.StoreOwner;
import com.everyonewaiter.presentation.owner.request.CategoryWriteRequest;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
class MenuController implements MenuControllerSpecification {

  private final CategoryService categoryService;

  @Override
  @StoreOwner
  @PostMapping("/stores/{storeId}/categories")
  public ResponseEntity<Void> createCategory(
      @PathVariable Long storeId,
      @RequestBody @Valid CategoryWriteRequest.Create request,
      @AuthenticationAccount(permission = Account.Permission.OWNER) Account account
  ) {
    Long categoryId = categoryService.create(storeId, request.toDomainDto());
    return ResponseEntity.created(URI.create(categoryId.toString())).build();
  }

}

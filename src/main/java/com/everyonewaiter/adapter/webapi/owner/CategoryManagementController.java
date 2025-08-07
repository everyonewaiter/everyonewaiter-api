package com.everyonewaiter.adapter.webapi.owner;

import com.everyonewaiter.adapter.webapi.owner.request.CategoryWriteRequest;
import com.everyonewaiter.application.menu.CategoryService;
import com.everyonewaiter.application.menu.response.CategoryResponse;
import com.everyonewaiter.domain.account.entity.Account;
import com.everyonewaiter.global.annotation.AuthenticationAccount;
import com.everyonewaiter.global.annotation.StoreOwner;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
class CategoryManagementController implements CategoryManagementControllerSpecification {

  private final CategoryService categoryService;

  @Override
  @StoreOwner
  @GetMapping("/stores/{storeId}/categories")
  public ResponseEntity<CategoryResponse.Simples> getCategories(
      @PathVariable Long storeId,
      @AuthenticationAccount(permission = Account.Permission.OWNER) Account account
  ) {
    return ResponseEntity.ok(categoryService.readAllSimples(storeId));
  }

  @Override
  @StoreOwner
  @PostMapping("/stores/{storeId}/categories")
  public ResponseEntity<Void> create(
      @PathVariable Long storeId,
      @RequestBody @Valid CategoryWriteRequest.Create request,
      @AuthenticationAccount(permission = Account.Permission.OWNER) Account account
  ) {
    Long categoryId = categoryService.create(storeId, request.toDomainDto());
    return ResponseEntity.created(URI.create(categoryId.toString())).build();
  }

  @Override
  @StoreOwner
  @PutMapping("/stores/{storeId}/categories/{categoryId}")
  public ResponseEntity<Void> update(
      @PathVariable Long storeId,
      @PathVariable Long categoryId,
      @RequestBody @Valid CategoryWriteRequest.Update request,
      @AuthenticationAccount(permission = Account.Permission.OWNER) Account account
  ) {
    categoryService.update(categoryId, storeId, request.toDomainDto());
    return ResponseEntity.noContent().build();
  }

  @Override
  @StoreOwner
  @PostMapping("/stores/{storeId}/categories/{sourceId}/move/{targetId}")
  public ResponseEntity<Void> movePosition(
      @PathVariable Long storeId,
      @PathVariable Long sourceId,
      @PathVariable Long targetId,
      @RequestBody @Valid CategoryWriteRequest.MovePosition request,
      @AuthenticationAccount(permission = Account.Permission.OWNER) Account account
  ) {
    categoryService.movePosition(sourceId, targetId, storeId, request.toDomainDto());
    return ResponseEntity.noContent().build();
  }

  @Override
  @StoreOwner
  @DeleteMapping("/stores/{storeId}/categories/{categoryId}")
  public ResponseEntity<Void> delete(
      @PathVariable Long storeId,
      @PathVariable Long categoryId,
      @AuthenticationAccount(permission = Account.Permission.OWNER) Account account
  ) {
    categoryService.delete(categoryId, storeId);
    return ResponseEntity.noContent().build();
  }

}

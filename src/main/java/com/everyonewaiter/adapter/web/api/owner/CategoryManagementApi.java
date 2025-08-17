package com.everyonewaiter.adapter.web.api.owner;

import com.everyonewaiter.adapter.web.api.dto.CategorySimpleResponses;
import com.everyonewaiter.application.menu.provided.CategoryFinder;
import com.everyonewaiter.application.menu.provided.CategoryManager;
import com.everyonewaiter.domain.account.Account;
import com.everyonewaiter.domain.account.AccountPermission;
import com.everyonewaiter.domain.auth.AuthenticationAccount;
import com.everyonewaiter.domain.menu.Category;
import com.everyonewaiter.domain.menu.CategoryCreateRequest;
import com.everyonewaiter.domain.menu.CategoryMovePositionRequest;
import com.everyonewaiter.domain.menu.CategoryUpdateRequest;
import com.everyonewaiter.domain.store.StoreOwner;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
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
class CategoryManagementApi implements CategoryManagementApiSpecification {

  private final CategoryFinder categoryFinder;
  private final CategoryManager categoryManager;

  @Override
  @StoreOwner
  @GetMapping("/stores/{storeId}/categories")
  public ResponseEntity<CategorySimpleResponses> getCategories(
      @PathVariable Long storeId,
      @AuthenticationAccount(permission = AccountPermission.OWNER) Account account
  ) {
    List<Category> categories = categoryFinder.findAll(storeId);

    return ResponseEntity.ok(CategorySimpleResponses.from(categories));
  }

  @Override
  @StoreOwner
  @PostMapping("/stores/{storeId}/categories")
  public ResponseEntity<Void> create(
      @PathVariable Long storeId,
      @RequestBody @Valid CategoryCreateRequest createRequest,
      @AuthenticationAccount(permission = AccountPermission.OWNER) Account account
  ) {
    Category category = categoryManager.create(storeId, createRequest);

    return ResponseEntity.created(URI.create(category.getNonNullId().toString())).build();
  }

  @Override
  @StoreOwner
  @PutMapping("/stores/{storeId}/categories/{categoryId}")
  public ResponseEntity<Void> update(
      @PathVariable Long storeId,
      @PathVariable Long categoryId,
      @RequestBody @Valid CategoryUpdateRequest updateRequest,
      @AuthenticationAccount(permission = AccountPermission.OWNER) Account account
  ) {
    categoryManager.update(categoryId, storeId, updateRequest);

    return ResponseEntity.noContent().build();
  }

  @Override
  @StoreOwner
  @PostMapping("/stores/{storeId}/categories/{sourceId}/move/{targetId}")
  public ResponseEntity<Void> movePosition(
      @PathVariable Long storeId,
      @PathVariable Long sourceId,
      @PathVariable Long targetId,
      @RequestBody @Valid CategoryMovePositionRequest movePositionRequest,
      @AuthenticationAccount(permission = AccountPermission.OWNER) Account account
  ) {
    categoryManager.movePosition(sourceId, targetId, storeId, movePositionRequest);

    return ResponseEntity.noContent().build();
  }

  @Override
  @StoreOwner
  @DeleteMapping("/stores/{storeId}/categories/{categoryId}")
  public ResponseEntity<Void> delete(
      @PathVariable Long storeId,
      @PathVariable Long categoryId,
      @AuthenticationAccount(permission = AccountPermission.OWNER) Account account
  ) {
    categoryManager.delete(categoryId, storeId);
    return ResponseEntity.noContent().build();
  }

}

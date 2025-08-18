package com.everyonewaiter.adapter.web.api.owner;

import com.everyonewaiter.adapter.web.api.dto.MenuSimpleResponses;
import com.everyonewaiter.application.menu.provided.MenuFinder;
import com.everyonewaiter.application.menu.provided.MenuManager;
import com.everyonewaiter.domain.account.Account;
import com.everyonewaiter.domain.account.AccountPermission;
import com.everyonewaiter.domain.auth.AuthenticationAccount;
import com.everyonewaiter.domain.menu.Menu;
import com.everyonewaiter.domain.menu.MenuCreateRequest;
import com.everyonewaiter.domain.menu.MenuDeleteRequest;
import com.everyonewaiter.domain.menu.MenuMovePositionRequest;
import com.everyonewaiter.domain.menu.MenuUpdateRequest;
import com.everyonewaiter.domain.menu.MenuView;
import com.everyonewaiter.domain.store.StoreOwner;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
class MenuManagementApi implements MenuManagementApiSpecification {

  private final MenuFinder menuFinder;
  private final MenuManager menuManager;

  @Override
  @StoreOwner
  @GetMapping("/stores/{storeId}/categories/{categoryId}/menus")
  public ResponseEntity<MenuSimpleResponses> getMenus(
      @PathVariable Long storeId,
      @PathVariable Long categoryId,
      @AuthenticationAccount(permission = AccountPermission.OWNER) Account account
  ) {
    List<Menu> menus = menuFinder.findAll(storeId, categoryId);

    return ResponseEntity.ok(MenuSimpleResponses.from(menus));
  }

  @Override
  @StoreOwner
  @GetMapping("/stores/{storeId}/categories/{categoryId}/menus/{menuId}")
  public ResponseEntity<MenuView.MenuDetail> getMenu(
      @PathVariable Long storeId,
      @PathVariable Long categoryId,
      @PathVariable Long menuId,
      @AuthenticationAccount(permission = AccountPermission.OWNER) Account account
  ) {
    return ResponseEntity.ok(menuFinder.findViewOrThrow(menuId, storeId, categoryId));
  }

  @Override
  @StoreOwner
  @PostMapping(
      value = "/stores/{storeId}/categories/{categoryId}/menus",
      consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE}
  )
  public ResponseEntity<Void> create(
      @PathVariable Long storeId,
      @PathVariable Long categoryId,
      @RequestPart MultipartFile file,
      @RequestPart @Valid MenuCreateRequest request,
      @AuthenticationAccount(permission = AccountPermission.OWNER) Account account
  ) {
    Menu menu = menuManager.create(categoryId, storeId, request, file);

    return ResponseEntity.created(URI.create(menu.getNonNullId().toString())).build();
  }

  @Override
  @StoreOwner
  @PutMapping("/stores/{storeId}/menus/{menuId}")
  public ResponseEntity<Void> update(
      @PathVariable Long storeId,
      @PathVariable Long menuId,
      @RequestBody @Valid MenuUpdateRequest request,
      @AuthenticationAccount(permission = AccountPermission.OWNER) Account account
  ) {
    menuManager.update(menuId, storeId, request);

    return ResponseEntity.noContent().build();
  }

  @Override
  @StoreOwner
  @PutMapping(
      value = "/stores/{storeId}/menus/{menuId}/with-image",
      consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE}
  )
  public ResponseEntity<Void> updateWithImage(
      @PathVariable Long storeId,
      @PathVariable Long menuId,
      @RequestPart MultipartFile file,
      @RequestPart @Valid MenuUpdateRequest request,
      @AuthenticationAccount(permission = AccountPermission.OWNER) Account account
  ) {
    menuManager.update(menuId, storeId, request, file);

    return ResponseEntity.noContent().build();
  }

  @Override
  @StoreOwner
  @PostMapping("/stores/{storeId}/menus/{sourceId}/move/{targetId}")
  public ResponseEntity<Void> movePosition(
      @PathVariable Long storeId,
      @PathVariable Long sourceId,
      @PathVariable Long targetId,
      @RequestBody @Valid MenuMovePositionRequest movePositionRequest,
      @AuthenticationAccount(permission = AccountPermission.OWNER) Account account
  ) {
    menuManager.movePosition(sourceId, targetId, storeId, movePositionRequest);

    return ResponseEntity.noContent().build();
  }

  @Override
  @StoreOwner
  @DeleteMapping("/stores/{storeId}/categories/{categoryId}/menus/{menuId}")
  public ResponseEntity<Void> delete(
      @PathVariable Long storeId,
      @PathVariable Long categoryId,
      @PathVariable Long menuId,
      @AuthenticationAccount(permission = AccountPermission.OWNER) Account account
  ) {
    menuManager.delete(menuId, storeId, categoryId);

    return ResponseEntity.noContent().build();
  }

  @Override
  @StoreOwner
  @PostMapping("/stores/{storeId}/menus/delete")
  public ResponseEntity<Void> deleteAll(
      @PathVariable Long storeId,
      @RequestBody @Valid MenuDeleteRequest deleteRequest,
      @AuthenticationAccount(permission = AccountPermission.OWNER) Account account
  ) {
    menuManager.deleteAll(storeId, deleteRequest);

    return ResponseEntity.noContent().build();
  }

}

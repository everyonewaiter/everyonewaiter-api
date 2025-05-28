package com.everyonewaiter.presentation.owner;

import com.everyonewaiter.application.menu.MenuService;
import com.everyonewaiter.application.menu.request.MenuWrite;
import com.everyonewaiter.application.menu.response.MenuResponse;
import com.everyonewaiter.domain.account.entity.Account;
import com.everyonewaiter.global.annotation.AuthenticationAccount;
import com.everyonewaiter.global.annotation.StoreOwner;
import com.everyonewaiter.presentation.owner.request.MenuWriteRequest;
import jakarta.validation.Valid;
import java.net.URI;
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
class MenuManagementController implements MenuManagementControllerSpecification {

  private final MenuService menuService;

  @Override
  @StoreOwner
  @GetMapping("/stores/{storeId}/categories/{categoryId}/menus")
  public ResponseEntity<MenuResponse.Simples> getMenus(
      @PathVariable Long storeId,
      @PathVariable Long categoryId,
      @AuthenticationAccount(permission = Account.Permission.OWNER) Account account
  ) {
    return ResponseEntity.ok(menuService.readAllSimples(storeId, categoryId));
  }

  @Override
  @StoreOwner
  @GetMapping("/stores/{storeId}/categories/{categoryId}/menus/{menuId}")
  public ResponseEntity<MenuResponse.Detail> getMenu(
      @PathVariable Long storeId,
      @PathVariable Long categoryId,
      @PathVariable Long menuId,
      @AuthenticationAccount(permission = Account.Permission.OWNER) Account account
  ) {
    return ResponseEntity.ok(menuService.readDetail(menuId, storeId, categoryId));
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
      @RequestPart @Valid MenuWriteRequest.Create request,
      @AuthenticationAccount(permission = Account.Permission.OWNER) Account account
  ) {
    MenuWrite.Create menuCreateRequest = request.toDomainDto(file);
    Long menuId = menuService.create(categoryId, storeId, menuCreateRequest);
    menuService.replaceMenuOptionGroups(menuId, storeId, menuCreateRequest.menuOptionGroups());
    return ResponseEntity.created(URI.create(menuId.toString())).build();
  }

  @Override
  @StoreOwner
  @PutMapping("/stores/{storeId}/menus/{menuId}")
  public ResponseEntity<Void> update(
      @PathVariable Long storeId,
      @PathVariable Long menuId,
      @RequestBody @Valid MenuWriteRequest.Update request,
      @AuthenticationAccount(permission = Account.Permission.OWNER) Account account
  ) {
    MenuWrite.Update menuUpdateRequest = request.toDomainDto();
    menuService.update(menuId, storeId, menuUpdateRequest);
    menuService.replaceMenuOptionGroups(menuId, storeId, menuUpdateRequest.menuOptionGroups());
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
      @RequestPart @Valid MenuWriteRequest.Update request,
      @AuthenticationAccount(permission = Account.Permission.OWNER) Account account
  ) {
    MenuWrite.Update menuUpdateRequest = request.toDomainDto();
    menuService.update(menuId, storeId, menuUpdateRequest, file);
    menuService.replaceMenuOptionGroups(menuId, storeId, menuUpdateRequest.menuOptionGroups());
    return ResponseEntity.noContent().build();
  }

  @Override
  @StoreOwner
  @PostMapping("/stores/{storeId}/menus/{sourceId}/move/{targetId}")
  public ResponseEntity<Void> movePosition(
      @PathVariable Long storeId,
      @PathVariable Long sourceId,
      @PathVariable Long targetId,
      @RequestBody @Valid MenuWriteRequest.MovePosition request,
      @AuthenticationAccount(permission = Account.Permission.OWNER) Account account
  ) {
    menuService.movePosition(sourceId, targetId, storeId, request.toDomainDto());
    return ResponseEntity.noContent().build();
  }

  @Override
  @StoreOwner
  @DeleteMapping("/stores/{storeId}/categories/{categoryId}/menus/{menuId}")
  public ResponseEntity<Void> delete(
      @PathVariable Long storeId,
      @PathVariable Long categoryId,
      @PathVariable Long menuId,
      @AuthenticationAccount(permission = Account.Permission.OWNER) Account account
  ) {
    menuService.delete(menuId, storeId, categoryId);
    return ResponseEntity.noContent().build();
  }

  @Override
  @StoreOwner
  @PostMapping("/stores/{storeId}/menus/delete")
  public ResponseEntity<Void> deleteAll(
      @PathVariable Long storeId,
      @RequestBody @Valid MenuWriteRequest.Delete request,
      @AuthenticationAccount(permission = Account.Permission.OWNER) Account account
  ) {
    menuService.deleteAll(storeId, request.toDomainDto());
    return ResponseEntity.noContent().build();
  }

}

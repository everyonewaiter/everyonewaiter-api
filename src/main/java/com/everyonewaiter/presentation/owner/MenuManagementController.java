package com.everyonewaiter.presentation.owner;

import com.everyonewaiter.application.menu.MenuService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    Long menuId = menuService.create(categoryId, storeId, request.toDomainDto(file));
    return ResponseEntity.created(URI.create(menuId.toString())).build();
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
    menuService.delete(menuId, categoryId, storeId);
    return ResponseEntity.noContent().build();
  }

}

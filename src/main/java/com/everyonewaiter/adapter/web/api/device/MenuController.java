package com.everyonewaiter.adapter.web.api.device;

import com.everyonewaiter.application.menu.CategoryService;
import com.everyonewaiter.application.menu.response.CategoryResponse;
import com.everyonewaiter.application.store.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
class MenuController implements MenuControllerSpecification {

  private final StoreService storeService;
  private final CategoryService categoryService;

  @Override
  @GetMapping("/stores/{storeId}/menus")
  public ResponseEntity<CategoryResponse.All> getStoreMenus(@PathVariable Long storeId) {
    storeService.checkExistsStore(storeId);
    return ResponseEntity.ok(categoryService.readAllWithMenus(storeId));
  }

}

package com.everyonewaiter.adapter.web.api.device;

import com.everyonewaiter.application.menu.provided.CategoryFinder;
import com.everyonewaiter.domain.menu.CategoryView;
import com.everyonewaiter.domain.store.StoreExist;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
class MenuApi implements MenuApiSpecification {

  private final CategoryFinder categoryFinder;

  @Override
  @StoreExist
  @GetMapping("/stores/{storeId}/menus")
  public ResponseEntity<CategoryView.Categories> getStoreMenus(@PathVariable Long storeId) {
    return ResponseEntity.ok(categoryFinder.findAllView(storeId));
  }

}

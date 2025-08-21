package com.everyonewaiter.adapter.web.api.device;

import com.everyonewaiter.adapter.web.api.dto.CategoryDetailResponses;
import com.everyonewaiter.application.menu.provided.CategoryFinder;
import com.everyonewaiter.domain.menu.CategoryView;
import com.everyonewaiter.domain.store.StoreExist;
import java.util.List;
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
  public ResponseEntity<CategoryDetailResponses> getStoreMenus(@PathVariable Long storeId) {
    List<CategoryView.CategoryDetail> categories = categoryFinder.findAllView(storeId);

    return ResponseEntity.ok(CategoryDetailResponses.from(categories));
  }

}

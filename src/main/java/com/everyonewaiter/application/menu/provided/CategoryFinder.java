package com.everyonewaiter.application.menu.provided;

import com.everyonewaiter.domain.menu.Category;
import com.everyonewaiter.domain.menu.CategoryView;
import java.util.List;

public interface CategoryFinder {

  List<Category> findAll(Long storeId);

  CategoryView.Categories findAllView(Long storeId);

  Category findOrThrow(Long categoryId, Long storeId);

}

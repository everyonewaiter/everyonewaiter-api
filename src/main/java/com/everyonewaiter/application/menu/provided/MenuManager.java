package com.everyonewaiter.application.menu.provided;

import com.everyonewaiter.domain.menu.Menu;
import com.everyonewaiter.domain.menu.MenuCreateRequest;
import com.everyonewaiter.domain.menu.MenuDeleteRequest;
import com.everyonewaiter.domain.menu.MenuMovePositionRequest;
import com.everyonewaiter.domain.menu.MenuUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

public interface MenuManager {

  Menu create(
      Long categoryId,
      Long storeId,
      @Valid MenuCreateRequest createRequest,
      MultipartFile file
  );

  Menu update(Long menuId, Long storeId, @Valid MenuUpdateRequest updateRequest);

  Menu update(
      Long menuId,
      Long storeId,
      @Valid MenuUpdateRequest updateRequest,
      MultipartFile file
  );

  Menu movePosition(
      Long sourceId,
      Long targetId,
      Long storeId,
      @Valid MenuMovePositionRequest movePositionRequest
  );

  void delete(Long menuId, Long storeId, Long categoryId);

  void deleteAll(Long storeId, @Valid MenuDeleteRequest deleteRequest);

}

package com.everyonewaiter.infrastructure.menu;

import static com.everyonewaiter.domain.menu.entity.QMenu.menu;

import com.everyonewaiter.domain.menu.entity.Menu;
import com.everyonewaiter.domain.menu.repository.MenuRepository;
import com.everyonewaiter.global.exception.BusinessException;
import com.everyonewaiter.global.exception.ErrorCode;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class MenuRepositoryImpl implements MenuRepository {

  private final JPAQueryFactory queryFactory;
  private final MenuJpaRepository menuJpaRepository;

  @Override
  public Long countByCategoryId(Long categoryId) {
    return menuJpaRepository.countByCategoryId(categoryId);
  }

  @Override
  public int findLastPositionByCategoryId(Long categoryId) {
    Integer lastPosition = queryFactory
        .select(menu.position.value.max())
        .from(menu)
        .where(menu.category.id.eq(categoryId))
        .fetchOne();
    return Objects.requireNonNullElse(lastPosition, 0);
  }

  @Override
  public List<Menu> findAllByCategoryId(Long categoryId) {
    return menuJpaRepository.findAllByCategoryId(categoryId);
  }

  @Override
  public Menu findByIdAndStoreId(Long menuId, Long storeId) {
    return menuJpaRepository.findByIdAndStoreId(menuId, storeId)
        .orElseThrow(() -> new BusinessException(ErrorCode.MENU_NOT_FOUND));
  }

  @Override
  public Menu findByIdAndStoreIdAndCategoryIdOrThrow(Long menuId, Long storeId, Long categoryId) {
    return menuJpaRepository.findByIdAndStoreIdAndCategoryId(menuId, storeId, categoryId)
        .orElseThrow(() -> new BusinessException(ErrorCode.MENU_NOT_FOUND));
  }

  @Override
  public Menu save(Menu menu) {
    return menuJpaRepository.save(menu);
  }

  @Override
  public void shiftPosition(Menu source) {
    queryFactory
        .update(menu)
        .set(menu.position.value, menu.position.value.add(1))
        .where(
            menu.id.ne(source.getId()),
            menu.storeId.eq(source.getStoreId()),
            menu.position.value.goe(source.getPosition().getValue())
        )
        .execute();
  }

  @Override
  public void delete(Menu menu) {
    menuJpaRepository.delete(menu);
  }

  @Override
  public void deleteAllByCategoryId(Long categoryId) {
    queryFactory
        .delete(menu)
        .where(menu.category.id.eq(categoryId))
        .execute();
  }

}

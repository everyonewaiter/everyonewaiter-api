package com.everyonewaiter.domain.menu;

import com.everyonewaiter.domain.menu.entity.Menu;
import java.util.List;

public record CategoryDeleteEvent(Long categoryId, List<Menu> menus) {

}

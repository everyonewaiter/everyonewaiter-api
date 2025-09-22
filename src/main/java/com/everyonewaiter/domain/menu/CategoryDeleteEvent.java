package com.everyonewaiter.domain.menu;

import java.util.List;

public record CategoryDeleteEvent(Long categoryId, List<String> menuImages) {

}

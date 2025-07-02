package com.everyonewaiter.domain.order.entity;

import java.util.List;

public record ReceiptMenu(String name, int quantity, List<String> options) {

}

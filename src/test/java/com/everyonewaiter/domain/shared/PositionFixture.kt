package com.everyonewaiter.domain.shared

import org.springframework.test.util.ReflectionTestUtils

fun createPosition(value: Int = 0): Position {
  val position = Position()
  ReflectionTestUtils.setField(position, "value", value)
  return position
}

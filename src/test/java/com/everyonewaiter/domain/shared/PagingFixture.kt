package com.everyonewaiter.domain.shared

data class PagingExample(val subject: String, val content: String)

fun createContents(size: Long = 10): List<PagingExample> {
  return List(size.toInt()) { PagingExample("제목$it", "본문$it") }
}

fun createPaging(pagination: Pagination, size: Long = 10): Paging<PagingExample> {
  val contents = createContents(size)
  return Paging(contents, size, pagination)
}

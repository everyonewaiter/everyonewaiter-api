package com.everyonewaiter.global.extension

import org.springframework.data.repository.CrudRepository

fun <T, ID> CrudRepository<T, ID>.findByIdOrNull(id: ID): T? = findById(id!!).orElse(null)

package com.everyonewaiter.domain.auth.event

data class AuthCodeCreateEvent(
    val phoneNumber: String,
    val code: Int,
)

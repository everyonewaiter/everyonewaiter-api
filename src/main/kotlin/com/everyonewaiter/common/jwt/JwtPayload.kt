package com.everyonewaiter.common.jwt

/**
 * JWT 생성 및 추출에 사용되는 데이터를 가진 클래스입니다.
 *
 * @property [id] JWT 고유 식별자
 * @property [subject] JWT 주체
 */
data class JwtPayload(
    val id: String,
    val subject: String,
)

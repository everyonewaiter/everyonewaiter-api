package com.everyonewaiter.common.jwt

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.Date

private const val TWELVE_HOUR_MILLISECONDS = 12 * 60 * 60 * 1000L

/**
 * JWT 토큰을 생성 및 추출하는 클래스입니다.
 *
 * @property [secretKey] JWT 서명에 사용되는 비밀 키
 */
@Component
class JwtProvider(
    @Value("\${jwt.secret-key}") secretKey: String,
) {
    private val secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey))

    /**
     * JWT 토큰을 생성합니다.
     *
     * @param [payload] JWT 생성 시 사용되는 데이터를 가진 DTO
     * @param [expirationMilliseconds] JWT의 만료 시간(밀리초) - 기본값: 12시간
     * @return 생성된 JWT 토큰 문자열
     */
    fun generate(
        payload: JwtPayload,
        expirationMilliseconds: Long = TWELVE_HOUR_MILLISECONDS,
    ): String {
        val now = Date()
        val expiration = Date(now.time + expirationMilliseconds)
        return Jwts
            .builder()
            .id(payload.id)
            .subject(payload.subject)
            .issuedAt(now)
            .expiration(expiration)
            .signWith(secretKey)
            .compact()
    }

    /**
     * JWT 토큰을 디코드하여 데이터를 추출합니다.
     *
     * @param [token] 디코드할 JWT 토큰 문자열
     * @return [JwtPayload] JWT에서 추출된 데이터를 가진 DTO
     * @throws [Exception] JWT 토큰이 유효하지 않은 경우
     */
    fun decode(token: String): Result<JwtPayload> =
        runCatching {
            val payload = Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .payload
            JwtPayload(payload.id, payload.subject)
        }
}

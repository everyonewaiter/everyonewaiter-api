package com.everyonewaiter.presentation.owner.api

import com.everyonewaiter.application.user.dto.AuthCode
import com.everyonewaiter.application.user.dto.AuthMail
import com.everyonewaiter.application.user.dto.SignIn
import com.everyonewaiter.application.user.dto.SignUp
import com.everyonewaiter.presentation.owner.spec.UserApiSpec
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users")
class UserController : UserApiSpec {
    @PostMapping
    override fun signUp(request: SignUp.Request): ResponseEntity<Unit> = ResponseEntity.ok().build()

    @PostMapping("/sign-in")
    override fun signIn(request: SignIn.Request): ResponseEntity<SignIn.Response> = ResponseEntity.ok().build()

    @PostMapping("/send-auth-code")
    override fun sendAuthCode(request: AuthCode.SendRequest): ResponseEntity<Unit> = ResponseEntity.ok().build()

    @PostMapping("/verify-auth-code")
    override fun verifyAuthCode(request: AuthCode.VerifyRequest): ResponseEntity<Unit> = ResponseEntity.ok().build()

    @PostMapping("/send-auth-mail")
    override fun sendAuthMail(request: AuthMail.SendRequest): ResponseEntity<Unit> = ResponseEntity.ok().build()

    @PostMapping("/verify-auth-mail")
    override fun verifyEmail(): ResponseEntity<Unit> = ResponseEntity.ok().build()
}

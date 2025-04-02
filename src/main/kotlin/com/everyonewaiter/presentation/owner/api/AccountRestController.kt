package com.everyonewaiter.presentation.owner.api

import com.everyonewaiter.application.account.dto.Profile
import com.everyonewaiter.application.account.dto.SignIn
import com.everyonewaiter.application.account.dto.SignUp
import com.everyonewaiter.application.account.service.AccountService
import com.everyonewaiter.application.auth.dto.SendAuthCode
import com.everyonewaiter.application.auth.dto.SendAuthMail
import com.everyonewaiter.application.auth.dto.VerifyAuthCode
import com.everyonewaiter.application.auth.service.AuthService
import com.everyonewaiter.domain.account.entity.Account
import com.everyonewaiter.domain.auth.entity.AuthPurpose
import com.everyonewaiter.global.annotation.AuthenticationAccount
import com.everyonewaiter.presentation.owner.spec.AccountControllerSpecification
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/api/v1/accounts")
class AccountRestController(
    private val accountService: AccountService,
    private val authService: AuthService,
) : AccountControllerSpecification {
    @GetMapping("/me")
    override fun getProfile(
        @AuthenticationAccount account: Account,
    ): ResponseEntity<Profile.Response> = ResponseEntity.ok(Profile.Response(account.id.toString(), account.email, account.permission))

    @PostMapping
    override fun signUp(
        @RequestBody @Valid request: SignUp.Request,
    ): ResponseEntity<Unit> {
        authService.checkAuthSuccessExists(request.phoneNumber)
        val accountId = accountService.create(request)
        return ResponseEntity
            .created(URI.create(accountId.toString()))
            .build()
    }

    @PostMapping("/sign-in")
    override fun signIn(
        @RequestBody @Valid request: SignIn.Request,
    ): ResponseEntity<SignIn.Response> {
        val accountId = accountService.signIn(request)
        val accessToken = authService.generateAccessTokenBySignIn(accountId, request.email)
        return ResponseEntity.ok(SignIn.Response(accessToken))
    }

    @PostMapping("/send-auth-code")
    override fun sendAuthCode(
        @RequestBody @Valid request: SendAuthCode.Request,
    ): ResponseEntity<Unit> {
        val purpose = AuthPurpose.SIGN_UP
        accountService.checkPhoneNumberNotInUse(request.phoneNumber)
        authService.checkAuthAttemptExceed(request.phoneNumber, purpose)
        authService.generateCode(request.phoneNumber, purpose)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/verify-auth-code")
    override fun verifyAuthCode(
        @RequestBody @Valid request: VerifyAuthCode.Request,
    ): ResponseEntity<Unit> {
        authService.checkAuthSuccessNotExists(request.phoneNumber)
        authService.verifyCode(request)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/send-auth-mail")
    override fun sendAuthMail(
        @RequestBody @Valid request: SendAuthMail.Request,
    ): ResponseEntity<Unit> {
        accountService.checkCanSendAuthMail(request.email)
        authService.sendAuthMail(request.email)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/verify-auth-mail")
    override fun verifyEmail(
        @RequestParam(value = "token") accessToken: String,
    ): ResponseEntity<Unit> {
        val email = authService.verifyAuthMail(accessToken)
        accountService.activate(email)
        return ResponseEntity.noContent().build()
    }
}

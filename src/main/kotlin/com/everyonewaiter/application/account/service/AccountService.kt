package com.everyonewaiter.application.account.service

import com.everyonewaiter.application.account.dto.SignUp
import com.everyonewaiter.domain.account.entity.Account
import com.everyonewaiter.domain.account.repository.AccountRepository
import com.everyonewaiter.global.exception.ErrorCode
import com.everyonewaiter.global.extension.checkOrThrow
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AccountService(
    private val passwordEncoder: PasswordEncoder,
    private val accountRepository: AccountRepository,
) {
    @Transactional
    fun create(request: SignUp.Request): Long {
        val (email, password, phoneNumber) = request
        checkOrThrow(accountRepository.existsByEmail(email).not(), ErrorCode.ALREADY_USE_EMAIL)
        checkOrThrow(accountRepository.existsByPhone(phoneNumber).not(), ErrorCode.ALREADY_USE_PHONE_NUMBER)
        val encodedPassword = passwordEncoder.encode(password)
        val account = Account.create(email, encodedPassword, phoneNumber)
        return accountRepository.save(account).id
    }

    fun checkPhoneNumberNotInUse(phoneNumber: String) {
        checkOrThrow(existsByPhone(phoneNumber).not(), ErrorCode.ALREADY_USE_PHONE_NUMBER)
    }

    private fun existsByPhone(phoneNumber: String): Boolean = accountRepository.existsByPhone(phoneNumber)
}

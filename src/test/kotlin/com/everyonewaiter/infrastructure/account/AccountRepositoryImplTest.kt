package com.everyonewaiter.infrastructure.account

import com.everyonewaiter.domain.account.entity.Account
import com.everyonewaiter.support.MysqlTest
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.springframework.context.annotation.Import
import org.springframework.data.jdbc.core.JdbcAggregateTemplate

@MysqlTest
@Import(AccountRepositoryImpl::class)
class AccountRepositoryImplTest(
    private val jdbcAggregateTemplate: JdbcAggregateTemplate,
    private val accountRepositoryImpl: AccountRepositoryImpl,
) : FunSpec({
        val account = Account(email = "admin@everyonewaiter.com", password = "@password1", phoneNumber = "01044591812")

        beforeSpec { jdbcAggregateTemplate.insert(account) }

        context("existsByEmail") {
            test("이메일로 계정을 찾으면 True를 반환한다.") {
                accountRepositoryImpl.existsByEmail(account.email) shouldBe true
            }

            test("이메일로 계정을 찾지 못하면 False를 반환한다.") {
                accountRepositoryImpl.existsByEmail("handwoong@gmail.com") shouldBe false
            }
        }

        context("existsByPhone") {
            test("휴대폰 번호로 계정을 찾으면 True를 반환한다.") {
                accountRepositoryImpl.existsByPhone(account.phoneNumber) shouldBe true
            }

            test("휴대폰 번호로 계정을 찾지 못하면 False를 반환한다.") {
                accountRepositoryImpl.existsByPhone("01012345678") shouldBe false
            }
        }
    })

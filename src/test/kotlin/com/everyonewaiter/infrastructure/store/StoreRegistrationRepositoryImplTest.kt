package com.everyonewaiter.infrastructure.store

import com.everyonewaiter.domain.store.entity.BusinessLicenseInformation
import com.everyonewaiter.domain.store.entity.StoreRegistration
import com.everyonewaiter.global.exception.BusinessException
import com.everyonewaiter.global.exception.ErrorCode
import com.everyonewaiter.support.MysqlTest
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.springframework.context.annotation.Import
import org.springframework.data.jdbc.core.JdbcAggregateTemplate

@MysqlTest
@Import(StoreRegistrationRepositoryImpl::class)
class StoreRegistrationRepositoryImplTest(
    private val jdbcAggregateTemplate: JdbcAggregateTemplate,
    private val registrationRepository: StoreRegistrationRepositoryImpl,
) : FunSpec({
        val accountId = 1L
        val totalCount = 10
        val registrations = List(totalCount) { index ->
            val id = index + 1L
            StoreRegistration(
                id = id,
                accountId = accountId,
                licenseInformation = BusinessLicenseInformation(
                    name = "모웨 ${id}호점",
                    ceoName = "홍길동",
                    address = "서울시 강남구",
                    landline = "0212345678",
                    license = "111-11-11111",
                    image = "license.webp",
                ),
            )
        }

        beforeSpec { jdbcAggregateTemplate.insertAll(registrations) }

        context("count") {
            test("계정 ID로 매장 등록 신청 수를 조회한다.") {
                val actual = registrationRepository.count(accountId, 10)
                actual shouldBe totalCount
            }

            test("LIMIT이 실제 데이터 보다 많다면 실제 데이터 수를 반환한다.") {
                val actual = registrationRepository.count(accountId, 20)
                actual shouldBe totalCount
            }
        }

        context("findAll") {
            val limit = 3L

            test("첫번째 페이지를 조회한다.") {
                val actual = registrationRepository.findAll(accountId, limit, 0)
                actual.size shouldBe 3
                actual.forEachIndexed { index, registration ->
                    registration.licenseInformation.name shouldBe "모웨 ${totalCount - index}호점"
                }
            }

            test("마지막 페이지를 조회한다.") {
                val actual = registrationRepository.findAll(accountId, limit, 9)
                actual.size shouldBe 1
                actual.first().licenseInformation.name shouldBe "모웨 1호점"
            }
        }
    })

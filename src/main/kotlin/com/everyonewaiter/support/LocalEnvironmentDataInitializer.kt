package com.everyonewaiter.support

import com.everyonewaiter.common.tsid.Tsid
import com.everyonewaiter.domain.account.entity.Account
import com.everyonewaiter.domain.account.repository.AccountRepository
import com.everyonewaiter.domain.store.entity.BusinessLicenseInformation
import com.everyonewaiter.domain.store.entity.StoreRegistration
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.data.jdbc.core.JdbcAggregateOperations
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.support.TransactionTemplate
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

@Profile("local")
@Component
class LocalEnvironmentDataInitializer(
    private val transactionTemplate: TransactionTemplate,
    private val jdbcAggregateOperations: JdbcAggregateOperations,
    private val passwordEncoder: PasswordEncoder,
    private val accountRepository: AccountRepository,
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        val adminAccountId = getAdminAccountId()
        initAccounts()
        initStoreRegistrations(adminAccountId)
    }

    private fun getAdminAccountId(): Long {
        val adminAccount = accountRepository.findByEmail("admin@everyonewaiter.com")
        return adminAccount?.id
            ?: accountRepository
                .save(
                    Account(
                        id = Tsid.nextLong(),
                        email = "admin@everyonewaiter.com",
                        password = passwordEncoder.encode("@password1"),
                        phoneNumber = "01044591812",
                    ),
                ).id
    }

    private fun initAccounts() {
        val accountCount = jdbcAggregateOperations.count(Account::class.java)
        if (accountCount > 0) return
        BulkInsertExecutor(
            executorName = "Account",
            transactionTemplate = transactionTemplate,
            jdbcAggregateOperations = jdbcAggregateOperations,
            generateData = ::generateAccounts,
        ).execute()
    }

    private fun generateAccounts() =
        List(BULK_INSERT_SIZE) {
            Account(
                id = Tsid.nextLong(),
                email = "${Tsid.nextString()}@everyonewaiter.com",
                password = "@password1",
                phoneNumber = Tsid.nextLong().toString().takeLast(11),
            )
        }

    private fun initStoreRegistrations(adminAccountId: Long) {
        val storeRegistrationCount = jdbcAggregateOperations.count(StoreRegistration::class.java)
        if (storeRegistrationCount > 0) return
        BulkInsertExecutor(
            executorName = "StoreRegistration",
            transactionTemplate = transactionTemplate,
            jdbcAggregateOperations = jdbcAggregateOperations,
            generateData = { generateStoreRegistrations(adminAccountId) },
        ).execute()
    }

    private fun generateStoreRegistrations(adminAccountId: Long) =
        List(BULK_INSERT_SIZE) {
            StoreRegistration(
                id = Tsid.nextLong(),
                accountId = adminAccountId,
                licenseInformation = BusinessLicenseInformation(
                    name = "홍길동식당",
                    ceoName = "홍길동",
                    address = "경상남도 창원시 의창구 123",
                    landline = "055-123-4567",
                    license = "123-45-67890",
                    image = "license/202504/0KA652ZFZ26DG.webp",
                ),
            )
        }

    class BulkInsertExecutor<T>(
        private val executorName: String,
        private val transactionTemplate: TransactionTemplate,
        private val jdbcAggregateOperations: JdbcAggregateOperations,
        private val generateData: () -> List<T>,
    ) {
        fun execute() {
            val countDownLatch = CountDownLatch(EXECUTE_COUNT)
            val executorService = Executors.newFixedThreadPool(10)
            repeat(EXECUTE_COUNT) {
                executorService.submit {
                    transactionTemplate.executeWithoutResult { _ ->
                        val data = generateData()
                        jdbcAggregateOperations.insertAll(data)
                    }
                    countDownLatch.countDown()
                    println("[$executorName] CountDownLatch.count=${countDownLatch.count}")
                }
            }
            countDownLatch.await()
            executorService.shutdown()
        }
    }

    private companion object {
        private const val BULK_INSERT_SIZE = 1000
        private const val EXECUTE_COUNT = 10000
    }
}

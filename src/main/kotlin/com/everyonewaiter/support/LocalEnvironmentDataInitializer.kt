package com.everyonewaiter.support

import com.everyonewaiter.common.tsid.Tsid
import com.everyonewaiter.domain.account.entity.Account
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.data.jdbc.core.JdbcAggregateOperations
import org.springframework.stereotype.Component
import org.springframework.transaction.support.TransactionTemplate
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

@Profile("local")
@Component
class LocalEnvironmentDataInitializer(
    private val transactionTemplate: TransactionTemplate,
    private val jdbcAggregateOperations: JdbcAggregateOperations,
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        initAccounts()
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

    companion object {
        private const val BULK_INSERT_SIZE = 1000
        private const val EXECUTE_COUNT = 10000
    }
}

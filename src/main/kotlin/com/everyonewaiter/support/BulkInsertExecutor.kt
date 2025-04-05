package com.everyonewaiter.support

import org.springframework.data.jdbc.core.JdbcAggregateOperations
import org.springframework.transaction.support.TransactionTemplate
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

class BulkInsertExecutor<T>(
    private val threadCount: Int,
    private val executorName: String,
    private val transactionTemplate: TransactionTemplate,
    private val jdbcAggregateOperations: JdbcAggregateOperations,
    private val generateData: () -> List<T>,
) {
    fun execute(executeCount: Int) {
        val countDownLatch = CountDownLatch(executeCount)
        val executorService = Executors.newFixedThreadPool(threadCount)
        repeat(executeCount) {
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

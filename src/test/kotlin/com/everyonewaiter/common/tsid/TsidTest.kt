package com.everyonewaiter.common.tsid

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.util.concurrent.Executors
import java.util.concurrent.Future

class TsidTest :
    FunSpec({
        context("nextLong") {
            val createIdCount = 1000L
            val repeatCount = 1000L

            test("중복되지 않는 숫자 Tsid를 생성한다.") {
                val executorService = Executors.newFixedThreadPool(10)
                val futures = mutableListOf<Future<List<Long>>>()

                for (i in 1..repeatCount) {
                    futures.add(
                        executorService.submit<List<Long>> {
                            val ids = mutableListOf<Long>()
                            for (j in 1..createIdCount) {
                                ids.add(Tsid.nextLong())
                            }
                            ids
                        },
                    )
                }

                val results = futures.flatMap { it.get() }.toSet()
                results.size shouldBe createIdCount * repeatCount
                executorService.shutdown()
            }
        }

        context("nextString") {
            val createIdCount = 1000L
            val repeatCount = 1000L

            test("중복되지 않는 문자열 Tsid를 생성한다.") {
                val executorService = Executors.newFixedThreadPool(10)
                val futures = mutableListOf<Future<List<String>>>()

                for (i in 1..repeatCount) {
                    futures.add(
                        executorService.submit<List<String>> {
                            val ids = mutableListOf<String>()
                            for (j in 1..createIdCount) {
                                ids.add(Tsid.nextString())
                            }
                            ids
                        },
                    )
                }

                val results = futures.flatMap { it.get() }.toSet()
                results.size shouldBe createIdCount * repeatCount
                executorService.shutdown()
            }
        }
    })

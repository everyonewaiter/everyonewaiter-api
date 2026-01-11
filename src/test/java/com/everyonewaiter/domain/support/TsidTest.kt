package com.everyonewaiter.domain.support

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.concurrent.Callable
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.function.Supplier
import java.util.stream.Collectors

class TsidTest {

  @Test
  fun `고유 숫자 ID 생성`() {
    Executors.newFixedThreadPool(10).use { executorService ->
      val futures = mutableListOf<Future<List<Long>>>()
      repeat(REPEAT_COUNT) {
        val future = executorService.submit(createTsidIds { Tsid.nextLong() })
        futures.add(future)
      }

      val ids = collectFuturesToSet(futures)
      assertThat(ids).hasSize(REPEAT_COUNT * ID_CREATE_COUNT)
      executorService.shutdown()
    }
  }

  @Test
  fun `고유 문자열 ID 생성`() {
    Executors.newFixedThreadPool(10).use { executorService ->
      val futures = mutableListOf<Future<List<String>>>()
      repeat(REPEAT_COUNT) {
        val future = executorService.submit(createTsidIds { Tsid.nextString() })
        futures.add(future)
      }

      val ids = collectFuturesToSet(futures)
      assertThat(ids).hasSize(REPEAT_COUNT * ID_CREATE_COUNT)
      executorService.shutdown()
    }
  }

  private fun <T> createTsidIds(idFactory: Supplier<T>): Callable<List<T>> {
    return Callable {
      val ids = mutableListOf<T>()
      repeat(ID_CREATE_COUNT) {
        ids.add(idFactory.get())
      }
      ids
    }
  }

  private fun <T> collectFuturesToSet(futures: List<Future<List<T>>>): Set<T> {
    return futures.stream()
      .flatMap<T> {
        try {
          return@flatMap it.get().stream()
        } catch (e: InterruptedException) {
          throw RuntimeException(e)
        } catch (e: ExecutionException) {
          throw RuntimeException(e)
        }
      }
      .collect(Collectors.toSet())
  }

  companion object {

    const val REPEAT_COUNT = 1000
    const val ID_CREATE_COUNT = 1000
  }
}


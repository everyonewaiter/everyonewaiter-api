package com.everyonewaiter.domain.support;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class TsidTest {

  private static final int REPEAT_COUNT = 1000;
  private static final int CREATE_ID_COUNT = 1000;

  @Test
  void nextLong() {
    try (ExecutorService executorService = Executors.newFixedThreadPool(10)) {
      List<Future<List<Long>>> futures = new ArrayList<>();

      for (int i = 0; i < REPEAT_COUNT; i++) {
        Future<List<Long>> future = executorService.submit(createTsidIds(Tsid::nextLong));

        futures.add(future);
      }

      Set<Long> ids = collectFuturesToSet(futures);
      assertThat(ids).hasSize(REPEAT_COUNT * CREATE_ID_COUNT);

      executorService.shutdown();
    }
  }

  @Test
  void nextString() {
    try (ExecutorService executorService = Executors.newFixedThreadPool(10)) {
      List<Future<List<String>>> futures = new ArrayList<>();

      for (int i = 0; i < REPEAT_COUNT; i++) {
        Future<List<String>> future = executorService.submit(createTsidIds(Tsid::nextString));

        futures.add(future);
      }

      Set<String> ids = collectFuturesToSet(futures);
      assertThat(ids).hasSize(REPEAT_COUNT * CREATE_ID_COUNT);

      executorService.shutdown();
    }
  }

  private <T> Callable<List<T>> createTsidIds(Supplier<T> idFactory) {
    return () -> {
      List<T> ids = new ArrayList<>();

      for (int j = 0; j < CREATE_ID_COUNT; j++) {
        ids.add(idFactory.get());
      }

      return ids;
    };
  }

  private <T> Set<T> collectFuturesToSet(List<Future<List<T>>> futures) {
    return futures.stream()
        .flatMap(future -> {
          try {
            return future.get().stream();
          } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
          }
        })
        .collect(Collectors.toSet());
  }

}

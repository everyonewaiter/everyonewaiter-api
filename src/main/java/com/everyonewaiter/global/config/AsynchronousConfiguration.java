package com.everyonewaiter.global.config;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
class AsynchronousConfiguration {

  @Bean(name = "eventTaskExecutor")
  public TaskExecutor eventTaskExecutor() {
    ThreadFactory factory = Thread.ofVirtual().name("app-event-", 1).factory();
    return new TaskExecutorAdapter(Executors.newThreadPerTaskExecutor(factory));
  }

}

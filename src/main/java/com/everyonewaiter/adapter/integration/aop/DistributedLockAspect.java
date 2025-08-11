package com.everyonewaiter.adapter.integration.aop;

import static java.util.Objects.requireNonNull;

import com.everyonewaiter.application.support.DistributedLock;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
class DistributedLockAspect {

  private static final Logger LOGGER = LoggerFactory.getLogger(DistributedLockAspect.class);

  private static final String LOCK_KEY_PREFIX = "lock:";

  private final AopTransaction aopTransaction;
  private final RedissonClient redissonClient;
  private final SpelExpressionParser spelExpressionParser = new SpelExpressionParser();

  @Pointcut("@annotation(com.everyonewaiter.application.support.DistributedLock)")
  public void distributedLock() {
    // Pointcut for all methods annotated with @RedissonLock
  }

  @Around("distributedLock()")
  public Object lock(ProceedingJoinPoint joinPoint) throws Throwable {
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    Method method = signature.getMethod();
    DistributedLock redissonLock = requireNonNull(method.getAnnotation(DistributedLock.class));

    List<RLock> locks = Arrays.stream(redissonLock.key())
        .map(key ->
            LOCK_KEY_PREFIX + parseLockKey(key, signature.getParameterNames(), joinPoint.getArgs())
        )
        .map(redissonClient::getLock)
        .toList();

    try {
      List<Boolean> lockConditions = new ArrayList<>();

      for (RLock lock : locks) {
        lockConditions.add(
            lock.tryLock(
                redissonLock.waitTime(),
                redissonLock.leaseTime(),
                redissonLock.timeUnit()
            )
        );
      }

      if (lockConditions.stream().allMatch(Boolean::booleanValue)) {
        return aopTransaction.proceed(joinPoint);
      } else {
        return false;
      }
    } catch (InterruptedException exception) {
      LOGGER.error("[분산락 적용 중단] 메서드명: {}", method.getName(), exception);

      Thread.currentThread().interrupt();

      return false;
    } finally {
      for (RLock lock : locks) {
        if (lock.isLocked() && lock.isHeldByCurrentThread()) {
          lock.unlock();
        }
      }
    }
  }

  private String parseLockKey(String lockKey, String[] parameterNames, Object[] args) {
    StandardEvaluationContext context = new StandardEvaluationContext();

    for (int i = 0; i < parameterNames.length; i++) {
      context.setVariable(parameterNames[i], args[i]);
    }

    return (String) spelExpressionParser.parseExpression(lockKey).getValue(context);
  }

}

package com.everyonewaiter.global.aop;

import com.everyonewaiter.global.annotation.RedissonLock;
import java.lang.reflect.Method;
import java.util.Objects;
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
class RedissonLockAspect {

  private static final Logger LOGGER = LoggerFactory.getLogger(RedissonLockAspect.class);
  private static final String LOCK_KEY_PREFIX = "lock:";

  private final AopTransaction aopTransaction;
  private final RedissonClient redissonClient;
  private final SpelExpressionParser spelExpressionParser = new SpelExpressionParser();

  @Pointcut("@annotation(com.everyonewaiter.global.annotation.RedissonLock)")
  public void redissonLock() {
    // Pointcut for all methods annotated with @RedissonLock
  }

  @Around("redissonLock()")
  public Object lock(ProceedingJoinPoint joinPoint) throws Throwable {
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    Method method = signature.getMethod();
    RedissonLock redissonLock = Objects.requireNonNull(method.getAnnotation(RedissonLock.class));

    String lockKey = LOCK_KEY_PREFIX + parseLockKey(
        redissonLock.key(),
        signature.getParameterNames(),
        joinPoint.getArgs()
    );
    RLock lock = redissonClient.getLock(lockKey);

    try {
      boolean isLocked = lock.tryLock(
          redissonLock.waitTime(),
          redissonLock.leaseTime(),
          redissonLock.timeUnit()
      );
      if (isLocked) {
        return aopTransaction.proceed(joinPoint);
      } else {
        return false;
      }
    } catch (InterruptedException exception) {
      LOGGER.error("[분산락 적용 중단] 메서드명: {}", method.getName(), exception);
      Thread.currentThread().interrupt();
      return false;
    } finally {
      if (lock.isLocked() && lock.isHeldByCurrentThread()) {
        lock.unlock();
      }
    }
  }

  public String parseLockKey(String lockKey, String[] parameterNames, Object[] args) {
    StandardEvaluationContext context = new StandardEvaluationContext();
    for (int i = 0; i < parameterNames.length; i++) {
      context.setVariable(parameterNames[i], args[i]);
    }
    return (String) spelExpressionParser.parseExpression(lockKey).getValue(context);
  }

}

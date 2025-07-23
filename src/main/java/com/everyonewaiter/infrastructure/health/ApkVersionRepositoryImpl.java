package com.everyonewaiter.infrastructure.health;

import static com.everyonewaiter.domain.health.entity.QApkVersion.apkVersion;

import com.everyonewaiter.domain.health.entity.ApkVersion;
import com.everyonewaiter.domain.health.repository.ApkVersionRepository;
import com.everyonewaiter.global.exception.BusinessException;
import com.everyonewaiter.global.exception.ErrorCode;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class ApkVersionRepositoryImpl implements ApkVersionRepository {

  private final JPAQueryFactory queryFactory;
  private final ApkVersionJpaRepository apkVersionJpaRepository;

  @Override
  public ApkVersion findLatest() {
    return Optional.ofNullable(
            queryFactory
                .select(apkVersion)
                .from(apkVersion)
                .orderBy(apkVersion.id.desc())
                .fetchFirst()
        )
        .orElseThrow(() -> new BusinessException(ErrorCode.APK_VERSION_NOT_FOUND));
  }

  @Override
  public ApkVersion save(ApkVersion apkVersion) {
    return apkVersionJpaRepository.save(apkVersion);
  }

}

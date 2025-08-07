package com.everyonewaiter.adapter.persistence.health;

import static com.everyonewaiter.domain.health.QApkVersion.apkVersion;

import com.everyonewaiter.application.health.required.ApkVersionRepository;
import com.everyonewaiter.domain.health.ApkVersion;
import com.everyonewaiter.domain.health.ApkVersionNotFoundException;
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
        .orElseThrow(ApkVersionNotFoundException::new);
  }

  @Override
  public ApkVersion save(ApkVersion apkVersion) {
    return apkVersionJpaRepository.save(apkVersion);
  }

}

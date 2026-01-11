package com.everyonewaiter.application.health;

import static com.everyonewaiter.domain.shared.ErrorCode.FAILED_EXTERNAL_SERVER_COMMUNICATION;

import com.everyonewaiter.application.health.provided.HealthCheckCreator;
import com.everyonewaiter.application.health.required.ApkVersionRepository;
import com.everyonewaiter.domain.health.ApkVersion;
import com.everyonewaiter.domain.health.ApkVersionCreateRequest;
import com.everyonewaiter.domain.shared.BusinessException;
import com.everyonewaiter.domain.shared.GithubRelease;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Validated
@Service
@Transactional
@RequiredArgsConstructor
class HealthCheckModifyService implements HealthCheckCreator {

  private static final String STORE_APP_REPO = "https://api.github.com/repos/everyonewaiter/everyonewaiter-store-app";
  private static final RestClient STORE_APP_REPO_CLIENT = RestClient.create(STORE_APP_REPO);

  private final ApkVersionRepository apkVersionRepository;

  @Override
  public ApkVersion createApkVersion() {
    try {
      GithubRelease latestRelease = STORE_APP_REPO_CLIENT.get()
          .uri("/releases/latest")
          .accept(MediaType.APPLICATION_JSON)
          .retrieve()
          .body(GithubRelease.class);

      Assert.notNull(latestRelease, "매장 앱 릴리즈 정보는 null일 수 없습니다.");
      Assert.notEmpty(latestRelease.assets(), "매장 앱 APK 에셋을 찾을 수 없습니다.");

      String[] versionTokens = latestRelease.tag_name().split("\\.");
      ApkVersionCreateRequest createRequest = new ApkVersionCreateRequest(
          Integer.parseInt(versionTokens[0].replace("v", "")),
          Integer.parseInt(versionTokens[1]),
          Integer.parseInt(versionTokens[2]),
          latestRelease.assets().getFirst().browser_download_url()
      );

      ApkVersion apkVersion = ApkVersion.create(createRequest);

      return apkVersionRepository.save(apkVersion);
    } catch (RestClientResponseException exception) {
      throw new BusinessException(FAILED_EXTERNAL_SERVER_COMMUNICATION);
    }
  }

  @Override
  public ApkVersion createApkVersion(ApkVersionCreateRequest createRequest) {
    ApkVersion apkVersion = ApkVersion.create(createRequest);

    return apkVersionRepository.save(apkVersion);
  }

}

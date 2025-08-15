package com.everyonewaiter.application.health.provided;

import static com.everyonewaiter.domain.health.ApkVersionFixture.createApkVersionCreateRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.everyonewaiter.IntegrationTest;
import com.everyonewaiter.domain.health.ApkVersion;
import com.everyonewaiter.domain.health.ApkVersionCreateRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

@RequiredArgsConstructor
class HealthCheckCreatorTest extends IntegrationTest {

  private final HealthCheckCreator healthCheckCreator;

  @Test
  void createApkVersion() {
    ApkVersionCreateRequest createRequest = createApkVersionCreateRequest();

    ApkVersion apkVersion = healthCheckCreator.createApkVersion(createRequest);

    assertThat(apkVersion.getId()).isNotNull();
  }

  @Test
  @SuppressWarnings("DataFlowIssue")
  void createApkVersionRequestFail() {
    checkValidation(new ApkVersionCreateRequest(0, 0, 0, ""));
    checkValidation(new ApkVersionCreateRequest(1, 101, 0, ""));
    checkValidation(new ApkVersionCreateRequest(1, 0, 101, ""));
    checkValidation(new ApkVersionCreateRequest(1, 0, 0, null));
  }

  private void checkValidation(ApkVersionCreateRequest createRequest) {
    assertThatThrownBy(() -> healthCheckCreator.createApkVersion(createRequest))
        .isInstanceOf(ConstraintViolationException.class);
  }

}

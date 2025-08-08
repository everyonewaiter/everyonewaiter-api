package com.everyonewaiter.application.health.provided;

import static com.everyonewaiter.domain.health.ApkVersionFixture.createRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.everyonewaiter.IntegrationTest;
import com.everyonewaiter.domain.health.ApkVersionCreateRequest;
import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;

@IntegrationTest
record HealthCheckCreatorTest(EntityManager entityManager, HealthCheckCreator healthCheckCreator) {

  @Test
  void createApkVersion() {
    ApkVersionCreateRequest createRequest = createRequest();

    Long apkVersionId = healthCheckCreator.createApkVersion(createRequest);
    entityManager.flush();
    entityManager.clear();

    assertThat(apkVersionId).isNotNull();
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

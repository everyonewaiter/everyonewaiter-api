package com.everyonewaiter.domain.health;

import static lombok.AccessLevel.PROTECTED;

import com.everyonewaiter.domain.AggregateRootEntity;
import jakarta.persistence.Entity;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = PROTECTED)
public class ApkVersion extends AggregateRootEntity<ApkVersion> {

  private int majorVersion;

  private int minorVersion;

  private int patchVersion;

  private String downloadUrl;

  public static ApkVersion create(ApkVersionCreateRequest createRequest) {
    ApkVersion apkVersion = new ApkVersion();

    apkVersion.majorVersion = createRequest.majorVersion();
    apkVersion.minorVersion = createRequest.minorVersion();
    apkVersion.patchVersion = createRequest.patchVersion();
    apkVersion.downloadUrl = Objects.requireNonNull(createRequest.downloadUrl());

    return apkVersion;
  }

}

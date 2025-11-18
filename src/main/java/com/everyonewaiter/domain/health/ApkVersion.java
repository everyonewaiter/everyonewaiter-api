package com.everyonewaiter.domain.health;

import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PROTECTED;

import com.everyonewaiter.domain.AggregateRootEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "apk_version")
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = PROTECTED)
public class ApkVersion extends AggregateRootEntity<ApkVersion> {

  @Column(name = "apk_major_version", nullable = false)
  private int majorVersion;

  @Column(name = "apk_minor_version", nullable = false)
  private int minorVersion;

  @Column(name = "apk_patch_version", nullable = false)
  private int patchVersion;

  @Column(name = "apk_download_uri", nullable = false)
  private String downloadUri;

  public static ApkVersion create(ApkVersionCreateRequest createRequest) {
    ApkVersion apkVersion = new ApkVersion();

    apkVersion.majorVersion = createRequest.majorVersion();
    apkVersion.minorVersion = createRequest.minorVersion();
    apkVersion.patchVersion = createRequest.patchVersion();
    apkVersion.downloadUri = requireNonNull(createRequest.downloadUri());

    return apkVersion;
  }

}

package com.everyonewaiter.domain.health.entity;

import com.everyonewaiter.global.domain.entity.AggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "apk_version")
@Entity
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApkVersion extends AggregateRoot<ApkVersion> {

  @Column(name = "apk_major_version", nullable = false)
  private int majorVersion;

  @Column(name = "apk_minor_version", nullable = false)
  private int minorVersion;

  @Column(name = "apk_patch_version", nullable = false)
  private int patchVersion;

  @Column(name = "apk_download_url", nullable = false)
  private String downloadUrl;

  public static ApkVersion create(
      int majorVersion,
      int minorVersion,
      int patchVersion,
      String downloadUrl
  ) {
    ApkVersion apkVersion = new ApkVersion();
    apkVersion.majorVersion = majorVersion;
    apkVersion.minorVersion = minorVersion;
    apkVersion.patchVersion = patchVersion;
    apkVersion.downloadUrl = downloadUrl;
    return apkVersion;
  }

}

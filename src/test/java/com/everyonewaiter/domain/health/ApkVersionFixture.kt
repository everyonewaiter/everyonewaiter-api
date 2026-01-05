package com.everyonewaiter.domain.health

fun createApkVersionCreateRequest(
  majorVersion: Int = 1,
  minorVersion: Int = 0,
  patchVersion: Int = 0,
  downloadUri: String = "https://github.com/everyonewaiter/everyonewaiter-store-app/releases"
): ApkVersionCreateRequest {
  return ApkVersionCreateRequest(majorVersion, minorVersion, patchVersion, downloadUri)
}

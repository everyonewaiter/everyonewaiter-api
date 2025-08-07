package com.everyonewaiter.application.health.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.boot.info.BuildProperties;

@Schema(name = "ServerVersionDetailResponse")
public record ServerVersionDetailResponse(
    @Schema(description = "서버 버전", example = "1.0.0")
    String version,

    @Schema(description = "서버 그룹", example = "com.everyonewaiter")
    String group,

    @Schema(description = "서버 아티팩트", example = "everyonewaiter-api")
    String artifact
) {

  public static ServerVersionDetailResponse from(BuildProperties buildProperties) {
    return new ServerVersionDetailResponse(
        buildProperties.getVersion(),
        buildProperties.getGroup(),
        buildProperties.getArtifact()
    );
  }

}

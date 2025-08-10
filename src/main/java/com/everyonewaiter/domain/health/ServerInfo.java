package com.everyonewaiter.domain.health;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.boot.info.BuildProperties;

@Schema(name = "ServerInfo")
public record ServerInfo(
    @Schema(description = "서버 버전", example = "1.0.0")
    String version,

    @Schema(description = "서버 그룹", example = "com.everyonewaiter")
    String group,

    @Schema(description = "서버 아티팩트", example = "everyonewaiter-api")
    String artifact
) {

  public static ServerInfo create(BuildProperties buildProperties) {
    return new ServerInfo(
        buildProperties.getVersion(),
        buildProperties.getGroup(),
        buildProperties.getArtifact()
    );
  }

}

package com.megachollos.testUtils.integration;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DockerComposeInfo {

  private String serviceName;
  private Integer servicePort;
  private String ymlFile;
}

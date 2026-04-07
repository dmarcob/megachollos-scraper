package com.megachollos.shared.core.lib.web.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

  @Value("${spring.application.name}")
  private String appName;

  @Value("${core.swagger.server.url}")
  private String serverUrl;

  @Value("${core.swagger.server.description}")
  private String serverDescription;

  @Bean
  public OpenAPI customOpenAPI() {
    Info info = new Info()
        .title(appName + " API Documentation")
        .description("Documentación de la API de " + appName);

    Server server = new Server().url(serverUrl).description(serverDescription);

    return new OpenAPI()
        .info(info)
        .addServersItem(server);
  }
}
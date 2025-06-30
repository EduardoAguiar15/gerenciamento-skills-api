package br.com.neki.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
      .info(new Info()
        .title("API De Gerenciamento De Usuarios e Skills")
        .version("1.0.0")
        .description("API desenvolvida para gerenciar o relacionamento entre usuário e suas skills"));
  }
}
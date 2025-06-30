package br.com.neki.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.basePackage("br.com.neki"))
				.paths(PathSelectors.any()).build().apiInfo(apiInfo());
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("API De Gerenciamento De Usuarios e Skills")
				.description("API desenvolvida para gerenciar o relacionamento entre usuário e suas skills")
				.version("1.0.0").build();
	}
}
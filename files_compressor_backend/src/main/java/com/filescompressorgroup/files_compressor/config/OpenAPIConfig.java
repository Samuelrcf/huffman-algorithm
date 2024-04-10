package com.filescompressorgroup.files_compressor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenAPIConfig {

	@Bean
	OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("Compactador e Descompactador de Arquivos")
						.version("v1")
						.description("API para compactar e descompactar arquivos")
						.termsOfService("Termos")
						.license(
								new License()
										.name("Apache 2.0")
										.url("URL")));
	}

}

package com.HomeRentSolution.ms_limpieza.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("API 2026 Gestión de Limpiezas y Aseo")
                .version("1.0")
                .description("Documentación de la API para el control de estados de limpieza de propiedades"));
    }
}

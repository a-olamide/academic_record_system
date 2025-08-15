package org.olamide.academicrecordmanagementsystem.api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Academic Records Management System",
                version = "v1",
                description = "CRUD + registration with classroom, courses & transcript",
                contact = @Contact(name = "SWE Team", email = "team@miu.edu", url = "https://miu.edu")
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local")
        }
)
public class OpenApiConfig {
        @Bean
        public OpenAPI armsOpenAPI() {
                final String schemeName = "bearerAuth";

                return new OpenAPI()
                        .addSecurityItem(new SecurityRequirement().addList(schemeName))
                        .components(new Components().addSecuritySchemes(
                                schemeName,
                                new SecurityScheme()
                                        .name("Authorization")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        ));
        }
}

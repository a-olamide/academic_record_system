package org.olamide.academicrecordmanagementsystem.api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
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
public class OpenApiConfig { }

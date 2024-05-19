package com.davidmaisuradze.gymapplication.config.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Gym Application",
                description = "CRUD operations for gym api",
                contact = @Contact(
                        name = "Davit Maisuradze",
                        email = "myemail@gmail.com"
                ),
                version = "v1"
        ),
        servers = {
                @Server(
                        description = "Dev server",
                        url = "http://localhost:8080"
                )
        }
)
public class OpenApiConfig {
}

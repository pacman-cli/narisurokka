package org.example.authservice.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

  @Value("${server.port:8081}")
  private String serverPort;

  @Bean
  public OpenAPI authServiceOpenAPI() {
    Server localServer = new Server();
    localServer.setUrl("http://localhost:" + serverPort);
    localServer.setDescription("Local Development Server");

    Contact contact = new Contact();
    contact.setName("NariSurokkha Team");
    contact.setEmail("support@narisurokkha.com");

    io.swagger.v3.oas.models.info.Info info = new io.swagger.v3.oas.models.info.Info();
    info.setTitle("NariSurokkha - Auth Service API");
    info.setDescription("JWT authentication, registration, login, token refresh, and role-based access control");
    info.setVersion("1.0.0");
    info.setContact(contact);

    SecurityScheme bearerScheme = new SecurityScheme()
        .type(SecurityScheme.Type.HTTP)
        .scheme("bearer")
        .bearerFormat("JWT")
        .description("JWT access token");

    OpenAPI openAPI = new OpenAPI();
    openAPI.setInfo(info);
    openAPI.setServers(List.of(localServer));
    openAPI.components(new Components().addSecuritySchemes("bearerAuth", bearerScheme));
    openAPI.addSecurityItem(new SecurityRequirement().addList("bearerAuth"));

    return openAPI;
  }
}

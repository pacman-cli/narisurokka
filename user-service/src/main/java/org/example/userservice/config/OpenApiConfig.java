package org.example.userservice.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;

/**
 * OpenAPI/Swagger configuration for User Service API documentation.
 */
@Configuration
public class OpenApiConfig {

  @Value("${server.port:8083}")
  private String serverPort;

  @Bean
  public OpenAPI userServiceOpenAPI() {
    Server localServer = new Server();
    localServer.setUrl("http://localhost:" + serverPort);
    localServer.setDescription("Local Development Server");

    Contact contact = new Contact();
    contact.setName("NariSurokkha Team");
    contact.setEmail("support@narisurokkha.com");

    io.swagger.v3.oas.models.info.Info info = new io.swagger.v3.oas.models.info.Info();
    info.setTitle("NariSurokkha - User Service API");
    info.setDescription(
        "User registration, profile management, and preferences for the NariSurokkha Women Safety Platform");
    info.setVersion("1.0.0");
    info.setContact(contact);

    OpenAPI openAPI = new OpenAPI();
    openAPI.setInfo(info);
    openAPI.setServers(List.of(localServer));

    return openAPI;
  }
}

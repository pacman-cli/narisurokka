package org.example.sosservice.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

/**
 * OpenAPI/Swagger configuration for SOS Service API documentation.
 */
@Configuration
public class OpenApiConfig {

  @Value("${server.port:8080}")
  private String serverPort;

  @Bean
  public OpenAPI sosServiceOpenAPI() {
    Server localServer = new Server();
    localServer.setUrl("http://localhost:" + serverPort);
    localServer.setDescription("Local Development Server");

    Contact contact = new Contact();
    contact.setName("NariSurokkha Team");
    contact.setEmail("support@narisurokkha.com");

    License license = new License();
    license.setName("MIT License");
    license.setUrl("https://opensource.org/licenses/MIT");

    Info info = new Info();
    info.setTitle("SOS Service API");
    info.setDescription("Emergency SOS alert microservice for NariSurokkha platform. " +
        "Handles triggering and cancelling SOS alerts with Kafka event publishing and Redis caching.");
    info.setVersion("1.0.0");
    info.setContact(contact);
    info.setLicense(license);

    OpenAPI openAPI = new OpenAPI();
    openAPI.setInfo(info);
    openAPI.setServers(List.of(localServer));

    return openAPI;
  }
}

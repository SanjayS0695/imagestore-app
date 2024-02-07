package com.codecademy.imagestore.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Profile({"local", "prod"})
@Configuration
public class OpenAPIConfig {

    @Value("${imagestore.openapi.host-url}")
    private String serverUrl;

    @Bean
    public OpenAPI myOpenAPI() {
        var server = new Server();
        server.setUrl(serverUrl);
        server.setDescription("Server URL for image store");

        var contact = new Contact();
        contact.setName("SanjayS");
        contact.setEmail("sanjay.suresh0695@gmail.com");

        var info = new Info()
                .title("ImageStore API")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints for ImageStore App");
        return new OpenAPI().info(info).servers(List.of(server));
    }

}

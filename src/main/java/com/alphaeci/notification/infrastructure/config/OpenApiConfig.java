package com.alphaeci.notification.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Notification Service API")
                        .description("""
                                AlphaECI — PATRICI.A Notification Microservice.

                                Event-driven service that consumes 9 RabbitMQ queues from the platform's
                                producer modules and turns them into notifications. Delivery is IN_APP over
                                WebSocket STOMP (`/ws-notifications`, user destination
                                `/user/{userId}/queue/notifications`); every notification is also persisted in
                                MongoDB so it survives the user being offline. OTP_VERIFICATION and
                                PASSWORD_RESET are additionally sent by email.

                                Authentication is handled by the API Gateway, which propagates the
                                authenticated user in the `X-User-Id` header. The WebSocket channel is not
                                described here — see the README.
                                """)
                        .version("1.0.0")
                        .license(new License().name("Escuela Colombiana de Ingeniería Julio Garavito")))
                .tags(List.of(
                        new Tag().name("Notifications — Send").description("Creation and delivery of notifications"),
                        new Tag().name("Notifications — Inbox").description("Query and read state of a user's notifications"),
                        new Tag().name("Notification Preferences").description("Per-user opt-in and opt-out of notification types"),
                        new Tag().name("Event Reminders").description("Scheduled reminders for saved events")))
                .components(new Components()
                        .addParameters("XUserId", new HeaderParameter()
                                .name("X-User-Id")
                                .description("Authenticated user, propagated by the API Gateway")
                                .required(true)
                                .schema(new StringSchema().format("uuid"))));
    }
}

package com.alphaeci.notification.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ConfigTest {

    @Test
    void openApiConfig_buildsDefinition() {
        OpenAPI api = new OpenApiConfig().openAPI();
        assertNotNull(api);
        assertEquals("Notification Service API", api.getInfo().getTitle());
        assertEquals("1.0.0", api.getInfo().getVersion());
        assertEquals(4, api.getTags().size());
        assertNotNull(api.getComponents().getParameters().get("XUserId"));
    }

    @Test
    void webSocketConfig_registersEndpointsAndBroker() {
        WebSocketConfig config = new WebSocketConfig();

        StompEndpointRegistry stompRegistry = mock(StompEndpointRegistry.class, RETURNS_DEEP_STUBS);
        config.registerStompEndpoints(stompRegistry);
        verify(stompRegistry).addEndpoint("/ws-notifications");

        MessageBrokerRegistry brokerRegistry = mock(MessageBrokerRegistry.class, RETURNS_DEEP_STUBS);
        config.configureMessageBroker(brokerRegistry);
        verify(brokerRegistry).enableSimpleBroker("/topic", "/user");
        verify(brokerRegistry).setApplicationDestinationPrefixes("/app");
        verify(brokerRegistry).setUserDestinationPrefix("/user");
    }

    @Test
    void securityConfig_buildsFilterChain() throws Exception {
        HttpSecurity http = mock(HttpSecurity.class, RETURNS_DEEP_STUBS);
        SecurityFilterChain chain = new SecurityConfig().filterChain(http);
        assertNotNull(chain);
    }

    @Test
    void schedulerConfig_instantiable() {
        assertNotNull(new SchedulerConfig());
    }

    @Test
    void rabbitListenerConfig_buildsFactory() {
        ConnectionFactory cf = mock(ConnectionFactory.class);
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        SimpleRabbitListenerContainerFactory factory =
                new RabbitMQListenerConfig().rabbitListenerContainerFactory(cf, converter);
        assertNotNull(factory);
    }

    @Test
    void rabbitMQConfig_beans() {
        RabbitMQConfig config = new RabbitMQConfig();
        ReflectionTestUtils.setField(config, "exchange", "notification.exchange");
        ReflectionTestUtils.setField(config, "dlx", "notification.dlx");
        ReflectionTestUtils.setField(config, "authQueue", "notification.auth.queue");
        ReflectionTestUtils.setField(config, "hangoutQueue", "notification.hangout.queue");
        ReflectionTestUtils.setField(config, "chatQueue", "notification.chat.queue");
        ReflectionTestUtils.setField(config, "eventQueue", "notification.event.queue");
        ReflectionTestUtils.setField(config, "matchingQueue", "notification.matching.queue");
        ReflectionTestUtils.setField(config, "achievementQueue", "notification.achievement.queue");
        ReflectionTestUtils.setField(config, "geolocationQueue", "notification.geolocation.queue");
        ReflectionTestUtils.setField(config, "profileQueue", "notification.profile.queue");
        ReflectionTestUtils.setField(config, "memberQueue", "notification.member.queue");

        TopicExchange exchange = config.notificationExchange();
        assertEquals("notification.exchange", exchange.getName());
        DirectExchange dlx = config.deadLetterExchange();
        assertEquals("notification.dlx", dlx.getName());

        assertQueue(config.authQueue(), "notification.auth.queue");
        assertQueue(config.hangoutQueue(), "notification.hangout.queue");
        assertQueue(config.chatQueue(), "notification.chat.queue");
        assertQueue(config.eventQueue(), "notification.event.queue");
        assertQueue(config.matchingQueue(), "notification.matching.queue");
        assertQueue(config.achievementQueue(), "notification.achievement.queue");
        assertQueue(config.geolocationQueue(), "notification.geolocation.queue");
        assertQueue(config.profileQueue(), "notification.profile.queue");
        assertQueue(config.memberQueue(), "notification.member.queue");

        assertBinding(config.authBinding(), "auth.#");
        assertBinding(config.hangoutBinding(), "hangout.#");
        assertBinding(config.chatBinding(), "chat.#");
        assertBinding(config.eventBinding(), "event.#");
        assertBinding(config.matchingBinding(), "matching.#");
        assertBinding(config.achievementBinding(), "achievement.#");
        assertBinding(config.geolocationBinding(), "geolocation.#");
        assertBinding(config.profileBinding(), "profile.#");
        assertBinding(config.memberBinding(), "member.#");

        assertNotNull(config.messageConverter());

        ConnectionFactory cf = mock(ConnectionFactory.class);
        RabbitTemplate template = config.rabbitTemplate(cf);
        assertNotNull(template);
        assertInstanceOf(Jackson2JsonMessageConverter.class, template.getMessageConverter());
    }

    private void assertQueue(Queue queue, String name) {
        assertEquals(name, queue.getName());
        assertTrue(queue.isDurable());
        assertEquals("notification.dlx", queue.getArguments().get("x-dead-letter-exchange"));
        assertEquals(name + ".dlq", queue.getArguments().get("x-dead-letter-routing-key"));
    }

    private void assertBinding(Binding binding, String routingKey) {
        assertEquals(routingKey, binding.getRoutingKey());
        assertEquals("notification.exchange", binding.getExchange());
    }
}

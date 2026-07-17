package com.alphaeci.notification.infrastructure.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.dlx}")
    private String dlx;

    @Value("${rabbitmq.queues.auth}")
    private String authQueue;

    @Value("${rabbitmq.queues.hangout}")
    private String hangoutQueue;

    @Value("${rabbitmq.queues.chat}")
    private String chatQueue;

    @Value("${rabbitmq.queues.event}")
    private String eventQueue;

    @Value("${rabbitmq.queues.matching}")
    private String matchingQueue;

    @Value("${rabbitmq.queues.achievement}")
    private String achievementQueue;

    @Value("${rabbitmq.queues.geolocation}")
    private String geolocationQueue;

    @Value("${rabbitmq.queues.profile}")
    private String profileQueue;

    @Value("${rabbitmq.queues.member}")
    private String memberQueue;

    @Value("${rabbitmq.queues.eventBroadcast}")
    private String eventBroadcastQueue;

    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(exchange);
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(dlx);
    }

    @Bean
    public Queue authQueue() { return buildQueue(authQueue); }

    @Bean
    public Queue hangoutQueue() { return buildQueue(hangoutQueue); }

    @Bean
    public Queue chatQueue() { return buildQueue(chatQueue); }

    @Bean
    public Queue eventQueue() { return buildQueue(eventQueue); }

    @Bean
    public Queue matchingQueue() { return buildQueue(matchingQueue); }

    @Bean
    public Queue achievementQueue() { return buildQueue(achievementQueue); }

    @Bean
    public Queue geolocationQueue() { return buildQueue(geolocationQueue); }

    @Bean
    public Queue profileQueue() { return buildQueue(profileQueue); }

    @Bean
    public Queue memberQueue() { return buildQueue(memberQueue); }

    /**
     * Distinta de eventQueue (event.#): esta es la publicidad de un evento
     * nuevo a todos los dispositivos, no un recordatorio a un usuario
     * puntual, por eso vive bajo un routing key separado (broadcast.event.#)
     * que no colisiona con la binding existente.
     */
    @Bean
    public Queue eventBroadcastQueue() { return buildQueue(eventBroadcastQueue); }

    @Bean
    public Binding authBinding() { return bind(authQueue(), "auth.#"); }

    @Bean
    public Binding hangoutBinding() { return bind(hangoutQueue(), "hangout.#"); }

    @Bean
    public Binding chatBinding() { return bind(chatQueue(), "chat.#"); }

    @Bean
    public Binding eventBinding() { return bind(eventQueue(), "event.#"); }

    @Bean
    public Binding matchingBinding() { return bind(matchingQueue(), "matching.#"); }

    @Bean
    public Binding achievementBinding() { return bind(achievementQueue(), "achievement.#"); }

    @Bean
    public Binding geolocationBinding() { return bind(geolocationQueue(), "geolocation.#"); }

    @Bean
    public Binding profileBinding() { return bind(profileQueue(), "profile.#"); }

    @Bean
    public Binding memberBinding() { return bind(memberQueue(), "member.#"); }

    @Bean
    public Binding eventBroadcastBinding() {
        return bind(eventBroadcastQueue(), "broadcast.event.#");
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }

    private Queue buildQueue(String name) {
        return QueueBuilder.durable(name)
                .withArgument("x-dead-letter-exchange", dlx)
                .withArgument("x-dead-letter-routing-key", name + ".dlq")
                .build();
    }

    private Binding bind(Queue queue, String routingKey) {
        return BindingBuilder.bind(queue).to(notificationExchange()).with(routingKey);
    }
}

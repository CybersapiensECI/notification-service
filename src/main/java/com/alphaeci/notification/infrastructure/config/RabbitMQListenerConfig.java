package com.alphaeci.notification.infrastructure.config;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQListenerConfig {

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter converter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(converter);
        // Si el handler lanza excepcion, NO reencolar el mensaje: se enruta al DLX
        // (o se descarta) en lugar de reintentarse en bucle. Evita la tormenta de
        // reintentos que satura el SMTP de Gmail y dispara "454 Too many login attempts".
        factory.setDefaultRequeueRejected(false);
        return factory;
    }
}

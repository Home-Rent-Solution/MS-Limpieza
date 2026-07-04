package com.HomeRentSolution.ms_limpieza.config;

import feign.Request;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AppConfig {
    @Bean
    public Request.Options feignOptions() {
        return new Request.Options(5000, 10000);
    }

    public static final String LIMPIEZAS_EXCHANGE = "limpiezas.exchange";
    public static final String RESERVAS_EXCHANGE = "reservas.exchange"; // NUEVO
    public static final String RESERVA_CREADA_QUEUE = "limpiezas.reserva-creada.queue";
    public static final String RESERVA_CANCELADA_QUEUE = "limpiezas.reserva-cancelada.queue";
    public static final String ROUTING_ESTADO_CAMBIADO = "limpieza.estado.cambiado";

    @Bean
    public TopicExchange limpiezasExchange() {
        return new TopicExchange(LIMPIEZAS_EXCHANGE);
    }

    @Bean
    public TopicExchange reservasExchange() {
        return new TopicExchange(RESERVAS_EXCHANGE);
    }

    @Bean
    public Queue reservaCreadaLimpiezaQueue() {
        return new Queue(RESERVA_CREADA_QUEUE, true);
    }

    @Bean
    public Queue reservaCanceladaLimpiezaQueue() {
        return new Queue(RESERVA_CANCELADA_QUEUE, true);
    }

    @Bean
    public Binding bindingReservaCreada(Queue reservaCreadaLimpiezaQueue, TopicExchange reservasExchange) {
        return BindingBuilder.bind(reservaCreadaLimpiezaQueue).to(reservasExchange).with("reserva.creada");
    }

    @Bean
    public Binding bindingReservaCancelada(Queue reservaCanceladaLimpiezaQueue, TopicExchange reservasExchange) {
        return BindingBuilder.bind(reservaCanceladaLimpiezaQueue).to(reservasExchange).with("reserva.cancelada");
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}

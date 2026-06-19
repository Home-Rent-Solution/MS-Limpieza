package com.HomeRentSolution.ms_limpieza.config;

import feign.Request;
import org.springframework.a
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Queue;

@Configuration
public class AppConfig {
    @Bean
    public Request.Options feignOptions() {
        return new Request.Options(5000, 10000); // Tiempos de espera Feign idénticos
    }

    public static final String LIMPIEZAS_EXCHANGE = "limpiezas.exchange";
    public static final String RESERVA_CREADA_LIMPIEZA_QUEUE = "limpiezas.reserva-creada.queue";
    public static final String RESERVA_CANCELADA_LIMPIEZA_QUEUE = "limpiezas.reserva-cancelada.queue";

    public static final String ROUTING_ESTADO_CAMBIADO = "limpieza.estado.cambiado";

    @Bean
    public TopicExchange limpiezasExchange() {
        return new TopicExchange(LIMPIEZAS_EXCHANGE);
    }

    @Bean
    public Queue reservaCreadaLimpiezaQueue() {
        return new Queue(RESERVA_CREADA_LIMPIEZA_QUEUE, true);
    }

    @Bean
    public Queue reservaCanceladaLimpiezaQueue() {
        return new Queue(RESERVA_CANCELADA_LIMPIEZA_QUEUE, true);
    }

    @Bean
    public Binding bindingReservaCreada(Queue reservaCreadaLimpiezaQueue, TopicExchange limpiezasExchange) {
        return BindingBuilder.bind(reservaCreadaLimpiezaQueue).to(limpiezasExchange).with("reserva.creada");
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}

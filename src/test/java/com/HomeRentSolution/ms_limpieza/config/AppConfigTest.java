package com.HomeRentSolution.ms_limpieza.config;

import feign.Request;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

import static org.junit.jupiter.api.Assertions.*;

class AppConfigTest {

    private final AppConfig appConfig = new AppConfig();

    @Test
    void feignOptionsDebeCrearOpciones() {
        Request.Options options = appConfig.feignOptions();

        assertNotNull(options);
    }

    @Test
    void limpiezasExchangeDebeUsarNombreCorrecto() {
        TopicExchange exchange = appConfig.limpiezasExchange();

        assertEquals(AppConfig.LIMPIEZAS_EXCHANGE, exchange.getName());
        assertTrue(exchange.isDurable());
    }

    @Test
    void reservaCreadaQueueDebeUsarNombreCorrecto() {
        Queue queue = appConfig.reservaCreadaLimpiezaQueue();

        assertEquals(AppConfig.RESERVA_CREADA_QUEUE, queue.getName());
        assertTrue(queue.isDurable());
    }

    @Test
    void reservaCanceladaQueueDebeUsarNombreCorrecto() {
        Queue queue = appConfig.reservaCanceladaLimpiezaQueue();

        assertEquals(AppConfig.RESERVA_CANCELADA_QUEUE, queue.getName());
        assertTrue(queue.isDurable());
    }

    @Test
    void bindingReservaCreadaDebeCrearse() {
        Queue queue = appConfig.reservaCreadaLimpiezaQueue();
        TopicExchange exchange = appConfig.limpiezasExchange();

        Binding binding = appConfig.bindingReservaCreada(queue, exchange);

        assertNotNull(binding);
        assertEquals(AppConfig.RESERVA_CREADA_QUEUE, binding.getDestination());
        assertEquals(AppConfig.LIMPIEZAS_EXCHANGE, binding.getExchange());
        assertEquals("reserva.creada", binding.getRoutingKey());
    }

    @Test
    void bindingReservaCanceladaDebeCrearse() {
        Queue queue = appConfig.reservaCanceladaLimpiezaQueue();
        TopicExchange exchange = appConfig.limpiezasExchange();

        Binding binding = appConfig.bindingReservaCancelada(queue, exchange);

        assertNotNull(binding);
        assertEquals(AppConfig.RESERVA_CANCELADA_QUEUE, binding.getDestination());
        assertEquals(AppConfig.LIMPIEZAS_EXCHANGE, binding.getExchange());
        assertEquals("reserva.cancelada", binding.getRoutingKey());
    }

    @Test
    void messageConverterDebeCrearse() {
        Jackson2JsonMessageConverter converter = appConfig.messageConverter();

        assertNotNull(converter);
    }
}
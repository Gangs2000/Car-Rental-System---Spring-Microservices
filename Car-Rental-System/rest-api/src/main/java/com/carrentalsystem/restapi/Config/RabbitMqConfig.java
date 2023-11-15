package com.carrentalsystem.restapi.Config;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    public static final String QUEUE1="car-rental-system-booking-trigger", QUEUE2="car-rental-system-cancellation-trigger", QUEUE3="car-rental-system-password-reset-trigger";
    public static final String EXCHANGE="car-rental-system-email-service";
    public static final String ROUTING_KEY1="booking-confirmation", ROUTING_KEY2="cancellation-confirmation", ROUTING_KEY3="password-reset";

    @Bean
    public Queue queue1(){
        return new Queue(QUEUE1);
    }

    @Bean
    public Queue queue2(){
        return new Queue(QUEUE2);
    }

    @Bean
    public Queue queue3(){
        return new Queue(QUEUE3);
    }

    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding binding1(TopicExchange topicExchange){
        return BindingBuilder.bind(queue1()).to(topicExchange).with(ROUTING_KEY1);
    }

    @Bean
    public Binding binding2(TopicExchange topicExchange){
        return BindingBuilder.bind(queue2()).to(topicExchange).with(ROUTING_KEY2);
    }

    @Bean
    public Binding binding3(TopicExchange topicExchange){
        return BindingBuilder.bind(queue3()).to(topicExchange).with(ROUTING_KEY3);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate=new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
        return rabbitTemplate;
    }
}

package com.red.domovie.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Configuration
@EnableRabbit
@Slf4j
public class RabbitMQConfig {
	// 메세지 브로커
	
	//스프링 부트의 자동 구성 기능을 통해 RabbitMQ와 연동할 수 있도록 설정됨.
	//스프링 부트의 자동 구성은 클래스 경로에 필요한 라이브러리와 설정이 감지되면 해당 설정을 자동으로 수행하여 빈을 생성 함.
	private final ConnectionFactory connectionFactory;
	
	@Value("${spring.rabbitmq.template.default-receive-queue}")
	private String queue;
	@Value("${spring.rabbitmq.template.exchange}")
	private String exchange;
	@Value("${spring.rabbitmq.template.routing-key}")
	private String routingKey;
	
	// RabbitMQ에서 사용할 큐를 정의함
    // 큐 이름은 properties에서 주입된 값이며, durable=false로 설정하여 비휘발성 큐가 아님
	@Bean
	Queue queue() {
		return new Queue(queue, false);
	}
	
	//"topic" 유형의 RabbitMQ Exchange를 정의
	@Bean
	TopicExchange exchange() {
		return new TopicExchange(exchange);
	}
	
	// 큐와 익스체인지 간의 바인딩을 정의
	// 특정 라우팅 키를 사용하여 큐를 익스체인지에 바인딩
	@Bean
	Binding binding() {
		return BindingBuilder.bind(queue()).to(exchange()).with(routingKey);
	}
	
	/*
	//RabbitMQ 메시지 리스너 컨테이너 팩토리를 정의
	//@Bean
    SimpleRabbitListenerContainerFactory myFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        //ConnectionFactory connectionFactory = getCustomConnectionFactory();
        configurer.configure(factory, connectionFactory);
        //factory.setMessageConverter(new MyMessageConverter());
        return factory;
    }
    */
	
	//RabbitMQ 메시지 리스너 컨테이너를 정의
	@Bean
    SimpleMessageListenerContainer container(Receiver receiver) {
		log.debug(">>>>>>connectionFactory:"+connectionFactory);
		SimpleMessageListenerContainer container=new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(queue);
		container.setMessageListener(messageListenerAdapter(receiver));
		return container;
	}
	
	
	// 메시지 리스너 어댑터를 정의
	// 메시지를 수신했을 때 receiver 객체의 "receiveMessage" 메서드를 호출함
	@Bean
	MessageListenerAdapter messageListenerAdapter(Receiver receiver) {
		//receiver 객체와 receiveMessage 메소드를 사용하여 메시지 리스너 어댑터를 설정
		MessageListenerAdapter messageListenerAdapter=new MessageListenerAdapter(receiver, "receiveMessage");
		messageListenerAdapter.setMessageConverter(messageConverter());
		return messageListenerAdapter;
	}
	
	//RabbitMQ 메시지 변환기를 정의
	// 메시지를 JSON 형식으로 변환하는 Jackson2JsonMessageConverter를 사용
	@Bean
	MessageConverter messageConverter() {
		return new Jackson2JsonMessageConverter();
	}
	
}

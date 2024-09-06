package com.red.domovie.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableRabbit
public class RabbitMQConfig {
	
	// 메세지 브로커
	//스프링 부트의 자동 구성 기능을 통해 RabbitMQ와 연동할 수 있도록 설정됨.
	//스프링 부트의 자동 구성은 클래스 경로에 필요한 라이브러리와 설정이 감지되면 해당 설정을 자동으로 수행하여 빈을 생성 함.
	private final ConnectionFactory connectionFactory;
	
	/*
	@Value("${spring.rabbitmq.template.default-receive-queue}")
	private String queue;
	@Value("${spring.rabbitmq.template.exchange}")
	private String exchange;
	@Value("${spring.rabbitmq.template.routing-key}")
	private String routingKey;
	
	
	// RabbitMQ에서 사용할 큐를 정의함
	@Bean
	Queue queue() {
		return new Queue(queue, false); // 큐를 durable로 설정하여 서버 재시작 후에도 큐가 유지되도록
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
	
	
	//RabbitMQ 메시지 리스너 컨테이너 팩토리를 정의
	@Bean
    SimpleRabbitListenerContainerFactory myFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        return factory;
    }
	
	//RabbitMQ 메시지 리스너 컨테이너를 정의
	@Bean
    SimpleMessageListenerContainer container(Receiver receiver) {
		SimpleMessageListenerContainer container=myFactory().createListenerContainer();
		container.setQueueNames(queue);
		container.setMessageListener(messageListenerAdapter(receiver)); // 메시지 리스너 어댑터 설정
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
	*/
	
	//RabbitMQ 메시지를 수신하기 위한 리스너 컨테이너를 생성하는 데 사용되는 Bean
	//RabbitMQ와의 통신에 필요한 리스너 컨테이너를 구성하고 관리할 수 있음.
	//1. 리스너 컨테이너의 스레드 관리를 담당합니다. 이를 통해 리스너의 동작을 제어하고, 스레드 풀을 관리하여 메시지 처리의 성능과 확장성을 조절할 수 있음
	//2. 커스텀한 설정을 적용할 수 있습니다. 예를 들어, 메시지 변환기(MessageConverter), Acknowledge 모드, 쓰레드 풀 사이즈 등을 설정할 수 있습니다. 이를 통해 다양한 요구사항에 맞게 리스너 컨테이너를 조정할 수 있음
	//3. 여러 개의 리스너를 동시에 처리할 수 있는 컨테이너를 생성할 수 있습니다. 각 리스너는 별도의 설정을 가질 수 있으며, SimpleRabbitListenerContainerFactory를 사용하여 각 리스너의 독립적인 환경을 구성할 수 있음
	@Bean
	SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory() {
		SimpleRabbitListenerContainerFactory factory=new SimpleRabbitListenerContainerFactory();
		System.out.println(">>>>:"+connectionFactory);
		factory.setConnectionFactory(connectionFactory);
		factory.setMessageConverter(messageConverter());
		return factory;
	}
	
	
	//RabbitMQ 메시지 변환기를 정의
	// 메시지를 JSON 형식으로 변환하는 Jackson2JsonMessageConverter를 사용
	@Bean
	MessageConverter messageConverter() {
		return new Jackson2JsonMessageConverter();
	}
	
	
}

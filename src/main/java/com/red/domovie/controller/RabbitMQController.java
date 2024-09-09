package com.red.domovie.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.red.domovie.service.impl.RabbitMQServiceProcess;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RabbitMQController {
	
	private final RabbitMQServiceProcess rabbitMQService;

    /**
     * 메시지 큐 생성 엔드포인트
     *
     * @param queueName 생성할 메시지 큐의 이름
     * @return 생성 결과 메시지
     */
    @PostMapping("/rabbit/queues/{branchName}")
    public String createQueue(@PathVariable String branchName) {
        rabbitMQService.createQueue(branchName);
        return "Queue " + branchName + " created successfully!";
    }

    /**
     * 익스체인지 생성 엔드포인트
     *
     * @param exchangeName 생성할 익스체인지의 이름
     * @return 생성 결과 메시지
     */
    @PostMapping("/rabbit/exchanges/{branchName}")
    public String createExchange(@PathVariable String branchName) {
        rabbitMQService.createExchange(branchName);
        return "Exchange " + branchName + " created successfully!";
    }

    /**
     * 큐와 익스체인지 바인딩 엔드포인트
     *
     * @param bindingInfo 바인딩에 필요한 정보를 담은 DTO 객체
     * @return 바인딩 결과 메시지
     */
    @PostMapping("/rabbit/bindings/{branchName}")
    public String bindQueueToExchange(@PathVariable String branchName) {
        rabbitMQService.bindQueueToExchange(branchName);
        return "Queue " + branchName+".queue " + " bound to Exchange " + branchName + " with routing key " + branchName+".#";
    }


}

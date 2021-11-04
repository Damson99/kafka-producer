package com.cloud.kafkaproducer.web.v1.controller;

import com.cloud.kafkaproducer.service.KafkaProducerService;
import com.cloud.kafkaproducer.web.v1.model.IncomingMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/kafka")
public class KafkaMessageController {

    @Autowired
    KafkaProducerService kafkaProducerService;


//    @PostMapping
//    public String sendMessageToKafka(@RequestBody IncomingMessage incomingMessage)
//    {
//        kafkaProducerService.sendMessage(incomingMessage.getTopic(), incomingMessage.getKey(),
//                incomingMessage.getValue());
//        return String.format("message sent to kafka topic. topic: %s | key: %s | value: %s", incomingMessage.getTopic(),
//                incomingMessage.getKey(), incomingMessage.getValue());
//    }
}

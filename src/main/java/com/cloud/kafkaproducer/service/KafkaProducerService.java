package com.cloud.kafkaproducer.service;

import com.cloud.kafkaproducer.web.v1.model.PosInvoice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaProducerService {

    @Value("${application.configs.topic.name}")
    private String KAFKA_TOPIC;

    @Autowired
    private KafkaTemplate<String, PosInvoice> kafkaTemplate;

    public void sendMessage(PosInvoice posInvoice)
    {
        log.info(String.format("Producing Invoice No: %s", posInvoice));
        kafkaTemplate.send(KAFKA_TOPIC, posInvoice.getStoreID(), posInvoice);
    }
}

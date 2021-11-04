package com.cloud.kafkaproducer.bootstrap;

import com.cloud.kafkaproducer.service.KafkaProducerService;
import com.cloud.kafkaproducer.service.datagenerator.InvoiceGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class KafkaDataLoader implements ApplicationRunner {

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private InvoiceGenerator invoiceGenerator;

    @Value("${application.configs.invoice.count}")
    private int INVOICE_LOOP_COUNT;

    @Override
    public void run(ApplicationArguments args) throws Exception{
        for(int i=0; i<INVOICE_LOOP_COUNT; i++){
            kafkaProducerService.sendMessage(invoiceGenerator.getNextInvoice());
            Thread.sleep(200);
        }

    }
}

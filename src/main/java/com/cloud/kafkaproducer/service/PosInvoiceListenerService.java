package com.cloud.kafkaproducer.service;

import com.cloud.kafkaproducer.web.v1.model.HadoopRecord;
import com.cloud.kafkaproducer.web.v1.model.Notification;
import com.cloud.kafkaproducer.web.v1.model.PosInvoice;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@EnableBinding
public class PosInvoiceListenerService {

    private static final String PRIME="PRIME";

    @Autowired
    RecordBuilderService recordBuilderService;

    @StreamListener("pos-invoice-input-channel")
    public void process(KStream<String, PosInvoice> inputStream){
        KStream<String, HadoopRecord> hadoopRecordKStream = inputStream
                .mapValues(recordBuilderService::getMaskedPosInvoice)
                .flatMapValues(recordBuilderService::getHadoopRecords);

        KStream<String, Notification> notificationKStream = inputStream
                .filter((key, value) -> value.getCustomerType().equals(PRIME))
                .mapValues(recordBuilderService::getPosInvoiceNotification);

        hadoopRecordKStream.foreach((key, value) ->
                log.info(String.format("Hadoop Record: Key: %s | Value: %s", key, value)));
        notificationKStream.foreach((key, value) ->
                log.info(String.format("Hadoop Record: Key: %s | Value: %s", key, value)));

        hadoopRecordKStream.to("hadoop-sink-topic");
        notificationKStream.to("loyalty-topic");
    }

}

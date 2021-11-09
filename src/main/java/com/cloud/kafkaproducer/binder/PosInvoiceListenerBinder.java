package com.cloud.kafkaproducer.binder;

import com.cloud.kafkaproducer.web.v1.model.PosInvoice;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;

public interface PosInvoiceListenerBinder {

    @Input("pos-invoice-input-channel")
    KStream<String, PosInvoice> posInvoiceInputStream();

}

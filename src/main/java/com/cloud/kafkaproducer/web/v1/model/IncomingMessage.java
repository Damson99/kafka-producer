package com.cloud.kafkaproducer.web.v1.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class IncomingMessage implements Serializable {
    private String topic;
    private String key;
    private String value;
}

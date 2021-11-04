package com.cloud.kafkaproducer.service.datagenerator;

import com.cloud.kafkaproducer.web.v1.model.DeliveryAddress;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Random;

@Service
public class AddressGenerator {
    private final Random random;
    private final DeliveryAddress[] addresses;

    public AddressGenerator(){
        final String DATAFILE="src/main/resources/data/address.json";
        final ObjectMapper objectMapper = new ObjectMapper();
        random = new Random();
        try {
            addresses = objectMapper.readValue(new File(DATAFILE), DeliveryAddress[].class);
        } catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public DeliveryAddress getNextAddress(){
        return addresses[getIndex()];
    }

    private int getIndex() {
        return random.nextInt(100);
    }
}

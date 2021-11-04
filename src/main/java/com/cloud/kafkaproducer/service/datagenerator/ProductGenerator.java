package com.cloud.kafkaproducer.service.datagenerator;

import com.cloud.kafkaproducer.web.v1.model.LineItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Random;

@Service
public class ProductGenerator {
    private final Random random;
    private final Random qty;
    private final LineItem[] products;

    public ProductGenerator()
    {
        String DATAFILE = "src/main/resources/data/products.json";
        ObjectMapper objectMapper = new ObjectMapper();
        random = new Random();
        qty = new Random();
        try{
            products = objectMapper.readValue(new File(DATAFILE), LineItem[].class);
        }
        catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public LineItem getNextProduct(){
        LineItem lineItem = products[getIndex()];
        lineItem.setItemQty(getQuantity());
        lineItem.setTotalValue(lineItem.getItemPrice()*lineItem.getItemQty());
        return lineItem;
    }

    private int getIndex() {
        return random.nextInt(100);
    }

    private int getQuantity() {
        return random.nextInt(2) + 1;
    }
}

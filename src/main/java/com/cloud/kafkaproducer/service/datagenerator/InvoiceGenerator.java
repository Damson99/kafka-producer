package com.cloud.kafkaproducer.service.datagenerator;

import com.cloud.kafkaproducer.web.v1.model.DeliveryAddress;
import com.cloud.kafkaproducer.web.v1.model.LineItem;
import com.cloud.kafkaproducer.web.v1.model.PosInvoice;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class InvoiceGenerator {
    private final Random invoiceIndex;
    private final Random invoiceNumber;
    private final Random numberOfItems;
    private final PosInvoice[] invoices;

    @Autowired
    private AddressGenerator addressGenerator;

    @Autowired
    private ProductGenerator productGenerator;

    public InvoiceGenerator()
    {
        String DATAFILE = "src/main/resources/data/invoice.json";
        invoiceIndex = new Random();
        invoiceNumber = new Random();
        numberOfItems = new Random();
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            invoices = objectMapper.readValue(new File(DATAFILE), PosInvoice[].class);
        }
        catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }


    public PosInvoice getNextInvoice(){
        PosInvoice posInvoice = invoices[getIndex()];
        posInvoice.setInvoiceNumber(Integer.toString(getNewInvoiceNumber()));
        posInvoice.setCreatedTime(System.currentTimeMillis());
        if("HOME-DELIVERY".equalsIgnoreCase(posInvoice.getDeliveryType())){
            DeliveryAddress deliveryAddress = addressGenerator.getNextAddress();
            posInvoice.setDeliveryAddress(deliveryAddress);
        }
        int itemCount = getNoOfItem();
        double totalAmount = 0.0;
        List<LineItem> items = new ArrayList<>();
        for(int i = 0; i < itemCount; i++){
            LineItem item = productGenerator.getNextProduct();
            totalAmount = totalAmount + item.getTotalValue();
            items.add(item);
        }
        posInvoice.setNumberOfItems(itemCount);
        posInvoice.setInvoiceLineItem(items);
        posInvoice.setTotalAmount(totalAmount);
        posInvoice.setTaxableAmount(totalAmount);
        posInvoice.setCGST(totalAmount * 0.025);
        posInvoice.setSGST(totalAmount * 0.025);
        posInvoice.setCESS(totalAmount * 0.00125);
        log.info(posInvoice.toString());
        return posInvoice;
    }

    private int getNoOfItem(){
        return numberOfItems.nextInt(4) + 1;
    }

    private int getNewInvoiceNumber(){
        return invoiceNumber.nextInt(99999999) + 99999;
    }

    private int getIndex(){
        return invoiceIndex.nextInt(100);
    }
}

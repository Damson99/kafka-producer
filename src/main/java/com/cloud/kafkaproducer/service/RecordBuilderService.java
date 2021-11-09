package com.cloud.kafkaproducer.service;

import com.cloud.kafkaproducer.web.v1.model.HadoopRecord;
import com.cloud.kafkaproducer.web.v1.model.Notification;
import com.cloud.kafkaproducer.web.v1.model.PosInvoice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RecordBuilderService
{
    private static final String HOME_DELIVERY="HOME_DELIVERY";

    public Notification getPosInvoiceNotification(PosInvoice posInvoice){
        return Notification.newBuilder()
                .setInvoiceNumber(posInvoice.getInvoiceNumber())
                .setCustomerCardNo(posInvoice.getCustomerCardNo())
                .setTotalAmount(posInvoice.getTotalAmount())
                .setEarnedLoyaltyPoints(posInvoice.getTotalAmount()*0.02)
                .build();
    }

    public PosInvoice getMaskedPosInvoice(PosInvoice posInvoice){
        posInvoice.setCustomerCardNo(null);
        if(posInvoice.getDeliveryType().equalsIgnoreCase(HOME_DELIVERY)){
            posInvoice.getDeliveryAddress().setAddressLine(null);
            posInvoice.getDeliveryAddress().setContactNumber(null);
        }

        return posInvoice;
    }

    public List<HadoopRecord> getHadoopRecords(PosInvoice posInvoice)
    {
        return posInvoice.getInvoiceLineItems()
                .parallelStream().map(line -> {
                    HadoopRecord hadoopRecord =
                            new HadoopRecord();
                    hadoopRecord    .setInvoiceNumber(posInvoice.getInvoiceNumber());
                    hadoopRecord    .setCreatedTime(posInvoice.getCreatedTime());
                    hadoopRecord    .setStoreID(posInvoice.getStoreID());
                    hadoopRecord    .setPosID(posInvoice.getStoreID());
                    hadoopRecord    .setCustomerType(posInvoice.getCustomerType());
                    hadoopRecord    .setPaymentMethod(posInvoice.getPaymentMethod());
                    hadoopRecord    .setDeliveryType(posInvoice.getDeliveryType());
                    hadoopRecord    .setItemCode(line.getItemCode());
                    hadoopRecord    .setItemDescription(line.getItemDescription());
                    hadoopRecord    .setItemPrice(line.getItemPrice());
                    hadoopRecord    .setItemQty(line.getItemQty());
                    hadoopRecord    .setTotalValue(line.getTotalValue());

                    if(posInvoice.getDeliveryType().equalsIgnoreCase(HOME_DELIVERY)){
                        hadoopRecord.setCity(posInvoice.getDeliveryAddress().getCity());
                        hadoopRecord.setState(posInvoice.getDeliveryAddress().getState());
                        hadoopRecord.setPinCode(posInvoice.getDeliveryAddress().getPinCode());
                    }
                    return hadoopRecord;
                }).collect(Collectors.toList());
    }
}

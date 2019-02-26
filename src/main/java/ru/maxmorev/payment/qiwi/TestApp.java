package ru.maxmorev.payment.qiwi;

import org.springframework.web.client.RestClientException;
import ru.maxmorev.payment.qiwi.response.Payment;

import java.util.List;

public class TestApp {


    public static void main(String... args) {

        String token = "";//your qiwi token token
        String phone = "79263.."; //your qiwi wallet

        System.out.println("2. TEST QIWI API GET BALANCE RU");

        QIWI qiwi = null;
        try{
            qiwi = new QIWI( phone, token);
        }
        catch (RestClientException ex){
            System.out.println("Probably error in phone or token: " + ex.getMessage());
        }

        if(qiwi!=null && qiwi.isConnected()) {

            System.out.println("QIWI BALCNCE: " + qiwi.getBalanceRU());


            List<Payment> payments = null;
            try{
                payments = qiwi.getPaymentsLast(3);
            }catch (RestClientException ex){
                System.out.println("Error in REST" + ex.getMessage());
            }
            if(payments!=null) {
                System.out.println("Payment list size: " + payments.size());
                for (Payment pay : payments) {
                    System.out.println(pay.toString());
                }
            }
        }


    }


}

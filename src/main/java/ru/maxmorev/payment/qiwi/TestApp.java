package ru.maxmorev.payment.qiwi;

import ru.maxmorev.payment.qiwi.response.Payment;

import java.util.List;

public class TestApp {


    public static void main(String... args) {

        String token = "";//your qiwi token token
        String phone = ""; //your qiwi wallet

        QIWI qiwi = new QIWI( phone, token);

        System.out.println("2. TEST QIWI API GET BALANCE RU");

        System.out.println(qiwi.getBalanceRU());


        List<Payment> payments = qiwi.getPaymentsLast(3);

        for (Payment pay : payments) {
            System.out.println(pay.toString());
        }


    }


}

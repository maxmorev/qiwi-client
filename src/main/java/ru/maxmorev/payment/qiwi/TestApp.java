package ru.maxmorev.payment.qiwi;

import ru.maxmorev.payment.qiwi.response.Payment;
import ru.maxmorev.payment.qiwi.response.PaymentHistory;
import ru.maxmorev.payment.qiwi.response.State;
import ru.maxmorev.payment.qiwi.response.TransferResponse;

import java.net.UnknownHostException;
import java.util.List;

public class TestApp {


    public static void main(String... args) throws ResponeParsingQiwiException, UnknownHostException, AuthorizationQiwiException {

        String token = "";//your qiwi token token
        String phone = "7926..."; //your qiwi wallet
        System.out.println("TEST QIWI API");

        QIWI qiwi = new QIWI( phone, token);

        double balanceRub = qiwi.getBalanceRub();
        System.out.println("Balance: " + balanceRub);


        List<Payment> paymentList = qiwi.getPaymentsHistory(20, PaymentHistory.PaymentType.INDEFERENT);
        System.out.println("Payments History size: " + paymentList.size());

        TransferResponse tr = qiwi.transferToWallet("79263926369", 100.0d, "Thank you");
        if(tr.getTransaction().getState().getCode()==State.CODE_ACCEPTED){
            System.out.println( "id of accepted transaction " + tr.getTransaction().getId() );
        }
        System.out.println(tr);

    }


}

package ru.maxmorev.payment.qiwi;


import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import ru.maxmorev.payment.qiwi.request.Transfer;
import ru.maxmorev.payment.qiwi.response.*;

import java.util.Date;
import java.util.List;
import java.util.Random;

public class QIWI {

    private String token;
    private String phone;
    private Wallet balance;
    private double balanceRU;

    private HttpHeaders requestHeaders;
    private boolean TOKENOK = false;
    private HttpEntity<?> httpEntity;
    private RestTemplate restTemplate;
    private String urlGETBalance = "https://edge.qiwi.com/funding-sources/v1/accounts/current";
    private String urlGETPaymentHistory = "https://edge.qiwi.com/payment-history/v1/persons/<79112223344>/payments?";
    private String urlPOSTPaymentTransfer = "https://edge.qiwi.com/sinap/api/v2/terms/99/payments";
    //private String urlPOSTPaymentTransfer = "http://localhost:8080/test";

    private Wallet wallet;


    /**
     * @param phone - phone number
     * @param token - secret should be secure
     */
    public QIWI(String phone, String token) {
        super();
        this.token = token;
        this.phone = phone;
        this.init();
    }

    public boolean isOk() {
        return this.TOKENOK;
    }

    private double getRandomDouble() {
        double start = 200;
        double end = 402;
        double random = new Random().nextDouble();
        return  start + (random * (end - start));
    }

    private void init() {
        //TODO TEST remove first TEST LINE
        if(this.token.equals("TEST")) {
            this.TOKENOK=true;
            this.balanceRU = this.getRandomDouble();
            System.out.println("Add "+this.phone+" random balance = "+this.balanceRU);
            return;
        }
        this.requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.set("Accept", "application/json");
        requestHeaders.set("Authorization", "Bearer "+token);
        this.httpEntity = new HttpEntity<String>(requestHeaders);
        this.restTemplate = new RestTemplate();
        this.urlGETPaymentHistory = "https://edge.qiwi.com/payment-history/v1/persons/"+this.phone+"/payments?";
        System.out.println("URL HISTORY:"+this.urlGETPaymentHistory);
        this.wallet = this.getBalance();
        if(this.wallet == null) {
            System.out.println(";( fok");
            return;
        }
        this.setBalanceRU(this.wallet.getRuBalance());
        if(this.balanceRU==-404.0) {
            return;
        }
        this.TOKENOK = true;
    }

    private Wallet getBalance() {
        System.out.println("GET BALANCE");
        System.out.println(httpEntity.getHeaders().toString());
        ResponseEntity<Wallet> resp = this.restTemplate.exchange(this.urlGETBalance, HttpMethod.GET, this.httpEntity, Wallet.class);
        this.balance = resp.getBody();
        return balance;
    }

    /*
     * Пример 1. Последние 10 платежей

        user@server:~$ curl "https://edge.qiwi.com/payment-history/v1/persons/79112223344/payments?rows=10"
          --header "Accept: application/json"
     *
     */
    private PaymentHistory getLast(int count) {
        //rows=10
        ResponseEntity<PaymentHistory> resp = this.restTemplate.exchange(this.urlGETPaymentHistory+"rows="+count, HttpMethod.GET, this.httpEntity, PaymentHistory.class);
        return resp.getBody();
    }

    public Transaction doTransferToWallet(String phoneReciver, double amount, String comment) {

        long stampInstance = new Date().getTime() ;
        // ObjectMapper mapper = new ObjectMapper();

        Transfer walletTransfer = new Transfer(String.valueOf(stampInstance), amount, comment, phoneReciver);
        HttpEntity<?> httpEntityTransfer = new HttpEntity<Transfer>(walletTransfer, this.requestHeaders);
        System.out.println("HttpEntity "+this.urlPOSTPaymentTransfer);
        System.out.println(httpEntityTransfer.getHeaders());
        System.out.println(httpEntityTransfer.getBody());

        ResponseEntity<TransferResponse> resp =  this.restTemplate.exchange(this.urlPOSTPaymentTransfer, HttpMethod.POST, httpEntityTransfer, TransferResponse.class);
        System.out.println("########RESPONSE");
        System.out.println(resp.getHeaders());
        System.out.println(resp.getBody());
        return resp.getBody().getTransaction();
    }

    /*
     *  PUBLIC METHODS 4 USER
     */

    public double getBalanceRU() {
        //TODO TEST remove next line
        if(this.token.equals("TEST")) {return this.balanceRU;}
        balanceRU = this.getBalance().getRuBalance();
        return balanceRU;
    }

    private void setBalanceRU(double balanceRU) {
        this.balanceRU = balanceRU;
    }

    public List<Payment> getPaymentsLast(int count) {
        PaymentHistory ph = this.getLast(count);
        return ph.getData();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((phone == null) ? 0 : phone.hashCode());
        result = prime * result + ((token == null) ? 0 : token.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        QIWI other = (QIWI) obj;
        if(this.token.equals(other.token) && this.phone.equals(other.phone)) { return true;} else {return false;}

    }

    public String getToken() {
        return token;
    }



    public String getPhone() {
        return phone;
    }





}
package ru.maxmorev.payment.qiwi;


import org.apache.log4j.Logger;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import ru.maxmorev.payment.qiwi.request.Transfer;
import ru.maxmorev.payment.qiwi.response.*;

import java.util.Date;
import java.util.List;


/**
 *
 * Rest JAVA QIWI API
 * @author maxmorev
 *
 */

public class QIWI {

    final static Logger logger = Logger.getLogger(QIWI.class);


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
     * @param phone - phone number 7926...
     * @param token - qiwi token
     *              Для выпуска токена выполните следующие шаги:
     *
     *     Откройте в браузере страницу https://qiwi.com/api. Для этого потребуется авторизоваться или зарегистрироваться в сервисе QIWI Кошелек.
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



    private void init() {
        this.requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.set("Accept", "application/json");
        requestHeaders.set("Authorization", "Bearer "+token);
        this.httpEntity = new HttpEntity<String>(requestHeaders);
        this.restTemplate = new RestTemplate();
        this.urlGETPaymentHistory = "https://edge.qiwi.com/payment-history/v1/persons/"+this.phone+"/payments?";
        logger.debug("URL HISTORY:"+this.urlGETPaymentHistory);
        this.wallet = this.getBalance();
        if(this.wallet == null) {
            logger.debug(";( fok");
            return;
        }
        this.setBalanceRU(this.wallet.getRuBalance());
        if(this.balanceRU==-404.0) {
            return;
        }
        this.TOKENOK = true;
    }

    private Wallet getBalance() {
        logger.debug("GET BALANCE");
        logger.debug(httpEntity.getHeaders().toString());
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
    private PaymentHistory getPaymentHistory(int count) {
        //rows=10
        ResponseEntity<PaymentHistory> resp = this.restTemplate.exchange(this.urlGETPaymentHistory+"rows="+count, HttpMethod.GET, this.httpEntity, PaymentHistory.class);
        return resp.getBody();
    }

    public Transaction transferToWallet(String phoneReciver, double amount, String comment) {

        long timeStamp = new Date().getTime() ;

        Transfer walletTransfer = new Transfer(String.valueOf(timeStamp), amount, comment, phoneReciver);
        HttpEntity<?> httpEntityTransfer = new HttpEntity<Transfer>(walletTransfer, this.requestHeaders);
        logger.debug("HttpEntity "+this.urlPOSTPaymentTransfer);
        logger.debug(httpEntityTransfer.getHeaders());
        logger.debug(httpEntityTransfer.getBody());

        ResponseEntity<TransferResponse> resp =  this.restTemplate.exchange(this.urlPOSTPaymentTransfer, HttpMethod.POST, httpEntityTransfer, TransferResponse.class);
        logger.debug("########RESPONSE");
        logger.debug(resp.getHeaders());
        logger.debug(resp.getBody());
        return resp.getBody().getTransaction();
    }

    /*
     *  PUBLIC METHODS 4 USER
     */

    public double getBalanceRU() {
        balanceRU = this.getBalance().getRuBalance();
        return balanceRU;
    }

    private void setBalanceRU(double balanceRU) {
        this.balanceRU = balanceRU;
    }

    public List<Payment> getPaymentsLast(int count) {
        PaymentHistory ph = this.getPaymentHistory(count);
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
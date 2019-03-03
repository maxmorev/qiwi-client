package ru.maxmorev.payment.qiwi;


import org.apache.log4j.Logger;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.maxmorev.payment.qiwi.request.Transfer;
import ru.maxmorev.payment.qiwi.response.*;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.util.Date;
import java.util.List;


/**
 *
 * REST JAVA QIWI API
 * @author maxmorev
 *
 */

public class QIWI {

    final static Logger logger = Logger.getLogger(QIWI.class);

    private Date date;
    private String token;
    private String phone;
    private Wallet balance;
    private double balanceRU;

    private HttpHeaders requestHeaders;
    private boolean connected = false;
    private HttpEntity<?> httpEntity;
    private RestTemplate restTemplate;
    private String urlGETBalance = "https://edge.qiwi.com/funding-sources/v1/accounts/current";
    private String urlGETPaymentHistory = "https://edge.qiwi.com/payment-history/v1/persons/<79112223344>/payments?";
    private String urlPOSTPaymentTransfer = "https://edge.qiwi.com/sinap/api/v2/terms/99/payments";
    //private String urlPOSTPaymentTransfer = "http://localhost:8080/test";

    private Wallet wallet;


    String proxyServer = "";
    String proxyPort = "";
    SocketAddress socks;// new InetSocketAddress(proxyServer, Integer.parseInt(proxyPort));
    Proxy proxy;// = new Proxy(Proxy.Type.SOCKS, socks);


    /**
     * @param phone - phone number 7926...
     * @param token - qiwi token
     *              Для выпуска токена выполните следующие шаги:
     *
     *     Откройте в браузере страницу https://qiwi.com/api. Для этого потребуется авторизоваться или зарегистрироваться в сервисе QIWI Кошелек.
     */
    public QIWI(String phone, String token)  throws RestClientException {
        super();
        this.token = token;
        this.phone = phone;
        this.login();
    }

    public QIWI( String phone, String token, String socksServer, String socksPort )  throws RestClientException {
        super();
        this.token = token;
        this.phone = phone;

        proxyServer = socksServer;
        proxyPort = socksPort;

        socks = new InetSocketAddress(proxyServer, Integer.parseInt(proxyPort));

        proxy = new Proxy(Proxy.Type.SOCKS, socks);
        System.out.println("SOCKS OPTIONS: "+ proxy.address().toString());
        this.login();
    }


    public boolean isConnected() {
        return this.connected;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    private void login()  throws RestClientException {
        this.requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.set("Accept", "application/json");
        requestHeaders.set("Authorization", "Bearer "+token);
        this.httpEntity = new HttpEntity<String>(requestHeaders);
        this.restTemplate = new RestTemplate();
        this.urlGETPaymentHistory = "https://edge.qiwi.com/payment-history/v1/persons/"+this.phone+"/payments?";
        logger.debug("URL HISTORY:"+this.urlGETPaymentHistory);
        this.wallet = this.getBalance();
        this.connected = true;
    }

    private Wallet getBalance() throws RestClientException {
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
    private PaymentHistory getPaymentHistory(int count)  throws RestClientException {
        //rows=10
        ResponseEntity<PaymentHistory> resp = this.restTemplate.exchange(this.urlGETPaymentHistory+"rows="+count, HttpMethod.GET, this.httpEntity, PaymentHistory.class);
        return resp.getBody();
    }



    /*
     *  PUBLIC METHODS 4 USER
     */

    /**
     *  https://developer.qiwi.com/en/qiwi-wallet-personal/index.html#p2p
     *
     * @param phoneReciver qiwi wallet (phone number example 792938383838) Номер телефона получателя (с международным префиксом)
     * @param amount Сумма
     * @param comment comment for payment
     * @return @Transaction
     * @throws RestClientException
     */
    public Transaction transferToWallet(String phoneReciver, double amount, String comment)  throws RestClientException  {

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


    public double getBalanceRU() throws RestClientException{
        balanceRU = this.getBalance().getBalanceRub();
        return balanceRU;
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
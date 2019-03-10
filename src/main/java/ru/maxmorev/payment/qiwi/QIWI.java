package ru.maxmorev.payment.qiwi;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import ru.maxmorev.payment.qiwi.request.Field;
import ru.maxmorev.payment.qiwi.request.WalletTransfer;
import ru.maxmorev.payment.qiwi.response.*;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Objects;


/**
 *
 * REST JAVA QIWI API
 *
 * @author maxmorev
 *
 */

public class QIWI {

    final static Logger logger = Logger.getLogger(QIWI.class);

    private String token;
    private String phone;
    private Proxy proxy;


    /**
     * To release a token, follow these steps:
     * Open the page in the browser https://qiwi.com/api
     * To do this, you will need to log in or register in the QIWI Wallet service.
     *
     * @param phone - phone number 7926...
     * @param token - qiwi token
     *
     */
    public QIWI(String phone, String token) throws ResponeParsingQiwiException, UnknownHostException, AuthorizationQiwiException {
        super();
        Objects.requireNonNull(phone, " phone must be not null");
        Objects.requireNonNull(token, " token must be not null");
        this.token = token;
        this.phone = phone;
        getBalance();
    }



    public double getBalanceRub() throws ResponeParsingQiwiException, UnknownHostException, AuthorizationQiwiException {
        return this.getBalance().getBalanceRub();
    }

    public Wallet getBalance() throws AuthorizationQiwiException, UnknownHostException, ResponeParsingQiwiException {
        Wallet wallet = null;
        String responseString = sendGetBalance();

        try {
            ObjectMapper mapper = new ObjectMapper();
            wallet = mapper.readValue(responseString, Wallet.class);
        } catch (IOException e) {
            throw new ResponeParsingQiwiException(" exception in parsing response: " + responseString +"\n" + e.getMessage());
        }

        return wallet;
    }



    private String sendGetBalance() throws AuthorizationQiwiException, UnknownHostException {

        String url = "https://edge.qiwi.com/funding-sources/v1/accounts/current";
        return getResponseFromGET(url);

    }

    private String getResponseFromGET(String url) throws AuthorizationQiwiException, UnknownHostException {
        HttpsURLConnection connection = null;
        try {

            URL urlGet = new URL(url);

            connection = proxy != null ? (HttpsURLConnection) urlGet.openConnection(proxy) : (HttpsURLConnection) urlGet.openConnection();

            prepareConnectionHeaders(connection, "GET");

            int responseCode = connection.getResponseCode();
            logger.debug("\nSending 'GET' request to URL : " + url);
            logger.debug("Response Code : " + responseCode);

            if (responseCode == 200) {
                String resp = null;
                resp = readStringFromResponse(connection);
                connection.disconnect();
                return resp;
            }else{
                connection.disconnect();
                throw new AuthorizationQiwiException(" problems with your token: " + token);
            }

        }catch (java.io.IOException ioEsception){
            if(ioEsception instanceof UnknownHostException){
                throw new UnknownHostException(ioEsception.getMessage());
            }

        }finally {
            if(connection!=null) connection.disconnect();
        }
        return null;
    }


    private void prepareConnectionHeaders(HttpsURLConnection connection, String method) throws ProtocolException {
        connection.setRequestMethod(method);
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Authorization", "Bearer " + token);
        connection.setRequestProperty("Content-type", "application/json");
        connection.setRequestProperty("Host", "edge.qiwi.com");
    }

    private String readStringFromResponse(HttpsURLConnection con) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;

        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);

        }
        in.close();
        return response.toString();
    }

    /**
     *
     * The history of payments and recharge your wallet.
     *
     * @param rows The number of payments in the response, for breaking the report into parts. An integer from 1 to 50. Required
     * @return
     * @throws AuthorizationQiwiException
     * @throws UnknownHostException
     * @throws ResponeParsingQiwiException
     */

    public List<Payment> getPaymentsHistory(int rows, PaymentHistory.PaymentType paymentType) throws AuthorizationQiwiException, UnknownHostException, ResponeParsingQiwiException {
        if(rows<1)
            throw new IllegalArgumentException("Illegal rows: " + rows);
        if(rows>50)
            rows = 50; //max rows of rows

        String responseString;
        switch (paymentType){
            case INDEFERENT:
                responseString = sendGetHistory(rows);
                break;
            case PAYMENT_IN:
                responseString = sendGetHistoryIn(rows);
                break;
            case PAYMENT_OUT:
                responseString = sendGetHistoryOut(rows);
                break;

                default:
                    responseString = sendGetHistory(rows);
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            PaymentHistory ph = mapper.readValue(responseString, PaymentHistory.class);
            return ph.getData();
        } catch (IOException e) {
            throw new ResponeParsingQiwiException(" exception in parsing response: " + responseString +"\n" + e.getMessage());
        }

    }

    private String sendGetHistory(Integer rows) throws AuthorizationQiwiException, UnknownHostException {

        String url = "https://edge.qiwi.com/payment-history/v1/persons/{wallet}/payments?rows={rows}";
        url = url.replace("{wallet}", phone);
        url = url.replace("{rows}", rows.toString());

        return getResponseFromGET(url);

    }


    private String sendGetHistoryIn(Integer rows) throws AuthorizationQiwiException, UnknownHostException {

        String url = "https://edge.qiwi.com/payment-history/v1/persons/{wallet}/payments?rows={rows}&operation=IN";
        url = url.replace("{wallet}", phone);
        url = url.replace("{rows}", rows.toString());

        return getResponseFromGET(url);

    }

    private String sendGetHistoryOut(Integer rows) throws AuthorizationQiwiException, UnknownHostException {

        String url = "https://edge.qiwi.com/payment-history/v1/persons/{wallet}/payments?rows={rows}&operation=OUT";
        url = url.replace("{wallet}", phone);
        url = url.replace("{rows}", rows.toString());

        return getResponseFromGET(url);

    }


    /**
     * Peer-to-Peer QIWI Wallet Transfer
     *
     * @param phoneReciver Recipient's phone number (with international prefix)
     * @param amount Amount
     * @param comment can be null
     * @return TransferResponse - information about transaction:
     *         TransferResponse tr = qiwi.transferToWallet("7926...", 250.0d, "Thank you Maxim");
     *         if(tr.getTransaction().getState().getCode()==State.CODE_ACCEPTED){
     *             System.out.println( "id of accepted transaction " + tr.getTransaction().getId() );
     *         }
     *
     * @throws AuthorizationQiwiException you must correctly release the token:
     * mark down the option to transfer funds
     * @throws UnknownHostException
     * @throws ResponeParsingQiwiException
     */
    public TransferResponse transferToWallet(String phoneReciver, double amount, String comment) throws AuthorizationQiwiException, UnknownHostException, ResponeParsingQiwiException {

        TransferResponse tr = null;
        String url = "https://edge.qiwi.com/sinap/api/v2/terms/99/payments";

        String responseString = getResponseFromPOST(url, makeBodyWalletTransfer(phoneReciver, amount, comment) );

        try {
            ObjectMapper mapper = new ObjectMapper();
            tr = mapper.readValue(responseString, TransferResponse.class);
        } catch (IOException e) {
            throw new ResponeParsingQiwiException(" exception in parsing response: " + responseString +"\n" + e.getMessage());
        }

        return tr;
    }



    private static String makeBodyWalletTransfer(String phoneReciver, Double amount, String comment) {

        Objects.requireNonNull(phoneReciver, " phone must be not null");
        Objects.requireNonNull(amount, " amount must be not null");
        if(amount<=0.0d)
            throw new IllegalArgumentException("Illegal amount: " + amount);


        WalletTransfer walletTransfer = new WalletTransfer();

        if(comment==null)
            walletTransfer.setComment("");
        else
            walletTransfer.setComment(comment);

        walletTransfer.setSum(new Amount(amount));
        walletTransfer.setFields(new Field(phoneReciver));
        logger.info("bodyTransfer: \n" + walletTransfer.toString());

        return walletTransfer.toString();

    }


    private String getResponseFromPOST(String url, String body) throws AuthorizationQiwiException, UnknownHostException {
        HttpsURLConnection connection = null;
        try {

            URL urlGet = new URL(url);

            connection = proxy != null ? (HttpsURLConnection) urlGet.openConnection(proxy) : (HttpsURLConnection) urlGet.openConnection();

            prepareConnectionHeaders(connection, "POST");

            connection.setDoOutput( true );

            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes( body );
            wr.flush();
            wr.close();


            int responseCode = connection.getResponseCode();
            logger.debug("\nSending 'POST' request to URL : " + url);
            logger.debug("Response Code : " + responseCode);

            if (responseCode == 200) {
                String resp = null;
                resp = readStringFromResponse(connection);
                connection.disconnect();
                return resp;
            }else{
                connection.disconnect();
                throw new AuthorizationQiwiException("Error code "+ responseCode + " problems with your token: " + token );
            }

        }catch (java.io.IOException ioEsception){
            if(ioEsception instanceof UnknownHostException){
                throw new UnknownHostException(ioEsception.getMessage());
            }

        }finally {
            if(connection!=null) connection.disconnect();
        }
        return null;
    }



}
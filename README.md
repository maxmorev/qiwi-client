JAVA REST Qiwi API
========
:credit_card:
Java REST Qiwi API with simple qiwi-client test app
Based on [Official API QIWI Doc](https://developer.qiwi.com/en/qiwi-wallet-personal/index.html#intro)

Supported Operations & Examples
--------

#### Login

```java
            // Login with phone number and token
            String token = "";//your qiwi token token
            String phone = "79263.."; //your qiwi wallet
            System.out.println("2. TEST QIWI API GET BALANCE RU");
            
            QIWI qiwi = null;
            try{
                qiwi = new QIWI( phone, token);
            }catch (RestClientException ex){
                System.out.println("Probably error in phone or token: " + ex.getMessage());
            }
            
            if( qiwi!=null ) {
                System.out.println("QIWI BALCNCE: " + qiwi.getBalanceRU());
            }
```
#### Payments History

```java
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
```
#### Peer-to-Peer QIWI Wallet Transfer

```java
                Transaction transaction = null;
                try{
                    transaction = qiwi.transferToWallet("79112223344", 100.0, "Comment");
                }catch  (RestClientException ex){
                    System.out.println("Error in REST" + ex.getMessage());
                }
                if(transaction!=null){
                    System.out.println("Transaction id " + transaction.getId());
                }
```

#### Balance

```java
                qiwi.getBalanceRU();
```

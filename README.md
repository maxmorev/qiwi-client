JAVA REST Qiwi API
========
:credit_card:
Java REST Qiwi API with simple qiwi-client test app
Based on [Official API QIWI Doc](https://developer.qiwi.com/en/qiwi-wallet-personal/index.html#intro)

Supported Operations & Examples
--------

#### Login

```java
            QIWI qiwi = new QIWI( phone, token);
            
            double balanceRub = qiwi.getBalanceRub();
            System.out.println("Balance: " + balanceRub);
```
#### Payments History

```java
            
        List<Payment> paymentList = qiwi.getPaymentsHistory(20, PaymentHistory.PaymentType.INDEFERENT);
        System.out.println("Payments History size: " + paymentList.size());
```
#### Peer-to-Peer QIWI Wallet Transfer

```java
                TransferResponse tr = qiwi.transferToWallet("79263926369", 100.0d, "Thank you");
                if(tr.getTransaction().getState().getCode()==State.CODE_ACCEPTED){
                    System.out.println( "id of accepted transaction " + tr.getTransaction().getId() );
                }
                System.out.println(tr);
```

#### Balance

```java
                qiwi.getBalanceRub();
```

use http://www.jsonschema2pojo.org/ site to generate Java POJO

:credit_card:
Java REST Qiwi API with simple qiwi-client test app

Supported Operations & Examples
--------

#### Login

```java
// Login with phone and token
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
#### Payments list

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




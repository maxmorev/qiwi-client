package ru.maxmorev.payment.qiwi.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentMethod {

private String type = "Account";
private String accountId = "643";//RUB

public PaymentMethod() {
	super();
	this.type = "Account";
	this.accountId = "643";
}

public String getType() {
return type;
}

public void setType(String type) {
this.type = type;
}

public String getAccountId() {
return accountId;
}

public void setAccountId(String accountId) {
this.accountId = accountId;
}
/*
@Override
public String toString() {
	return  "{\"paymentMethod\": { \"type\": \"Account\", \"accountId\":\"643\"  }";
}
*/

}

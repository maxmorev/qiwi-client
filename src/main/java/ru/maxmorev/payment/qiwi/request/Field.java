package ru.maxmorev.payment.qiwi.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Field {

private String account;

public Field() {
	super();
}

public Field(String phoneReciever) {
	this.account = phoneReciever;
}

public String getAccount() {
return account;
}

public void setAccount(String account) {
this.account = account;
}

@Override
public String toString() {
return "{\"account\": \""+this.account+"\"}";
}

}

package ru.maxmorev.payment.qiwi.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Payment {

private long txnId;
private long personId;
private Date date;
private String status;//SUCCESS
private String type;	//OUT:IN
private String statusText;
private String trmTxnId;
private String account;
private Amount sum;
private Amount commission;
private Amount total;
private String comment;

public long getTxnId() {
return txnId;
}

public void setTxnId(long txnId) {
this.txnId = txnId;
}

public long getPersonId() {
return personId;
}

public void setPersonId(long personId) {
this.personId = personId;
}

public Date getDate() {
return date;
}

public void setDate(Date date) {
this.date = date;
}

public String getStatus() {
return status;
}

public void setStatus(String status) {
this.status = status;
}

public String getType() {
return type;
}

public void setType(String type) {
this.type = type;
}

public String getStatusText() {
return statusText;
}

public void setStatusText(String statusText) {
this.statusText = statusText;
}

public String getTrmTxnId() {
return trmTxnId;
}

public void setTrmTxnId(String trmTxnId) {
this.trmTxnId = trmTxnId;
}

/**
 * @return tel number
 */
public String getAccount() {
return account;
}

public void setAccount(String account) {
this.account = account;
}

public Amount getTotal() {
return total;
}

public void setTotal(Amount total) {
this.total = total;
}

public Amount getCommission() {
return commission;
}

public void setCommission(Amount commission) {
this.commission = commission;
}



public String getComment() {
return comment;
}

public void setComment(String comment) {
this.comment = comment;
}

public Amount getSum() {
	return sum;
}

public void setSum(Amount sum) {
	this.sum = sum;
}

@Override
public String toString() {
	ObjectMapper mapper = new ObjectMapper();
	
	String jsonStr = "";
	try {
		jsonStr = mapper.writeValueAsString(this);
	}catch(Exception e) {
		
	}
	return jsonStr;
}

}

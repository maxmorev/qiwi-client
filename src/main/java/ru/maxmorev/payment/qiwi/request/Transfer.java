package ru.maxmorev.payment.qiwi.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.maxmorev.payment.qiwi.response.TransactionAmount;

@JsonIgnoreProperties(ignoreUnknown = true )
public class Transfer {
/*Клиентский ID транзакции (максимум 20 цифр). Должен быть уникальным для каждой транзакции и увеличиваться с каждой последующей транзакцией*/
private String id;
private TransactionAmount sum;
private PaymentMethod paymentMethod;
private String comment;
private Field fields;

public Transfer() {
	super();
}

/**
 * @param id Клиентский ID транзакции (максимум 20 цифр). 
 * Должен быть уникальным для каждой транзакции и увеличиваться с каждой последующей транзакцией
 * @param sum рубли.копейки
 * @param comment Комментарий к платежу. Необязательный параметр. Для апи приложений почти всегда необходим
 * @param phoneReciever Номер телефона получателя (с международным префиксом) +79121112233
 */
@JsonIgnore
public Transfer(String id, double sum, String comment, String phoneReciever) {
	super();
	this.id = id;
	this.sum = new TransactionAmount(sum);
	this.comment = comment;
	this.fields = new Field(phoneReciever);
	this.paymentMethod = new PaymentMethod();
}


public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public TransactionAmount getSum() {
return sum;
}

public void setSum(TransactionAmount sum) {
this.sum = sum;
}

public PaymentMethod getPaymentMethod() {
return paymentMethod;
}

public void setPaymentMethod(PaymentMethod paymentMethod) {
this.paymentMethod = paymentMethod;
}

public String getComment() {
return comment;
}

public void setComment(String comment) {
this.comment = comment;
}

public Field getFields() {
return fields;
}

public void setFields(Field fields) {
this.fields = fields;
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

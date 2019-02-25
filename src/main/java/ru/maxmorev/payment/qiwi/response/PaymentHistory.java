package ru.maxmorev.payment.qiwi.response;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentHistory {
	private List<Payment> data;

	public List<Payment> getData() {
		return data;
	}

	public void setData(List<Payment> data) {
		this.data = data;
	} 

}

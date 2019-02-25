package ru.maxmorev.payment.qiwi.response;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Amount {
	
	@JsonIgnore
	public String PRINTFORMAT="0.###";
	
	private double amount;
	private int currency;
	
	public Amount() {
		super();
		this.currency = 643;
	}
	
	public Amount(double amount) {
		super();
		this.currency = 643;
		this.setAmount(amount);
	}
	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
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
	
	@JsonIgnore
	public double formatDouble() {
		NumberFormat formatter = new DecimalFormat();
		formatter = new DecimalFormat(this.PRINTFORMAT);
		String cute = formatter.format(amount);
		return Double.valueOf(cute) ;
	}
	@JsonIgnore
	public String getPrinteble() {
		NumberFormat formatter = new DecimalFormat();
		formatter = new DecimalFormat(this.PRINTFORMAT);
		String cute = formatter.format(amount);
		return cute;
	}	
	public int getCurrency() {
	return currency;
	}
	
	public void setCurrency(int currency) {
	this.currency = currency;
	}
	

}

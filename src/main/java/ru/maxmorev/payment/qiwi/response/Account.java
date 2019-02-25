package ru.maxmorev.payment.qiwi.response;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public class Account {
	
	private String alias;
	private String fsAlias;
	private String title;
	private boolean hasBalance;
	private Amount balance;
	private int currency;
	
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getFsAlias() {
		return fsAlias;
	}
	public void setFsAlias(String fsAlias) {
		this.fsAlias = fsAlias;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public boolean isHasBalance() {
		return hasBalance;
	}
	public void setHasBalance(boolean hasBalance) {
		this.hasBalance = hasBalance;
	}
	public Amount getBalance() {
		return balance;
	}
	public void setBalance(Amount balance) {
		this.balance = balance;
	}
	public int getCurrency() {
		return currency;
	}
	public void setCurrency(int currency) {
		this.currency = currency;
	}
	


}

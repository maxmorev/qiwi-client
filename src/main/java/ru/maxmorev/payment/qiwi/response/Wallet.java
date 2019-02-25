package ru.maxmorev.payment.qiwi.response;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public class Wallet {
	
	private List<Account> accounts;
	
	public List<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	} 
	
	@JsonIgnore
	private double ruBalance;
	
	public double getRuBalance() {
		if(this.accounts==null) {
			//this.hasBalance = false;
			return -404.0;
		}
		for (Account account : accounts) {
		    //System.out.println(element);
			if(account.getAlias().equals("qw_wallet_rub")) {
				this.ruBalance = account.getBalance().getAmount();
				return ruBalance;
			}
		}
		return this.ruBalance;
	}



}

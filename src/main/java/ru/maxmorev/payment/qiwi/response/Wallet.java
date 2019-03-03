package ru.maxmorev.payment.qiwi.response;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public class Wallet extends QiwiResponse {

	public static final String ALIAS_RU_ACCOUNT = "qw_wallet_rub";

	private List<Account> accounts;
	
	public List<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	} 
	
	@JsonIgnore
	private double ruBalance;
	
	public double getBalanceRub() {
		if(accounts==null){
			return 0.0d;
		}
		Optional<Account> account = accounts.stream().filter(a->a.getAlias().equals(ALIAS_RU_ACCOUNT)).findFirst();

		if(account.isPresent()){
				this.ruBalance = account.get().getBalance().getAmount();
		}
		return this.ruBalance;
	}



}

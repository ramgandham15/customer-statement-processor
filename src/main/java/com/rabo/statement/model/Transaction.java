package com.rabo.statement.model;

public class Transaction {

	private Long reference;
	private String accountNumber;

	public Long getReference() {
		return reference;
	}

	public void setReference(Long reference) {
		this.reference = reference;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	@Override
	public String toString() {
		return "Transaction [reference=" + reference + ", accountNumber=" + accountNumber + "]";
	}


}
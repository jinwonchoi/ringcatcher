package com.eclues.ringcatcher.obj;

public class Customer {
	private String customerId;
	private String customerName;
	
	public Customer() { }
	
	public Customer(String id, String name) {
		this.customerId = id;
		this.customerName = name;
	}
	
	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	public String toString() {
		return String.format("%s - %s", customerId, customerName);
	}
}

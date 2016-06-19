package com.eclues.ringcatcher.dao;

import java.util.List;

import com.eclues.ringcatcher.Customer;

public interface CustomerDAO {
	public void saveOrUpdate(Customer customer);
	public void delete(String id);
	public Customer get(String id);
	public List<Customer> list();
}

package com.eclues.ringcatcher.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import com.eclues.ringcatcher.obj.Customer;

public class CustomerDAOImpl implements CustomerDAO {

	private JdbcTemplate jdbcTemplate;
	
	public CustomerDAOImpl(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public void saveOrUpdate(Customer customer) {
		if (!customer.getCustomerId().equals("")) {
			//update
			String sql = "UPDATE customer SET customer_name = ? "
					+ "where cusomter_id = ?";
			jdbcTemplate.update(sql, customer.getCustomerName(), customer.getCustomerId());
		} else {
			//insert
			String sql = "INSERT INTO customer (customer_id, customer_name)"
					+ " VALUES(?, ?)";
			jdbcTemplate.update(sql, customer.getCustomerId(), customer.getCustomerName());
		}
	}

	@Override
	public void delete(String id) {
		String sql = "Delete from customer where customer_id = ?";
		jdbcTemplate.update(sql, id);
	}

	@Override
	public Customer get(String id) {
		String sql = "SELECT * FROM customer where customer_id ='"+id+"'";
		return jdbcTemplate.query(sql, new ResultSetExtractor<Customer>(){
			@Override
			public Customer extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Customer customer = new Customer();
					customer.setCustomerId(rs.getString("customer_id"));
					customer.setCustomerName(rs.getString("customer_name"));
					return customer;
				}
				return null;
			}
			
		});
	}

	@Override
	public List<Customer> list() {
		String sql = "SELECT * FROM customer";
		List<Customer> lsitCustomer = jdbcTemplate.query(sql, new RowMapper<Customer>(){
			@Override
			public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
				Customer customer = new Customer();
				customer.setCustomerId(rs.getString("customer_id"));
				customer.setCustomerName(rs.getString("customer_name"));
				return customer;
			}
		});
		return lsitCustomer;
	}

}

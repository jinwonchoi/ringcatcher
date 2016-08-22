package com.eclues.ringcatcher.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.eclues.ringcatcher.dao.AdminInfoDAO;
import com.eclues.ringcatcher.dao.AdminInfoDAOImpl;
import com.eclues.ringcatcher.dao.CustomerDAO;
import com.eclues.ringcatcher.dao.CustomerDAOImpl;
import com.eclues.ringcatcher.dao.MsgHistoryDAO;
import com.eclues.ringcatcher.dao.MsgHistoryDAOImpl;
import com.eclues.ringcatcher.dao.MsgInfoDAO;
import com.eclues.ringcatcher.dao.MsgInfoDAOImpl;
import com.eclues.ringcatcher.dao.RingHistoryDAO;
import com.eclues.ringcatcher.dao.RingHistoryDAOImpl;
import com.eclues.ringcatcher.dao.RingInfoDAO;
import com.eclues.ringcatcher.dao.RingInfoDAOImpl;
import com.eclues.ringcatcher.dao.UserInfoDAO;
import com.eclues.ringcatcher.dao.UserInfoDAOImpl;

@Configuration
@ComponentScan(basePackages="com.eclues.ringcatcher")
@EnableWebMvc
public class MvcConfiguration extends WebMvcConfigurerAdapter {
	
	@Bean
	public ViewResolver getViewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".jsp");
		return resolver;
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	}
	
    /*<beans:bean id="transactionManager"
            class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
            <beans:property name="dataSource" ref="dataSource" />
        </beans:bean> 
    */    
	@Bean
	public DataSourceTransactionManager getTransactionManager() {
		DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
		transactionManager.setDataSource(getDataSource());
		return transactionManager;
	}
	
	@Bean
	public DataSource getDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost:3306/rc_db");
		dataSource.setUsername("root");
		dataSource.setPassword("jessy");
		return dataSource;
	}
	
	@Bean
	public CustomerDAO getCustomerDAO() {
		return new CustomerDAOImpl(this.getDataSource());
	}

	@Bean
	public UserInfoDAO getUserInfoDAO() {
		return new UserInfoDAOImpl(this.getDataSource());
	}

	@Bean
	public RingInfoDAO getRingInfoDAO() {
		return new RingInfoDAOImpl(this.getDataSource());
	}

	@Bean
	public RingHistoryDAO getRingHistoryDAO() {
		return new RingHistoryDAOImpl(this.getDataSource());
	}

	@Bean
	public MsgInfoDAO getMsgInfoDAO() {
		return new MsgInfoDAOImpl(this.getDataSource());
	}

	@Bean
	public MsgHistoryDAO getMsgHistoryDAO() {
		return new MsgHistoryDAOImpl(this.getDataSource());
	}

	@Bean
	public AdminInfoDAO getAdminInfoDAO() {
		return new AdminInfoDAOImpl(this.getDataSource());
	}
}

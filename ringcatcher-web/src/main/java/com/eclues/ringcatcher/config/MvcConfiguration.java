package com.eclues.ringcatcher.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
	@Autowired
	@Qualifier("dataSource")
	private org.apache.commons.dbcp.BasicDataSource dataSource;
	
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
		transactionManager.setDataSource(dataSource);
		return transactionManager;
	}
	
/*	@Bean
	public DataSource getDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setUrl(datasourcePro);
		dataSource.setConnectionProperties(datasourceProperties);
		return dataSource;
	}*/
	
	@Bean
	public CustomerDAO getCustomerDAO() {
		return new CustomerDAOImpl(dataSource);
	}

	@Bean
	public UserInfoDAO getUserInfoDAO() {
		return new UserInfoDAOImpl(dataSource);
	}

	@Bean
	public RingInfoDAO getRingInfoDAO() {
		return new RingInfoDAOImpl(dataSource);
	}

	@Bean
	public RingHistoryDAO getRingHistoryDAO() {
		return new RingHistoryDAOImpl(dataSource);
	}

	@Bean
	public MsgInfoDAO getMsgInfoDAO() {
		return new MsgInfoDAOImpl(dataSource);
	}

	@Bean
	public MsgHistoryDAO getMsgHistoryDAO() {
		return new MsgHistoryDAOImpl(dataSource);
	}

	@Bean
	public AdminInfoDAO getAdminInfoDAO() {
		return new AdminInfoDAOImpl(dataSource);
	}
}

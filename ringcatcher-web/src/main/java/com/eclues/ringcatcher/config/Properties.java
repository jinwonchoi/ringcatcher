package com.eclues.ringcatcher.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

import com.eclues.ringcatcher.dao.EnvironmentBean;

@Configuration
@PropertySource("file:${work.path}/conf/config.properties")
public class Properties {

//	@Value("$(domain.name)")
//	public String domainName;
	@Autowired
	private Environment env;
	
	@Bean
	public EnvironmentBean environmentBean() {
		EnvironmentBean envBean = new EnvironmentBean();
		envBean.setDomainName(env.getProperty("domain.name"));
		envBean.setDomainIp(env.getProperty("domain.ip"));
		envBean.setPort(env.getProperty("domain.port"));
		envBean.setGcmApiKey(env.getProperty("gcm.apikey"));
		return envBean;
	}
	public String getDomainName() {
		System.out.println(env.getProperty("domain.ip"));
		for (String key : env.getDefaultProfiles()) {
			System.out.println(key+":"+env.getProperty(key));
		}
		return env.getProperty("domain.name");
	}
	
//	
//	
//	domain.name=notdecided
//	domain.ip=127.9.9.1
//	sound.filepath=/asdfasdfasdfa
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
		return new PropertySourcesPlaceholderConfigurer();
	}
}

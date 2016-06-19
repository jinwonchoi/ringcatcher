package com.eclues.ringcatcher.dao;

public class EnvironmentBean {
	String domainName;
	String domainIp;
	String port;
	String gcmApiKey;
	public String getDomainName() {
		return domainName;
	}
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	public String getDomainIp() {
		return domainIp;
	}
	public void setDomainIp(String domainIp) {
		this.domainIp = domainIp;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getGcmApiKey() {
		return gcmApiKey;
	}
	public void setGcmApiKey(String gcmApiKey) {
		this.gcmApiKey = gcmApiKey;
	}
	
}

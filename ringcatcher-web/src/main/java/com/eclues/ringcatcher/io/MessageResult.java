package com.eclues.ringcatcher.io;

import java.io.Serializable;

public class MessageResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String resultCode;
	String resultMsg;
	String defaultDurationType;
	String defaultJsonMessage;
	String defaultExpiredDate;
	String durationType;
	String jsonMessage;
	String expiredDate;
	
	public MessageResult() {
		// TODO Auto-generated constructor stub
	}
		
	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}
	
	public String getDefaultJsonMessage() {
		return defaultJsonMessage;
	}

	public void setDefaultJsonMessage(String defaultJsonMessage) {
		this.defaultJsonMessage = defaultJsonMessage;
	}

	public String getJsonMessage() {
		return jsonMessage;
	}

	public void setJsonMessage(String jsonMessage) {
		this.jsonMessage = jsonMessage;
	}

	public String getDefaultDurationType() {
		return defaultDurationType;
	}

	public void setDefaultDurationType(String defaultDurationType) {
		this.defaultDurationType = defaultDurationType;
	}

	public String getDurationType() {
		return durationType;
	}

	public void setDurationType(String durationType) {
		this.durationType = durationType;
	}

	public String getDefaultExpiredDate() {
		return defaultExpiredDate;
	}

	public void setDefaultExpiredDate(String defaultExpiredDate) {
		this.defaultExpiredDate = defaultExpiredDate;
	}

	public String getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(String expiredDate) {
		this.expiredDate = expiredDate;
	}

	public String toString() {
		return String.format("[%s][%s][%s][%s][%s][%s][%s][%s]", resultCode, resultMsg, defaultExpiredDate,defaultDurationType, defaultJsonMessage, expiredDate, durationType, jsonMessage);
	}
}

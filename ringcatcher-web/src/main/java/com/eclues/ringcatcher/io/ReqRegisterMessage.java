package com.eclues.ringcatcher.io;

import java.io.Serializable;

public class ReqRegisterMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3686159514938993292L;
	String userNum;
	String callingId;
	String callingNum;
	String callingName;
	String locale;
	String jsonMessage;
	String expiredDate;
	String durationType;
	
	public ReqRegisterMessage() {	}
	
	public ReqRegisterMessage(String userNum, String callingId, String callingNum, String callingName) {
		this.userNum = userNum;
		this.callingId = callingId;
		this.callingNum = callingNum;
		this.callingName = callingName;
	}
	
	public String getCallingId() {
		return callingId;
	}

	public void setCallingId(String callingId) {
		this.callingId = callingId;
	}

	public String getUserNum() {
		return userNum;
	}

	public void setUserNum(String userNum) {
		this.userNum = userNum;
	}

	public String getCallingNum() {
		return callingNum;
	}

	public void setCallingNum(String callingNum) {
		this.callingNum = callingNum;
	}
	
	public String getCallingName() {
		return callingName;
	}

	public void setCallingName(String callingName) {
		this.callingName = callingName;
	}

	public String getJsonMessage() {
		return jsonMessage;
	}

	public void setJsonMessage(String jsonMessage) {
		this.jsonMessage = jsonMessage;
	}
	
	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(String expiredDate) {
		this.expiredDate = expiredDate;
	}

	public String getDurationType() {
		return durationType;
	}

	public void setDurationType(String durationType) {
		this.durationType = durationType;
	}

	public String toString() {
		return String.format("%s:%s:%s:%s:%s:%s:%s:%s", userNum, callingId, callingNum, callingName, jsonMessage, locale, expiredDate, durationType);
	}
}

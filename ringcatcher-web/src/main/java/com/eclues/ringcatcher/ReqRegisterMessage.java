package com.eclues.ringcatcher;

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

	public String toString() {
		return String.format("%s:%s:%s:%s:%s:%s", userNum, callingId, callingNum, callingName, jsonMessage, locale);
	}
}

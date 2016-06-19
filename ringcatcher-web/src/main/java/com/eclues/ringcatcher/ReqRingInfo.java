package com.eclues.ringcatcher;

import java.io.Serializable;

public class ReqRingInfo implements Serializable {

	String userNum;
	String callingId;
	String callingNum;
	String callingName;
	String locale;
	String ringFileName;
	
	public ReqRingInfo() {	}
	
	public ReqRingInfo(String userNum, String callingId, String callingNum, String callingName) {
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

	public String getRingFileName() {
		return ringFileName;
	}

	public void setRingFileName(String ringFileName) {
		this.ringFileName = ringFileName;
	}
	
	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String toString() {
		return String.format("%s:%s:%s:%s:%s:%s", userNum, callingId, callingNum, callingName, ringFileName, locale);
	}
}

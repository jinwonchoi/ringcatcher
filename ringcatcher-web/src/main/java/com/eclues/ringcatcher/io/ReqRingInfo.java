package com.eclues.ringcatcher.io;

import java.io.Serializable;

public class ReqRingInfo implements Serializable {

	String userNum;
	String callingId;
	String callingNum;
	String callingName;
	String locale;
	String ringFileName;
	String expiredDate;
	String durationType; /* A:1회용, T:기간지정, P:영속*/

	public ReqRingInfo() {	}
	
	public ReqRingInfo(String userNum, String callingId, String callingNum, String callingName, String expiredDate, String durationType) {
		this.userNum = userNum;
		this.callingId = callingId;
		this.callingNum = callingNum;
		this.callingName = callingName;
		this.expiredDate = expiredDate;
		this.durationType = durationType;
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

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String toString() {
		return String.format("%s:%s:%s:%s:%s:%s:%s", userNum, callingId, callingNum, callingName, ringFileName,durationType, locale);
	}
}

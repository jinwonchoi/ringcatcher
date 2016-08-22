package com.eclues.ringcatcher.obj;

import java.io.Serializable;

public class RingHistory implements Serializable{

	String userNum;
	String callingNum;
	String callingName;
	String registerDate;
	String expiredDate;
	String userId;
	String durationType;
	String ringFileName;
	String updateDate;
	String createDate;
	
	public RingHistory() {	}
	
	public RingHistory(String userNum, String callingNum, String callingName, String registerDate, String expiredDate
			, String userId, String ringFileName, String durationType) {
		this.userNum = userNum;
		this.callingNum = callingNum;
		this.callingName = callingName;
		this.registerDate = registerDate;
		this.expiredDate = expiredDate;
		this.userId = userId;
		this.ringFileName = ringFileName;
		this.durationType = durationType;
	}

	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public String getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(String registerDate) {
		this.registerDate = registerDate;
	}
	
	public String getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(String expiredDate) {
		this.expiredDate = expiredDate;
	}

	public String getRingFileName() {
		return ringFileName;
	}

	public void setRingFileName(String ringFileName) {
		this.ringFileName = ringFileName;
	}

	public String getDurationType() {
		return durationType;
	}

	public void setDurationType(String durationType) {
		this.durationType = durationType;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String toString() {
		return String.format("%s:%s:%s:%s:%s:%s:%s:%s:%s", userNum, callingNum, callingName, registerDate, registerDate, userId, ringFileName, durationType, createDate);
	}
}

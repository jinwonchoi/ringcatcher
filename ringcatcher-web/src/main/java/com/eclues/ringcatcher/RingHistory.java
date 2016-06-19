package com.eclues.ringcatcher;

import java.io.Serializable;

public class RingHistory implements Serializable{

	String userNum;
	String callingNum;
	String callingName;
	String registerDate;
	String userId;
	String ringFileName;
	String updateDate;
	String createDate;
	
	public RingHistory() {	}
	
	public RingHistory(String userNum, String callingNum, String callingName, String registerDate
			, String userId, String ringFileName) {
		this.userNum = userNum;
		this.callingNum = callingNum;
		this.callingName = callingName;
		this.registerDate = registerDate;
		this.userId = userId;
		this.ringFileName = ringFileName;
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

	public String getRingFileName() {
		return ringFileName;
	}

	public void setRingFileName(String ringFileName) {
		this.ringFileName = ringFileName;
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
		return String.format("%s:%s:%s:%s:%s:%s:%s", userNum, callingNum, callingName, registerDate, userId, ringFileName, createDate);
	}
}

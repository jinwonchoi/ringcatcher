package com.eclues.ringcatcher;

import java.io.Serializable;

public class MsgHistory implements Serializable{

	String userNum;
	String callingNum;
	String callingName;
	String registerDate;
	String expiredDate;
	String userId;
	String jsonMsg;
	String durationType;
	String updateDate;
	String createDate;
	
	public MsgHistory() {	}
	
	public MsgHistory(String userNum, String callingNum, String callingName, String registerDate, String expiredDate
			, String userId, String jsonMsg, String durationType) {
		this.userNum = userNum;
		this.callingNum = callingNum;
		this.callingName = callingName;
		this.registerDate = registerDate;
		this.expiredDate = expiredDate;
		this.userId = userId;
		this.jsonMsg = jsonMsg;
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

	public String getJsonMsg() {
		return jsonMsg;
	}

	public void setJsonMsg(String jsonMsg) {
		this.jsonMsg = jsonMsg;
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
		return String.format("%s:%s:%s:%s:%s:%s:%s:%s:%s", userNum, callingNum, callingName, registerDate,expiredDate, userId, jsonMsg, durationType, createDate);
	}
}

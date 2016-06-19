package com.eclues.ringcatcher;

import java.io.Serializable;

public class MsgHistory implements Serializable{

	String userNum;
	String callingNum;
	String callingName;
	String registerDate;
	String userId;
	String jsonMsg;
	String updateDate;
	String createDate;
	
	public MsgHistory() {	}
	
	public MsgHistory(String userNum, String callingNum, String callingName, String registerDate
			, String userId, String jsonMsg) {
		this.userNum = userNum;
		this.callingNum = callingNum;
		this.callingName = callingName;
		this.registerDate = registerDate;
		this.userId = userId;
		this.jsonMsg = jsonMsg;
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

	public String getJsonMsg() {
		return jsonMsg;
	}

	public void setJsonMsg(String jsonMsg) {
		this.jsonMsg = jsonMsg;
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
		return String.format("%s:%s:%s:%s:%s:%s:%s", userNum, callingNum, callingName, registerDate, userId, jsonMsg, createDate);
	}
}

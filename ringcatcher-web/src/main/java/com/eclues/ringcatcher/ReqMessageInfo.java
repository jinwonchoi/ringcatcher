package com.eclues.ringcatcher;

import java.io.Serializable;

public class ReqMessageInfo implements Serializable {

	String userId;
	String userNum;
	String callingNum;
	
	public ReqMessageInfo() {	}
	
	public ReqMessageInfo(String userId, String userNum, String callingNum) {
		this.userId = userId;
		this.userNum = userNum;
		this.callingNum = callingNum;
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
		
	public String toString() {
		return String.format("%s:%s:%s", userId, userNum, callingNum);
	}
}

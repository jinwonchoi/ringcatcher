package com.eclues.ringcatcher;

import java.io.Serializable;

public class ReqRingUpdate implements Serializable {

	String userId;
	String userNum;
	
	public ReqRingUpdate() {	}
	
	public ReqRingUpdate(String userId, String userNum) {
		this.userId= userId;
		this.userNum = userNum;
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

	public String toString() {
		return String.format("%s:%s", userId, userNum);
	}
}

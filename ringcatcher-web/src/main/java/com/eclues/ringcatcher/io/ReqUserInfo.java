package com.eclues.ringcatcher.io;

import java.io.Serializable;

public class ReqUserInfo implements Serializable {

	String userNum;
	String userId;
	String userName;
	String userEmail;
	String recomId;
	boolean overwrite = false;
	
	public ReqUserInfo() {	}
	
	public String getUserNum() {
		return userNum;
	}

	public void setUserNum(String userNum) {
		this.userNum = userNum;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getRecomId() {
		return recomId;
	}

	public void setRecomId(String recomId) {
		this.recomId = recomId;
	}

	
	public boolean isOverwrite() {
		return overwrite;
	}

	public void setOverwrite(boolean overwrite) {
		this.overwrite = overwrite;
	}

	public String toString() {
		return String.format("%s:%s:%s:%s:%s:%s", userNum,userId,userName, userEmail, recomId, overwrite);
	}
}

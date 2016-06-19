package com.eclues.ringcatcher;

import java.io.Serializable;

public class UserInfo implements Serializable {

	String userNum;
	String userId;
	String userName;
	String userEmail;
	String recomId;
	String updateDate;
	String createDate;
	
	public UserInfo() {	}
	
	public UserInfo(String userNum) {
		this.userId = userNum;
	}

	public UserInfo(String userNum, String userId, String userName,String userEmail, String recomId) {
		this.userNum = userNum;
		this.userId = userId;
		this.userName = userName;
		this.userEmail = userEmail;
		this.recomId = recomId;
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
		return String.format("%s:%s:%s:%s:%s:%s", userNum,userId, userName,userEmail, recomId,createDate);
	}
}

package com.eclues.ringcatcher.obj;

import java.io.Serializable;

public class AdminInfo implements Serializable {

	String userId;
	String authId; /*email or phone number */
	String authIdType; /*email: E, phone number : P*/
	String userName;
	String userPasswd;
	String description;
	String updateDate;
	String createDate;
	/**
	      user_id varchar(200) NOT NULL,
		  user_id_type varchar(1) NOT NULL, //email: E, phone number : P
		  user_name varchar(100) DEFAULT NULL,
		  user_passwd varchar(200) NOT NULL,
		  description varchar(200) DEFAULT NULL,
		  update_date timestamp NOT NULL,
		  create_date timestamp NOT NULL,
	 */
	public AdminInfo() {	}
	
	public AdminInfo(String userNum) {
		this.userId = userNum;
	}

	public AdminInfo(String userId, String authId, String authIdType, String userName,String userPasswd, String description) {
		this.userId = userId;
		this.authId = authId;
		this.authIdType= authIdType;
		this.userName = userName;
		this.userPasswd= userPasswd;
		this.description = description;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAuthId() {
		return authId;
	}

	public void setAuthId(String authId) {
		this.authId = authId;
	}

	public String getAuthIdType() {
		return authIdType;
	}

	public void setAuthIdType(String authIdType) {
		this.authIdType = authIdType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPasswd() {
		return userPasswd;
	}

	public void setUserPasswd(String userPasswd) {
		this.userPasswd = userPasswd;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
		return String.format("%s:%s:%s:%s:%s:%s", userId, authId, authIdType, userName,userPasswd, description);
	}
}

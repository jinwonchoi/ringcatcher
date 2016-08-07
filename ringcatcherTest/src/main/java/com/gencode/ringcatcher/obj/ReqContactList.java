package com.gencode.ringcatcher.obj;

import java.io.Serializable;

public class ReqContactList implements Serializable {

	String userNum;
	String userId;
	String contactList;
	
	public ReqContactList() {	}
	
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

	public String getContactList() {
		return contactList;
	}

	public void setContactList(String contactList) {
		this.contactList = contactList;
	}

	public String toString() {
		return String.format("%s:%s:%s", userNum,userId,contactList);
	}
}

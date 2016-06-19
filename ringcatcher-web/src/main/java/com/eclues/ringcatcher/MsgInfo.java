package com.eclues.ringcatcher;

import java.io.Serializable;

public class MsgInfo implements Serializable {

	String userNum;
	String callingNum;
	String callingName;
	String registerDate;
	String jsonMsg;
	int download_cnt;
	String updateDate;
	String createDate;
	
	public MsgInfo() {	}
	
	public MsgInfo(String userNum, String callingNum) {
		this.userNum = userNum;
		this.callingNum = callingNum;
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

	public int getDownload_cnt() {
		return download_cnt;
	}

	public void setDownload_cnt(int download_cnt) {
		this.download_cnt = download_cnt;
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
		return String.format("%s:%s:%s:%s:%d:%s:%s", userNum, callingNum, callingName, registerDate, download_cnt, jsonMsg, createDate);
	}
}

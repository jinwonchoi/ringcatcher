package com.eclues.ringcatcher.obj;

import java.io.Serializable;

public class RingInfo implements Serializable {

	String userNum;
	String callingNum;
	String callingName;
	String registerDate;
	String expiredDate;
	String ringFileName;
	String durationType;
	int download_cnt;
	String updateDate;
	String createDate;
	
	public RingInfo() {	}
	
	public RingInfo(String userNum, String callingNum) {
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

	public String getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(String expiredDate) {
		this.expiredDate = expiredDate;
	}

	public String getRingFileName() {
		return ringFileName;
	}

	public void setRingFileName(String ringFileName) {
		this.ringFileName = ringFileName;
	}

	public String getDurationType() {
		return durationType;
	}

	public void setDurationType(String durationType) {
		this.durationType = durationType;
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
		return String.format("%s:%s:%s:%s:%d:%s:%s:%s", userNum, callingNum, callingName, registerDate, download_cnt, ringFileName, durationType, createDate);
	}
}

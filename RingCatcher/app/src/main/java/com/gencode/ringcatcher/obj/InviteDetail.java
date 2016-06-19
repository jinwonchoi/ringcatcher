package com.gencode.ringcatcher.obj;

import java.io.Serializable;

public class InviteDetail implements Serializable {

	String userNum;
	String callerNum;
	String ringSrcUrl;
	
	public String getUserNum() {
		return userNum;
	}

	public void setUserNum(String userNum) {
		this.userNum = userNum;
	}

	public String getCallerNum() {
		return callerNum;
	}

	public void setCallerNum(String callerNum) {
		this.callerNum = callerNum;
	}

	public String getRingSrcUrl() {
		return ringSrcUrl;
	}

	public void setRingSrcUrl(String ringSrcUrl) {
		this.ringSrcUrl = ringSrcUrl;
	}

	public String toString() {
		return String.format("[%s][%s][%s]", userNum, callerNum, ringSrcUrl);
	}
}

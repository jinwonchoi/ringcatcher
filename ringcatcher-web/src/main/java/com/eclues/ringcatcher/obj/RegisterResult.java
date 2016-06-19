package com.eclues.ringcatcher.obj;

import java.io.Serializable;

public class RegisterResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5014421380852439305L;
	String resultCode;
	String resultMsg;
	
	public RegisterResult() {
		// TODO Auto-generated constructor stub
	}
		
	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

	public String toString() {
		return String.format("[%s][%s]", resultCode, resultMsg);
	}
}

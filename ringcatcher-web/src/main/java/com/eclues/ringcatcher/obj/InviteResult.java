package com.eclues.ringcatcher.obj;

import java.io.Serializable;

public class InviteResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6826053308801805578L;
	String resultCode;
	String resultMsg;
	
	public InviteResult() {
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

package com.eclues.ringcatcher.obj;

import java.io.Serializable;

public class UploadImageResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String resultCode;
	String resultMsg;
	String fileUrl;
	
	public UploadImageResult() {
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
	

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public String toString() {
		return String.format("[%s][%s][%s]", resultCode, resultMsg, fileUrl);
	}
}

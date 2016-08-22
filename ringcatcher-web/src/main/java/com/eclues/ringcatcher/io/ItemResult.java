package com.eclues.ringcatcher.io;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ItemResult<T> implements Serializable {
	private static final Logger logger = LoggerFactory.getLogger(ItemResult.class);
	private static final long serialVersionUID = 1L;
	String resultCode;
	String resultMsg;
	T result;//userNum,callingNum,callingName,registerDate,expiredDate,jsonMsg,durationType,downloadCnt,updateDate;

	public ItemResult() {
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
	
	public T getItem() {
		return result;
	}

	public void setItem(T item) {
		result = item;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(String.format("[%s][%s]", resultCode, resultMsg));
		if (result != null)
			sb.append(String.format("{%s}", result.toString()));
		
		return sb.toString();
	}
}

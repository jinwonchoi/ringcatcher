package com.gencode.ringcatcher.obj;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class ListResult<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	String resultCode;
	String resultMsg;
	ArrayList<T> resultList = new ArrayList<T>();//userNum,callingNum,callingName,registerDate,expiredDate,jsonMsg,durationType,downloadCnt,updateDate;

	public ListResult() {
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
	
	public List<T> getList() {
		return resultList;
	}

	public void setList(T item) {
		resultList.add(item);
	}

	public void setList(ArrayList<T> itemList) {
		resultList =  itemList;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(String.format("[%s][%s]", resultCode, resultMsg));
		
		for (T item : resultList) {
			sb.append(String.format("{%s}", item.toString()));
		}

		return sb.toString();
	}
}

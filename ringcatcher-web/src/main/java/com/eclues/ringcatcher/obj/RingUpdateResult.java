package com.eclues.ringcatcher.obj;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class RingUpdateResult implements Serializable {

	private static final long serialVersionUID = -242858495909118917L;

	final public String CALLING_NUM = "callingNum";
	final public String FILE_PATH = "filePath";
	String resultCode;
	String resultMsg;
	ArrayList<String> updateList = new ArrayList<String>();//callingNum, filePath
	public RingUpdateResult() {
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
	
	public List<String> getUpdateList() {
		return updateList;
	}
	
	public void setUpdateItem(String callingNum, String filePath) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(CALLING_NUM, callingNum);
		jsonObject.put(FILE_PATH, filePath);
		updateList.add(jsonObject.toString());
	}

	public void setUpdateList(ArrayList<String> updateList) {
		this.updateList = updateList;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(String.format("[%s][%s]", resultCode, resultMsg));
		for (String item : updateList) {
			JSONObject jobject = new JSONObject(item);
			String callingNum = jobject.getString(CALLING_NUM);
			String filePath   = jobject.getString(FILE_PATH);
			sb.append(String.format("{[%s]:[%s],[%s]:[%s]}", CALLING_NUM, callingNum, FILE_PATH, filePath));
			
		}

		return sb.toString();
	}
}

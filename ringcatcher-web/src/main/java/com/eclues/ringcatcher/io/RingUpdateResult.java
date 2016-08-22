package com.eclues.ringcatcher.io;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.eclues.ringcatcher.etc.Constant;

public class RingUpdateResult implements Serializable {

	private static final long serialVersionUID = -242858495909118917L;

	String resultCode;
	String resultMsg;
	ArrayList<String> updateList = new ArrayList<String>();//callingNum, filePath, expired_date, duration_type
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
	
	public void setUpdateItem(String callingNum, String filePath, String expiredDate, String durationType) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(Constant.CALLING_NUM.get(), callingNum);
		jsonObject.put(Constant.FILE_PATH.get(), filePath);
		jsonObject.put(Constant.EXPIRED_DATE.get(), expiredDate);
		jsonObject.put(Constant.DURATION_TYPE.get(), durationType);
		
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
			String callingNum = jobject.getString(Constant.CALLING_NUM.get());
			String filePath   = jobject.getString(Constant.FILE_PATH.get());
			String expiredDate   = jobject.getString(Constant.EXPIRED_DATE.get());
			String durationType   = jobject.getString(Constant.DURATION_TYPE.get());
			sb.append(String.format("{[%s]:[%s],[%s]:[%s]}", callingNum, filePath,expiredDate, durationType));
			
		}

		return sb.toString();
	}
}

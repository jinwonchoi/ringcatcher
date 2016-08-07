package com.gencode.ringcatcher.obj;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class ContactListResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String resultCode;
	String resultMsg;
	//String registeredContactList;
	String unregisteredContactList;
	ArrayList<String> registeredContactList = new ArrayList<String>();//userNum,callingNum,callingName,registerDate,expiredDate,jsonMsg,durationType,downloadCnt,updateDate;

	public ContactListResult() {
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
	
	public List<String> getRegisteredContactList() {
		return registeredContactList;
	}
	/*
	 * 	String defaultDurationType;
	String defaultJsonMessage;
	String defaultExpiredDate;
	String durationType;
	String jsonMessage;
	String expiredDate;
	
	 */
	public void setRegisteredContactList(String userNum, String defaultDurationType, String defaultJsonMessage,
				String defaultExpiredDate,String durationType,String jsonMessage,String expiredDate) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(Constant.USER_NUM.get(), userNum);
		jsonObject.put(Constant.DEFAULT_DURATION_TYPE.get(), defaultDurationType);
		jsonObject.put(Constant.DEFAULT_JSON_MESSAGE.get(), defaultJsonMessage);
		jsonObject.put(Constant.DEFAULT_EXPIRED_DATE.get(), defaultExpiredDate);
		jsonObject.put(Constant.DURATION_TYPE.get(), durationType);
		jsonObject.put(Constant.JSON_MESSAGE.get(), jsonMessage);
		jsonObject.put(Constant.EXPIRED_DATE.get(), expiredDate);
		
		registeredContactList.add(jsonObject.toString());
	}

	public String getUnregisteredContactList() {
		return unregisteredContactList;
	}

	public void setUnregisteredContactList(String unregisteredContactList) {
		this.unregisteredContactList = unregisteredContactList;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(String.format("[%s][%s]", resultCode, resultMsg));
		sb.append(String.format("unregistered[%s]", unregisteredContactList));
		
		for (String item : registeredContactList) {
			JSONObject jsonObject = new JSONObject(item);
			String userNum = jsonObject.getString(Constant.USER_NUM.get());
			String defaultDurationType = jsonObject.getString(Constant.DEFAULT_DURATION_TYPE.get());
			String defaultJsonMessage = jsonObject.getString(Constant.DEFAULT_JSON_MESSAGE.get());
			String defaultExpiredDate = jsonObject.getString(Constant.DEFAULT_EXPIRED_DATE.get());
			String durationType = jsonObject.getString(Constant.DURATION_TYPE.get());
			String jsonMessage = jsonObject.getString(Constant.JSON_MESSAGE.get());
			String expiredDate = jsonObject.getString(Constant.EXPIRED_DATE.get());
			sb.append(String.format("{[%s]:[%s]:[%s]:[%s]:[%s]:[%s]:[%s]}", userNum,defaultDurationType,
					defaultJsonMessage,defaultExpiredDate,durationType,jsonMessage,expiredDate
			));
			
		}

		return sb.toString();
	}

}

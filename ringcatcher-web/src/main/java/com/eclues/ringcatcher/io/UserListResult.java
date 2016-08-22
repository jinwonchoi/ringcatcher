package com.eclues.ringcatcher.io;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eclues.ringcatcher.etc.Constant;
import com.eclues.ringcatcher.obj.AdminInfo;
import com.eclues.ringcatcher.obj.MsgInfo;
import com.eclues.ringcatcher.obj.UserInfo;

public class UserListResult implements Serializable {
	private static final Logger logger = LoggerFactory.getLogger(UserListResult.class);
	private static final long serialVersionUID = 1L;
	String resultCode;
	String resultMsg;
	ArrayList<UserInfo> userList = new ArrayList<UserInfo>();//userNum,callingNum,callingName,registerDate,expiredDate,jsonMsg,durationType,downloadCnt,updateDate;

	public UserListResult() {
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
	
	public List<UserInfo> getUserList() {
		return userList;
	}

	public void setUserList(UserInfo userInfo) {
		userList.add(userInfo);
	}

	public void setUserList(ArrayList<UserInfo> userInfoList) {
		userList =  userInfoList;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(String.format("[%s][%s]", resultCode, resultMsg));
		
		for (UserInfo item : userList) {
			sb.append(String.format("{[%s]:[%s]:[%s]:[%s]:[%s]:[%s]:[%s]}", item.getUserNum(),item.getUserId(),
					item.getUserName(), item.getUserEmail(), item.getRecomId(), item.getUpdateDate(), item.getCreateDate()
			));
			
		}

		return sb.toString();
	}
}

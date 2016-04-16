package com.gencode.ringcatcher.obj;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RingUpdateResult implements Serializable {

	String resultCode;
	String resultMsg;
	Map<String, String> updateMap = new HashMap<String, String>();//callingNum, filePath
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
	

	public Map<String, String> getUpdateMap() {
		return updateMap;
	}

	public void setUpdateMap(Map<String, String> updateMap) {
		this.updateMap = updateMap;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(String.format("[%s][%s]", resultCode, resultMsg));
		for (String key : updateMap.keySet()) {
			String val = updateMap.get(key);
			sb.append(String.format("[%s][%s]", key, val));
		}

		return sb.toString();
	}
}

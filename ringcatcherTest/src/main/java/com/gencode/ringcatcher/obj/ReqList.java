package com.gencode.ringcatcher.obj;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map.Entry;

public class ReqList implements Serializable {

	HashMap<String, String> parameterMap = new HashMap<String, String>();
	
	public ReqList() {	}
	
	public String getValue(String key) {
		return parameterMap.get(key);
	}
	
	public void setValue(String key, String value) {
		parameterMap.put(key, value);
	}

	public HashMap<String, String> getMap() {
		return parameterMap;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("ReqList: ");
		for (Entry<String, String> entry :  parameterMap.entrySet()) {
			sb.append(String.format("{%s, %s}",entry.getKey(), entry.getValue()));
		}
		return sb.toString();
	}
}

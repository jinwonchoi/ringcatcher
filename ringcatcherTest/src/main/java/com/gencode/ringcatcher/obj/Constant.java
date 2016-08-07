package com.gencode.ringcatcher.obj;

public enum Constant {
	DEFAULT_USER_NUM("defaultnum"),
	STR_DEFAULT_USER_NUM("default user number"),
	CALLING_NUM("callingNum"),
	FILE_PATH("filePath"),
	EXPIRED_DATE("expiredDate"),
	DURATION_TYPE("durationType"),
	USER_NUM("userNum"),
	CALLING_NAME("callingName"),
	REGISTER_DATE("registerDate"),
	JSON_MSG("jsonMsg"),
	DOWNLOAD_CNT("downloadCnt"),
	UPDATE_DATE("updateDate"),

	DEFAULT_DURATION_TYPE("defaultDurationType"),
	DEFAULT_JSON_MESSAGE("defaultJsonMessage"),
	DEFAULT_EXPIRED_DATE("defaultExpiredDate"),
	JSON_MESSAGE("jsonMessage"),
	;
	private Constant(String str) {
		value = str;
	}
	
	private Constant(int val) {
		intValue = val;
	}
	
	private int intValue;
	private String value;
	
	public boolean equalValues(String str) {
		return (str == null)?false:value.equals(str);
	}
	
	public String get() {
		return value;
	}
	
	public int val() {
		return intValue;
	}
}

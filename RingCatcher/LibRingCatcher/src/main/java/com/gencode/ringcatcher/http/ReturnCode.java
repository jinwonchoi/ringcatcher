package com.gencode.ringcatcher.http;

/**
 * Created by Administrator on 2016-06-19.
 */
public enum ReturnCode {
    SUCCESS("0001"),
    UPDATE_OK("0002"),
    USER_NOT_FOUND("1001"),
    USER_EXISTS("1002"),
    RING_NO_UPDATE("1005"),
    ERROR_UNKNOWN("4001"),
    ERROR_FILEUP("4002"),
    ERROR_INVALID_USER("4003"),

    STR_SUCCESS("Sucess"),
    STR_UPDATE_OK("Update OK"),
    STR_USER_NOT_FOUND("User Not Found"),
    STR_USER_EXISTS("User Exists"),
    STR_RING_NO_UPDATE("No Ring Update"),
    STR_ERROR_UNKNOWN("Unknown Error"),
    STR_ERROR_FILEUP("Error in File upload"),
    STR_ERROR_INVALID_USER("Invalid User Error"),


    ;
    private ReturnCode(String str) {
        value = str;
    }

    private ReturnCode(int val) {
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
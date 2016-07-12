package com.gencode.ringcatcher.obj;

import java.io.Serializable;

public class RegisterMessageRequest implements Serializable {

    String tokenId;
    String friendPhoneNum;
    String callingPhoneNum;
    String callingNickName;
    String locale;
    String jsonMessage;

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getFriendPhoneNum() {
        return friendPhoneNum;
    }

    public void setFriendPhoneNum(String friendPhoneNum) {
        this.friendPhoneNum = friendPhoneNum;
    }

    public String getCallingPhoneNum() {
        return callingPhoneNum;
    }

    public void setCallingPhoneNum(String callingPhoneNum) {
        this.callingPhoneNum = callingPhoneNum;
    }

    public String getCallingNickName() {
        return callingNickName;
    }

    public void setCallingNickName(String callingNickName) {
        this.callingNickName = callingNickName;
    }

    public String getJsonMessage() {
        return jsonMessage;
    }

    public void setJsonMessage(String jsonMessage) {
        this.jsonMessage = jsonMessage;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    @Override
    public String toString() {
        return "RegisterMessageRequest{" +
                "tokenId='" + tokenId + '\'' +
                ", friendPhoneNum='" + friendPhoneNum + '\'' +
                ", callingPhoneNum='" + callingPhoneNum + '\'' +
                ", callingNickName='" + callingNickName + '\'' +
                ", locale='" + locale + '\'' +
                ", jsonMessage='" + jsonMessage + '\'' +
                '}';
    }
}

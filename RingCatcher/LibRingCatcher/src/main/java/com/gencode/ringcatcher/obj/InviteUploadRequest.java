package com.gencode.ringcatcher.obj;

import java.io.Serializable;

/**
 * Created by Administrator on 2016-04-17.
 */
public class InviteUploadRequest implements Serializable {
    String tokenId;
    String friendPhoneNum;
    String callingPhoneNum;
    String callingNickName;
    String locale;
    String filePath;

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

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return "InviteUploadRequest{" +
                "tokenId='" + tokenId + '\'' +
                ", friendPhoneNum='" + friendPhoneNum + '\'' +
                ", callingPhoneNum='" + callingPhoneNum + '\'' +
                ", callingNickName='" + callingNickName + '\'' +
                ", locale='" + locale + '\'' +
                ", filePath='" + filePath + '\'' +
                '}';
    }
}

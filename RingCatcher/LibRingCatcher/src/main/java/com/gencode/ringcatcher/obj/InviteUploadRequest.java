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
    String expiredDate;
    String durationType; /* A: 1회용, T: 기간 한정, P:기간무제한 */

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

    public String getDurationType() {
        return durationType;
    }

    public void setDurationType(String durationType) {
        this.durationType = durationType;
    }

    public String getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(String expiredDate) {
        this.expiredDate = expiredDate;
    }

    @Override
    public String toString() {
        return "InviteUploadRequest{" +
                "tokenId='" + tokenId + '\'' +
                ", friendPhoneNum='" + friendPhoneNum + '\'' +
                ", callingPhoneNum='" + callingPhoneNum + '\'' +
                ", callingNickName='" + callingNickName + '\'' +
                ", expiredDate='" + expiredDate + '\'' +
                ", durationType='" + durationType + '\'' +
                ", locale='" + locale + '\'' +
                ", filePath='" + filePath + '\'' +                '}';
    }
}

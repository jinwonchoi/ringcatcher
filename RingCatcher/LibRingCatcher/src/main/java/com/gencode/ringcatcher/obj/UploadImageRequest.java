package com.gencode.ringcatcher.obj;


import java.io.Serializable;

public class UploadImageRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    String tokenId;
    String friendPhoneNum;
    String callingPhoneNum;
    String callingNickName;
    String locale;
    String imageFileName;

    public UploadImageRequest() {	}

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

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String toString() {
        return "UploadImageRequest{" +
                "tokenId='" + tokenId + '\'' +
                ", friendPhoneNum='" + friendPhoneNum + '\'' +
                ", callingPhoneNum='" + callingPhoneNum + '\'' +
                ", callingNickName='" + callingNickName + '\'' +
                ", locale='" + locale + '\'' +
                ", imageFileName='" + imageFileName + '\'' +
                '}';
    }
}

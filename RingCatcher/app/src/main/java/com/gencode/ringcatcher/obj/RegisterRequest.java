package com.gencode.ringcatcher.obj;

import java.io.Serializable;

/**
 * Created by Administrator on 2016-04-17.
 */
public class RegisterRequest implements Serializable {
    String tokenId;
    String userPhoneNum;
    String userNick;
    String userEmail;
    String recommendPhoneNum;
    boolean overwrite;

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getUserPhoneNum() {
        return userPhoneNum;
    }

    public void setUserPhoneNum(String userPhoneNum) {
        this.userPhoneNum = userPhoneNum;
    }

    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getRecommendPhoneNum() {
        return recommendPhoneNum;
    }

    public void setRecommendPhoneNum(String recommendPhoneNum) {
        this.recommendPhoneNum = recommendPhoneNum;
    }

    public boolean isOverwrite() {
        return overwrite;
    }

    public void setOverwrite(boolean overwrite) {
        this.overwrite = overwrite;
    }

    @Override
    public String toString() {
        return "RegisterRequest{" +
                "tokenId='" + tokenId + '\'' +
                ", friendPhoneNum='" + userPhoneNum + '\'' +
                ", userNick='" + userNick + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", recommendPhoneNum='" + recommendPhoneNum + '\'' +
                ", overwrite=" + overwrite +
                '}';
    }
}

package com.gencode.ringcatcher.obj;

import java.io.Serializable;

/**
 * Created by Administrator on 2016-04-17.
 */
public class MessageRequest implements Serializable {
    String userid;
    String userNum;
    String callingNum;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserNum() {
        return userNum;
    }

    public void setUserNum(String userNum) {
        this.userNum = userNum;
    }

    public String getCallingNum() {
        return callingNum;
    }

    public void setCallingNum(String callingNum) {
        this.callingNum = callingNum;
    }

    public String toString() {
        return String.format("[%s][%s][%s]", userid, userNum, callingNum);
    }
}

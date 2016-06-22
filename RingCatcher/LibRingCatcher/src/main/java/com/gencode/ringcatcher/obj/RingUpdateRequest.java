package com.gencode.ringcatcher.obj;

import java.io.Serializable;

/**
 * Created by Administrator on 2016-04-17.
 */
public class RingUpdateRequest implements Serializable {
    String userid;
    String userNum;

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
}

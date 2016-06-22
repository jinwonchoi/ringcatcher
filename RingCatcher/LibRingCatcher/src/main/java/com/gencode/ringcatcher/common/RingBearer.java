package com.gencode.ringcatcher.common;

/**
 * 링등록 관련 정보 보관
 * Created by Administrator on 2016-04-16.
 */
public class RingBearer {
    private static RingBearer ourInstance = new RingBearer();

    public static RingBearer getInstance() {
        return ourInstance;
    }

    private RingBearer() {
    }
    String tokenId;
    String myPhoneNumber;
    String myPhoneNick;
    String friendPhoneNumber;
    String friendNick;
    String mediaUrl;

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getMyPhoneNumber() {
        return myPhoneNumber;
    }

    public void setMyPhoneNumber(String myPhoneNumber) {
        this.myPhoneNumber = myPhoneNumber;
    }

    public String getMyPhoneNick() {
        return myPhoneNick;
    }

    public void setMyPhoneNick(String myPhoneNick) {
        this.myPhoneNick = myPhoneNick;
    }

    public String getFriendPhoneNumber() {
        return friendPhoneNumber;
    }

    public void setFriendPhoneNumber(String friendPhoneNumber) {
        this.friendPhoneNumber = friendPhoneNumber;
    }

    public String getFriendNick() {
        return friendNick;
    }

    public void setFriendNick(String friendNick) {
        this.friendNick = friendNick;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }
    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }
}

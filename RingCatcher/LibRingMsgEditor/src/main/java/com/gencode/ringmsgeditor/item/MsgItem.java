package com.gencode.ringmsgeditor.item;

/**
 * Created by Administrator on 2016-05-31.
 */
/**
 * A dummy item representing a piece of content.
 */
public class MsgItem {
    public final String id;
    public final String contentUrl;
    public final String details;

    public MsgItem(String id, String contentUrl, String details) {
        this.id = id;
        this.contentUrl = contentUrl;
        this.details = details;
    }

    @Override
    public String toString() {
        if (contentUrl != null) return contentUrl;
        else return details;
    }
}
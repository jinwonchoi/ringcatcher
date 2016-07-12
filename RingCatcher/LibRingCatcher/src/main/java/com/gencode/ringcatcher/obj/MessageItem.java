package com.gencode.ringcatcher.obj;

import android.util.Size;

/**
 * Created by Administrator on 2016-06-22.
 */
public class MessageItem {
    public int SCALE_X = 0;
    public int SCALE_Y = 1;
    public int TYPE_TEXT = 10;
    public int TYPE_IMAGE = 11;

    public float scaleX;
    public float scaleY;

    public String imageStr;
    public String messageStr;
    //

    /**
     *
     * @param key   format : "%d:img" or "%d:txt"
     * @param value format : "string" or "image_url;xscale;yscale"
     */
    public MessageItem(String key, String value) {

    }

}

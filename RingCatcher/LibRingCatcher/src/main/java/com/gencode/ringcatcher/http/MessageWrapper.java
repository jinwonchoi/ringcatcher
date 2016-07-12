package com.gencode.ringcatcher.http;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrator on 2016-06-11.
 */
public class MessageWrapper {
    final String TAG = this.getClass().getName();
    /* jsonMessage로 encode되어 실림
    {"message_from":"01044445555",
       "message_to":"01044445555"
       "ring_message":{
        "1:txt":"firstMessage"
       ,"2:img":"http://t1.daumcdn.net/news/201501/16/sportschosun/20150116083911939.jpeg;.45;.45"
       ,"3:txt":"thirdMessage"
       ,"4:img":"http://paitao358g.com/data/file/yaboard/1889778108_DXZgoKzp_f463bcf0f5715209ac70e54194eaabc0a250d944.png"
       ,"5:img":"http://appzzang.ca/data/file/hot/3718535273_NGKDQCfl_JqWaQEZ.jpg;0.9;0.9"
       ,"6:txt":"여섯번째 메시지"
    }
        }
    */
    String mStrJson;
    String mMessageFrom;
    String mMessageTo;
    Map<String, String> mMessageList = new HashMap<String, String>();
    JSONObject mJsonObject = new JSONObject();

    public MessageWrapper(String messageFrom, String messageTo, Map<String, String> messageList) throws JSONException{
        this.mMessageFrom = messageFrom;
        this.mMessageTo = messageTo;
        this.mMessageList = messageList;

        mJsonObject.put("message_from", messageFrom);
        mJsonObject.put("message_to", messageTo);
        JSONObject jsonObject = new JSONObject();
        for (Map.Entry<String, String> entry : messageList.entrySet())
        {
            jsonObject.put(entry.getKey(),entry.getValue());
        }
        mJsonObject.put("ring_message",jsonObject);
    }

    public MessageWrapper(String strJson, boolean isEncoded) throws JSONException {
        if (isEncoded) {
            mJsonObject = Utils.getJSONObjectFromBase64(strJson);
        } else {
            mJsonObject = new JSONObject(strJson);
        }
        this.mMessageFrom = mJsonObject.getString("message_from");
        this.mMessageTo = mJsonObject.getString("message_to");
        JSONObject jsonMap =  mJsonObject.getJSONObject("ring_message");
        Iterator<String> keys = jsonMap.keys();
        while(keys.hasNext()) {
            String key = (String)keys.next();
            mMessageList.put(key, (String)jsonMap.get(key));
        }
    }

    public String getMessageFrom() {
        return mMessageFrom;
    }

    public String getMessageTo() {
        return mMessageTo;
    }

    public Map<String, String> getMessageList() { return mMessageList;}

    public JSONObject toJSONObject() {
        return mJsonObject;
    }

    public String toString() {
        return mJsonObject.toString();
    }

    public String toEncodedString() {
        return Utils.getBase64StringFromJSONObject(mJsonObject);
    }
}

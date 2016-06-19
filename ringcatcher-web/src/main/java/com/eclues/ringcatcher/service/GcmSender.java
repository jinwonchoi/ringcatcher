package com.eclues.ringcatcher.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eclues.ringcatcher.ctrl.JSONController;
import com.eclues.ringcatcher.dao.EnvironmentBean;
import com.fasterxml.jackson.core.JsonParser;

@Service("gcmSender")
public class GcmSender {
	@Autowired
	private EnvironmentBean environmentBean;

	//public static final String API_KEY; = envi 
			//"AIzaSyA5hfsRi_iJ_8yfWhYvGSez5NV4DGiiXNo";
	private static final Logger logger = LoggerFactory.getLogger(JSONController.class);

	private boolean _call(String tokenId, JSONObject jData, JSONObject jNoti) {
    	boolean result = false;
        try {
            // Prepare JSON containing the GCM message content. What to send and where to send.
            JSONObject jGcmData = new JSONObject();
            // Where to send GCM message.
            // What to send in GCM message.
            jGcmData.put("data", jData);
            // Create connection to send GCM Message request.
            jGcmData.put("to", tokenId);
            //jGcmData.put("notification", jNoti);
            URL url = new URL("https://android.googleapis.com/gcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "key=" + environmentBean.getGcmApiKey());
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            logger.info("gcm api key="+environmentBean.getGcmApiKey());
            logger.info("gcm token id="+tokenId);
            logger.info("gcm data="+jGcmData);
            // Send GCM message content.
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jGcmData.toString().getBytes());

            // Read GCM response.
            InputStream inputStream = conn.getInputStream();
            String resp = IOUtils.toString(inputStream);
            logger.info("GcmSender.Response:"+resp);
            //{"multicast_id":7547691688482327412,"success":1,"failure":0,"canonical_ids":0
            // ,"results":[{"message_id":"0:1460554067079359%744ab298f9fd7ecd"}]}
            JSONParser parser = new JSONParser(); 
            JSONObject jobject = (JSONObject)parser.parse(resp);
            String successResult = jobject.get("success").toString();
            JSONArray messageArray = (JSONArray)jobject.get("results");
            if (successResult.equals("1")) {
            	logger.info("gcm call successful : ");
            } else {
            	logger.info("gcm call failed : ");
            }
            
            result = true;
        } catch (IOException e) {
            logger.error("Unable to send GCM message:",e);
        } catch (ParseException pe) {
            logger.error("Unable to parse GCM message:",pe);
		}
        return result;
	}
	
    public boolean call(Object data, String tokenId, String callingNum, String callingName, String locale) {
    	boolean result = false;
        // Prepare JSON containing the GCM message content. What to send and where to send.
        JSONObject jData = new JSONObject();
        jData.put("message", data);
        JSONObject jNoti = new JSONObject();
        if (Locale.KOREA.toString().equals(locale)) {
            jNoti.put("body", "어플에서 확인하세요.");
            jNoti.put("title", String.format("<%s>(%s)님이 벨소리 변경을 요청하셨습니다.", callingName,callingNum));
            jNoti.put("icon", "myicon");
        } else {
            jNoti.put("body", "Please check your Ringcatcher's updates.");
            jNoti.put("title", String.format("You have a new ring sound from <%s>(%s).", callingName,callingNum));
            jNoti.put("icon", "myicon");
        }
        return _call(tokenId, jData, jNoti);
	}

    public boolean callMessage(Object data, String tokenId, String callingNum, String callingName, String locale) {
    	boolean result = false;
        // Prepare JSON containing the GCM message content. What to send and where to send.
        JSONObject jData = new JSONObject();
        jData.put("message", data);
        JSONObject jNoti = new JSONObject();
        if (Locale.KOREA.toString().equals(locale)) {
            jNoti.put("body", "어플에서 확인하세요.");
            jNoti.put("title", String.format("<%s>(%s)님의 메시지가 도착했습니다.", callingName,callingNum));
            jNoti.put("icon", "myicon");
        } else {
            jNoti.put("body", "Please check your Ringcatcher's updates.");
            jNoti.put("title", String.format("You have a new message from <%s>(%s).", callingName,callingNum));
            jNoti.put("icon", "myicon");
        }
        return _call(tokenId, jData, jNoti);
	}

}

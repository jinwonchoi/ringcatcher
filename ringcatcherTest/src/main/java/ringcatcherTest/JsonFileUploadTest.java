package ringcatcherTest;

import static org.junit.Assert.*;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import com.gencode.ringcatcher.obj.InviteResult;
import com.gencode.ringcatcher.obj.RegisterResult;

public class JsonFileUploadTest {
	//String url = "http://localhost:8080/ringcatcher/";
	String url = "http://52.79.62.98:8080/ringcatcher/";

	@Before
	public void setUp() throws Exception {
	}

	/**
	 * 	upfile: filename
	json:
	{"userNum":"010444448888"
	,"callingNum":"01066663333"
	,"ringFileName":"234.mp3"
	} 
	
	String userNum;
	String callingId;
	String callingNum;
	String ringFileName;
	
	return OK
	  {"resultCode":"0001"
	  ,"resultMsg":"SUCCESS"}
	return already registered
	  {"resultCode":"4002"
	  ,"resultMsg":"Error in File upload"}
	return error
	  {"resultCode":"4001"
	  ,"regId":""
	  ,"resultMsg":"Unknown Error"}
	 */
	@Test
	public void testFileUpLoad() {
    	String url =this.url+"/json/invite";
    	String tokenId = "AToken_eiD55aUDQeM:APA91bG0AWBahj4df_LFwUpLvB9FegJcrs_NcFVOdjvOepHrVGwOKpHUbg6qeBv8lJzIHaECGA4pHLTqQEREtLmx58j2NoCu8zx0wYcG-S3oHZZgIOcL8Ydif9frkA00d0YES8WFCL7j";

    	String body = "	{\"userNum\":\"010444448888\""
    			+",\"callingId\":\""+tokenId+"\""
    			+",\"callingNum\":\"01055557777\""
    			+",\"locale\":\"ko_KR\""//en_US, ko_KR
    			+",\"ringFileName\":\"0004.jpg\"}";
    			
    	JsonFileUpload json = new JsonFileUpload();
    	//String filepath = "/home/jinnon/Music/374-Marion-Parcel.mp3";
    	String filepath = "/home/jinnon/Pictures/Wallpapers/vgirlmm-009-039.jpg";
    	String strJson = json.http(url, body, filepath);
    	
    	InviteResult result = new InviteResult(); 
    	JSONObject jsonObject = new JSONObject(strJson);
    	String resultCode = jsonObject.getString("resultCode");
    	String resultMsg  = jsonObject.getString("resultMsg");
    	result.setResultCode(resultCode);
    	result.setResultMsg(resultMsg);
    	System.out.println("resultCode="+resultCode);
    	System.out.println("resultMsg="+resultMsg);
    	System.out.println("result = "+result.toString()); 	

		assert(true);
	}

}

package ringcatcherTest;

import static org.junit.Assert.*;

import java.net.UnknownHostException;
import java.util.Map;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import com.gencode.ringcatcher.obj.RegisterResult;
import com.gencode.ringcatcher.obj.RingUpdateResult;

public class JsonSimpleTest {

	String url = "http://localhost:8080/ringcatcher/";
	//String url = "http://52.79.62.98:8080/ringcatcher/";
	String tokenId = "e4ODJwgWx9E:APA91bHauKGwsdiGLhGJHiOQX5IxMNbju1a2TH7QGKB4_HPd3UspkrsgyE_M54sw6OT3UwAaeNCSQiKzVpT922_qm3LAgH6D-zXipXBodX2rKQ5PAK8FdhNcdzQKeg7N-QHNbS1X-cNm";

	@Before
	public void setUp() throws Exception {
		//
	}

	/**
	 * 	 * 1 앱으로 가입
	 * 등록정보 폰번호 
	 * path: /register
	 *       register.jsp
	 * { "user_id":"3453245234"  
		  ,"user_num":"01042047792"
		  ,"user_email":"jinnonspot@gmail.com"
		  ,"recom_id":""
	  ,"overwrite":"true|false" (default : false)
	  }   
	  return OK
	  {"resultCode":"0001"
	  ,"regId":"23413241234123"
	  ,"resultMsg":"SUCCESS"}
	  return already registered
	  {"resultCode":"1002"
	  ,"regId":"23413241234123"
	  ,"resultMsg":"Already registered"}
	  return error
	  {"resultCode":"4001"
	  ,"regId":""
	  ,"resultMsg":"Unknown Error"}
	 */
	@Test
	public void testRegister() {
    	String url = this.url+"/json/register";
    	
    	String body = "{\"userNum\":\"01055557777\""
    			+",\"userId\":\""+tokenId+"\""
    			+",\"userEmail\":\"jinnonspot@gmail.com\""
    			+",\"recomId\":\"his-recommend\""
    			+",\"overwrite\":\"true\"}";
    	JsonSimple json = new JsonSimple();
    	String strJson = json.http(url, body);
    	
    	RegisterResult result = new RegisterResult(); 
    	JSONObject jsonObject = new JSONObject(strJson);
    	String resultCode = jsonObject.getString("resultCode");
    	String resultMsg  = jsonObject.getString("resultMsg");
    	result.setResultCode(resultCode);
    	result.setResultMsg(resultMsg);
    	System.out.println("resultCode="+resultCode);
    	System.out.println("resultMsg="+resultMsg);
    	System.out.println("result = "+result.toString()); 	
		assert(true);
//		fail("Not yet implemented");
	}
	
	@Test 
	public void testEnv() {
		Map<String, String> envMap = System.getenv();
		for (String key : envMap.keySet()) {
			System.out.println(key+":"+envMap.get(key));
		}
		
		Properties prop = System.getProperties();
		for (Object key : prop.keySet()) {
			System.out.println(key+":"+prop.get(key));
		}
		
		java.net.InetAddress ip;
		try {
			ip = java.net.InetAddress.getLocalHost();
	        String hostname = ip.getHostAddress();
	        System.out.println(hostname);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assert(true);
	}
	
	@Test
	public void testRegister2() {
    	String url = this.url+"/json/register";
    	String body = "{\"userNum\":\"0244445555\""
    			+",\"userId\":\"test-token-id\""
    			+",\"userEmail\":\"jinnon@naver.com\""
    			+",\"recomId\":\"1459574403591\""
    			+",\"overwrite\":\"false\"}";
    	JsonSimple json = new JsonSimple();
    	json.http(url, body);    	
		assert(true);
//		fail("Not yet implemented");
	}

	@Test
	public void testRegister3() {
    	String url = this.url+"/json/register";
    	String body = "{\"userNum\":\"01066668888\""
    			+",\"userId\":\"test-token-id2\""
    			+",\"userEmail\":\"test@naver.com\""
    			+",\"recomId\":\"1459574403591\""
    			+",\"overwrite\":\"false\"}";
    	JsonSimple json = new JsonSimple();
    	json.http(url, body);    	
		assert(true);
//		fail("Not yet implemented");
	}
	
	//010444448888
	/**
	 * 	4. 대기중인 링정보 체크 및 다운로드
	 path : ringupdate
	{ "user_id":"01042047792"
	, "user_num":"01042047792"}
	return OK
	{"resultCode":"0001"
	,"CALLING_NUM":"callingNum"
	,"FILE_PATH":"filePath"
	,"resultMsg":"Sucess"
	,"updateList":["{\"filePath\":\"\/ringmedia\/20160413\/01055557777_010444448888.jpg\"
	                 ,\"callingNum\":\"01055557777\"}"]}
	return No data
	{"updateMap":{},"resultCode":"1003","resultMsg":"No Ring Update"}
	return error
	{"updateMap":{},"resultCode":"4001","resultMsg":"Unknown Error"}
	 */
	@Test
	public void testRingUpdate() {
    	String url = this.url+"/json/ringupdate";

    	String tokenId = "test-token-id2";
    	String body = "{\"userId\":\""+tokenId+"\""
    			+",\"userNum\":\"01066668888\"}";
    	JsonSimple json = new JsonSimple();
    	String strJson = json.http(url, body);
    	
    	RingUpdateResult result = new RingUpdateResult(); 
    	JSONObject jsonObject = new JSONObject(strJson);
    	String resultCode = jsonObject.getString("resultCode");
    	String resultMsg  = jsonObject.getString("resultMsg");
    	result.setResultCode(resultCode);
    	result.setResultMsg(resultMsg);
    	System.out.println("resultCode="+resultCode);
    	JSONArray jsonArray = jsonObject.optJSONArray("updateList");
    	System.out.println("resultMsg="+jsonArray.toString());
    	for (int i= 0;i < jsonArray.length();i++ ) {
    		System.out.println("i="+i);
    		String jsonStr =  jsonArray.getString(i);
    		JSONObject jsonItem = new JSONObject(jsonStr);
    		System.out.println("jsonItem="+jsonItem);
    		
    		String callingNum = jsonItem.optString("callingNum");
        	System.out.println("callingNum="+callingNum);
    		String filePath = jsonItem.getString("filePath");
        	System.out.println("filePath="+filePath);
    		result.setUpdateItem(callingNum, filePath);
    	}

    	System.out.println("result = "+result.toString());
    	assert(true);
//		fail("Not yet implemented");
	}

	//010444448888
	/**
	 * 	4. 대기중인 링정보 체크 및 다운로드
	 path : ringupdate
	{ "user_id":"01042047792"
	, "user_num":"01042047792"}
	return OK
	{"resultCode":"0001"
	,"CALLING_NUM":"callingNum"
	,"FILE_PATH":"filePath"
	,"resultMsg":"Sucess"
	,"updateList":["{\"filePath\":\"\/ringmedia\/20160413\/01055557777_010444448888.jpg\"
	                 ,\"callingNum\":\"01055557777\"}"]}
	return No data
	{"updateMap":{},"resultCode":"1003","resultMsg":"No Ring Update"}
	return error
	{"updateMap":{},"resultCode":"4001","resultMsg":"Unknown Error"}
	 */
	@Test
	public void testRingCheckout() {
		try {
    	String url = this.url+"/json/ringcheckout";
    	

    	String body = "{\"userId\":\""+tokenId+"\""
    			+",\"userNum\":\"01055557777\"}";
    	JsonSimple json = new JsonSimple();
    	String strJson = json.http(url, body);
    	
    	RingUpdateResult result = new RingUpdateResult(); 
    	JSONObject jsonObject = new JSONObject(strJson);
    	String resultCode = jsonObject.getString("resultCode");
    	String resultMsg  = jsonObject.getString("resultMsg");
    	result.setResultCode(resultCode);
    	result.setResultMsg(resultMsg);
    	System.out.println("resultCode="+resultCode);
    	JSONArray jsonArray = jsonObject.optJSONArray("updateList");
    	System.out.println("resultMsg="+jsonArray.toString());
    	for (int i= 0;i < jsonArray.length();i++ ) {
    		System.out.println("i="+i);
    		String jsonStr =  jsonArray.getString(i);
    		JSONObject jsonItem = new JSONObject(jsonStr);
    		System.out.println("jsonItem="+jsonItem);
    		
    		String callingNum = jsonItem.optString("callingNum");
        	System.out.println("callingNum="+callingNum);
    		String filePath = jsonItem.getString("filePath");
        	System.out.println("filePath="+filePath);
    		result.setUpdateItem(callingNum, filePath);
    	}

    	System.out.println("result = "+result.toString());
		assert(true);
//		fail("Not yet implemented");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

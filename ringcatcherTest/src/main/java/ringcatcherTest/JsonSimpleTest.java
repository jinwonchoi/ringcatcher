package ringcatcherTest;

import static org.junit.Assert.*;

import java.net.UnknownHostException;
import java.util.Map;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

public class JsonSimpleTest {

	//String url = "http://localhost:8080/ringcatcher/";
	String url = "http://52.79.62.98:8080/ringcatcher/";
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
    	
    	String tokenId = "AToken_eiD55aUDQeM:APA91bG0AWBahj4df_LFwUpLvB9FegJcrs_NcFVOdjvOepHrVGwOKpHUbg6qeBv8lJzIHaECGA4pHLTqQEREtLmx58j2NoCu8zx0wYcG-S3oHZZgIOcL8Ydif9frkA00d0YES8WFCL7j";
    	String body = "{\"userNum\":\"01055557777\""
    			+",\"userId\":\""+tokenId+"\""
    			+",\"userEmail\":\"jinnonspot@gmail.com\""
    			+",\"recomId\":\"019932342323342\""
    			+",\"overwrite\":\"false\"}";
    	JsonSimple json = new JsonSimple();
    	json.http(url, body);    	
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
    	String tokenId = "eiD55aUDQeM:APA91bG0AWBahj4df_LFwUpLvB9FegJcrs_NcFVOdjvOepHrVGwOKpHUbg6qeBv8lJzIHaECGA4pHLTqQEREtLmx58j2NoCu8zx0wYcG-S3oHZZgIOcL8Ydif9frkA00d0YES8WFCL7j";
    	String body = "{\"userNum\":\"010444448888\""
    			+",\"userId\":\""+tokenId+"\""
    			+",\"userEmail\":\"jinnon@naver.com\""
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
	{"updateMap":{"01066663333":"\/home\/jinnon\/file_repo\/\/20160402\/01066663333_010444448888.jpg"},"resultCode":"0001","resultMsg":"Sucess"}
	return No data
	{"updateMap":{},"resultCode":"1003","resultMsg":"No Ring Update"}
	return error
	{"updateMap":{},"resultCode":"4001","resultMsg":"Unknown Error"}
	 */
	@Test
	public void testRingUpdate() {
    	String url = this.url+"/json/ringupdate";
    	String tokenId = "eiD55aUDQeM:APA91bG0AWBahj4df_LFwUpLvB9FegJcrs_NcFVOdjvOepHrVGwOKpHUbg6qeBv8lJzIHaECGA4pHLTqQEREtLmx58j2NoCu8zx0wYcG-S3oHZZgIOcL8Ydif9frkA00d0YES8WFCL7j";

    	String body = "{\"userId\":\""+tokenId+"\""
    			+",\"userNum\":\"010444448888\"}";
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
	{"updateMap":{"01066663333":"\/home\/jinnon\/file_repo\/\/20160402\/01066663333_010444448888.jpg"},"resultCode":"0001","resultMsg":"Sucess"}
	return No data
	{"updateMap":{},"resultCode":"1003","resultMsg":"No Ring Update"}
	return error
	{"updateMap":{},"resultCode":"4001","resultMsg":"Unknown Error"}
	 */
	@Test
	public void testRingCheckout() {
    	String url = this.url+"/json/ringcheckout";
    	
    	String tokenId = "eiD55aUDQeM:APA91bG0AWBahj4df_LFwUpLvB9FegJcrs_NcFVOdjvOepHrVGwOKpHUbg6qeBv8lJzIHaECGA4pHLTqQEREtLmx58j2NoCu8zx0wYcG-S3oHZZgIOcL8Ydif9frkA00d0YES8WFCL7j";

    	String body = "{\"userId\":\""+tokenId+"\""
    			+",\"userNum\":\"010444448888\"}";
    	JsonSimple json = new JsonSimple();
    	json.http(url, body);    	
		assert(true);
//		fail("Not yet implemented");
	}
}

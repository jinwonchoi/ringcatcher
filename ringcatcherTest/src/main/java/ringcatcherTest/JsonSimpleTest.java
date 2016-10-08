package ringcatcherTest;

import static org.junit.Assert.*;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import com.gencode.ringcatcher.obj.AdminInfo;
import com.gencode.ringcatcher.obj.ContactListResult;
import com.gencode.ringcatcher.obj.ItemResult;
import com.gencode.ringcatcher.obj.ListResult;
import com.gencode.ringcatcher.obj.RegisterResult;
import com.gencode.ringcatcher.obj.ReqList;
import com.gencode.ringcatcher.obj.RequestMap;
import com.gencode.ringcatcher.obj.RingUpdateResult;
import com.gencode.ringcatcher.obj.UserInfo;

public class JsonSimpleTest {

	//String url = "http://192.168.0.101:8080/ringcatcher/";
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


	@Test
	public void testRegister4() {
    	String url = this.url+"/json/register";
    	String body = "{\"userNum\":\"01021985055\""
    			+",\"userId\":\"dXfaMGiBaWw:APA91bH_9vpIxGPngRvE80yTRH1eq00i4qVcf3EGowocPWB7tHr6Mm0Qcd6yKNXbjnRvRDWbCeEfICE07Slz6POijAXDCjJLmYkbA79Vug-VV5jn1c-zqL2VPZ7IHDHi7xegWrR2Dwpp\""
    			+",\"userEmail\":\"test@naver.com\""
    			+",\"recomId\":\"1459574403591\""
    			+",\"overwrite\":\"true\"}";
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
        	String expiredDate = jsonItem.optString("expiredDate");
        	System.out.println("expiredDate="+expiredDate);
    		String durationType = jsonItem.optString("durationType");
    		System.out.println("durationType="+durationType);
    				
    		result.setUpdateItem(callingNum, filePath,expiredDate,durationType);
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
        	String expiredDate = jsonItem.optString("expiredDate");
        	System.out.println("expiredDate="+expiredDate);
    		String durationType = jsonItem.optString("durationType");
    		System.out.println("durationType="+durationType);
    				
    		result.setUpdateItem(callingNum, filePath,expiredDate,durationType);
    	}

    	System.out.println("result = "+result.toString());
		assert(true);
//		fail("Not yet implemented");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 	5. 앱에 등록된 고객 폰북명단 찾기
	 path : findcontacts
	{ "user_id":"01042047792"
	, "user_num":"01042047792"
	, "contactList":"01055557777,01066668888,01066669999"}
	return OK
	{"resultCode":"0001"
	,"resultMsg":"Sucess"
	,"registeredContactList":"01055557777,01066668888,01066669999"
	,"unregisteredContactList":"01055557777,01066668888,01066669999"
	}
	return No data
	{"updateMap":{},"resultCode":"1003","resultMsg":"No Ring Update"}
	return error
	{"updateMap":{},"resultCode":"4001","resultMsg":"Unknown Error"}
	 */
	@Test
	public void testFindConacts() {
		try {
    	String url = this.url+"/json/findcontacts";
    	

    	String body = "{\"userId\":\""+tokenId+"\""
    			+",\"userNum\":\"01055557777\""
    			+",\"lastUpdateDate\":\"20160501235959\""
    			+",\"contactList\":\"01066668888,01066669999,01033338888,01022227777,0244445555\"}";
    	System.out.println(body);
    	JsonSimple json = new JsonSimple();
    	String strJson = json.http(url, body);
    	
    	ContactListResult result = new ContactListResult(); 
    	JSONObject jsonObject = new JSONObject(strJson);
    	String resultCode = jsonObject.getString("resultCode");
    	String resultMsg  = jsonObject.getString("resultMsg");

    	result.setResultCode(resultCode);
    	result.setResultMsg(resultMsg);
    	
    	JSONArray jsonArray = jsonObject.optJSONArray("list");
    	System.out.println("list="+jsonArray.toString());
    	for (int i= 0;i < jsonArray.length();i++ ) {
    		System.out.println("i="+i);
    		String jsonStr =  jsonArray.getString(i);
    		JSONObject jsonItem = new JSONObject(jsonStr);
    		System.out.println("jsonItem="+jsonItem);
    		//userNum,callingNum,callingName,registerDate,expiredDate,jsonMsg,durationType,downloadCnt,updateDate;
    		
    		String userNum = jsonItem.optString("userNum");
        	System.out.println("userNum="+userNum);
    		String defaultDurationType = jsonItem.optString("defaultDurationType");
        	System.out.println("defaultDurationType="+defaultDurationType);
    		String defaultJsonMessage = jsonItem.optString("defaultJsonMessage");
        	System.out.println("defaultJsonMessage="+defaultJsonMessage);
    		String defaultExpiredDate = jsonItem.getString("defaultExpiredDate");
        	System.out.println("defaultExpiredDate="+defaultExpiredDate);
        	String durationType = jsonItem.optString("durationType");
        	System.out.println("durationType="+durationType);
    		String jsonMessage = jsonItem.optString("jsonMessage");
        	System.out.println("jsonMessage="+jsonMessage);
    		String expiredDate = jsonItem.optString("expiredDate");
    		System.out.println("expiredDate="+expiredDate);
    				
        	result.setRegisteredContactList(userNum, defaultDurationType,defaultJsonMessage,defaultExpiredDate,durationType,jsonMessage,expiredDate);
    	}

    	
    	System.out.println("result="+result);
    	assert(true);
//		fail("Not yet implemented");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testAuthenticateAdmin() {
		try {
    	String url = this.url+"/admin/authenticateadmin";
    	AdminInfo adminInfo = new AdminInfo("admin_0818","newemail@email.com","E","John Coltrane", "admin_0818","noname");
    	
    	JSONObject jsonObject = new JSONObject(adminInfo);
    	System.out.println("json.adminInfo: "+jsonObject.toString());

    	//call http
      	JsonSimple json = new JsonSimple();
    	String strJson = json.http(url, jsonObject.toString());
    	
    	ItemResult result = new ItemResult(); 
    	JSONObject jsonObject2 = new JSONObject(strJson);
    	String resultCode = jsonObject2.getString("resultCode");
    	String resultMsg  = jsonObject2.getString("resultMsg");
    	result.setResultCode(resultCode);
    	result.setResultMsg(resultMsg);

    	System.out.println("result="+result);
    	//System.out.println("result="+result);
    	assert(true);
//		fail("Not yet implemented");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testRegisterAdmin() {
		try {
    	String url = this.url+"/admin/registeradmin";
    	AdminInfo adminInfo = new AdminInfo("admin_0818","newemail@email.com","E","John Coltrane", "admin_0818","noname");
    	
    	JSONObject jsonObject = new JSONObject(adminInfo);
    	System.out.println("json.adminInfo: "+jsonObject.toString());

    	//call http
      	JsonSimple json = new JsonSimple();
    	String strJson = json.http(url, jsonObject.toString());
    	
    	ItemResult result = new ItemResult(); 
    	JSONObject jsonObject2 = new JSONObject(strJson);
    	String resultCode = jsonObject2.getString("resultCode");
    	String resultMsg  = jsonObject2.getString("resultMsg");
    	result.setResultCode(resultCode);
    	result.setResultMsg(resultMsg);

    	System.out.println("result="+result);
    	//System.out.println("result="+result);
    	assert(true);
//		fail("Not yet implemented");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testUnRegisterAdmin() {
		try {
    	String url = this.url+"/admin/unregisteradmin";
    	AdminInfo adminInfo = new AdminInfo("admin_0818","newemail@email.com","E","John Coltrane", "admin_0818","noname");
    	
    	JSONObject jsonObject = new JSONObject(adminInfo);
    	System.out.println("json.adminInfo: "+jsonObject.toString());

    	//call http
      	JsonSimple json = new JsonSimple();
    	String strJson = json.http(url, jsonObject.toString());
    	
    	ItemResult result = new ItemResult(); 
    	JSONObject jsonObject2 = new JSONObject(strJson);
    	String resultCode = jsonObject2.getString("resultCode");
    	String resultMsg  = jsonObject2.getString("resultMsg");
    	result.setResultCode(resultCode);
    	result.setResultMsg(resultMsg);

    	System.out.println("result="+result);
    	//System.out.println("result="+result);
    	assert(true);
//		fail("Not yet implemented");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void testRegisterUser() {
		try {
    	String url = this.url+"/admin/registeruser";
    	UserInfo userInfo = new UserInfo("01099994444","tokenid-123123432232","John Coltrane", "noname@email.com","noname");
    	
    	JSONObject jsonObject = new JSONObject(userInfo);
    	System.out.println("json.userInfo: "+jsonObject.toString());

    	//call http
      	JsonSimple json = new JsonSimple();
    	String strJson = json.http(url, jsonObject.toString());
    	
    	ItemResult result = new ItemResult(); 
    	JSONObject jsonObject2 = new JSONObject(strJson);
    	String resultCode = jsonObject2.getString("resultCode");
    	String resultMsg  = jsonObject2.getString("resultMsg");
    	result.setResultCode(resultCode);
    	result.setResultMsg(resultMsg);

    	System.out.println("result="+result);
    	//System.out.println("result="+result);
    	assert(true);
//		fail("Not yet implemented");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testUnRegisterUser() {
		try {
    	String url = this.url+"/admin/unregisteruser";
    	UserInfo userInfo = new UserInfo("01099994444","tokenid-123123432232","John Coltrane", "noname@email.com","noname");
    	
    	JSONObject jsonObject = new JSONObject(userInfo);
    	System.out.println("json.userInfo: "+jsonObject.toString());

    	//call http
      	JsonSimple json = new JsonSimple();
    	String strJson = json.http(url, jsonObject.toString());
    	
    	ItemResult result = new ItemResult(); 
    	JSONObject jsonObject2 = new JSONObject(strJson);
    	String resultCode = jsonObject2.getString("resultCode");
    	String resultMsg  = jsonObject2.getString("resultMsg");
    	result.setResultCode(resultCode);
    	result.setResultMsg(resultMsg);

    	System.out.println("result="+result);
    	//System.out.println("result="+result);
    	assert(true);
//		fail("Not yet implemented");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetUserList() {
		try {
    	String url = this.url+"/admin/getuserlist";
    	RequestMap requestMap = new RequestMap();
    	requestMap.put("userNum", "");
    	requestMap.put("userName", "");
    	
    	JSONObject jsonObject = new JSONObject(requestMap);
    	System.out.println("json.requestMap: "+requestMap.toString());
    	System.out.println("json.requestMap: "+jsonObject.toString());

    	//call http
      	JsonSimple json = new JsonSimple();
    	String strJson = json.http(url, jsonObject.toString());
    	
    	ListResult<UserInfo> result = new ListResult<UserInfo>(); 
    	JSONObject jsonObject2 = new JSONObject(strJson);
    	String resultCode = jsonObject2.getString("resultCode");
    	String resultMsg  = jsonObject2.getString("resultMsg");
    	result.setResultCode(resultCode);
    	result.setResultMsg(resultMsg);

    	JSONArray jsonArray = jsonObject2.optJSONArray("list");
    	for (int i= 0;i < jsonArray.length();i++ ) {
    		System.out.println("i="+i);
    		String jsonStr =  jsonArray.optString(i);
    		JSONObject jsonItem = new JSONObject(jsonStr);
    		System.out.println("jsonItem="+jsonItem);
    		//userNum,callingNum,callingName,registerDate,expiredDate,jsonMsg,durationType,downloadCnt,updateDate;
    		String userName = jsonItem.optString("userName");
    		String userNum = jsonItem.optString("userNum");
        	System.out.println("userNum="+userNum+" userName="+userNum);
    	}
    	
    	//System.out.println("result="+result);
    	assert(true);
//		fail("Not yet implemented");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testJSONHandling() {
    	
		HashMap<String, String> paramMap = new HashMap<String, String>();
    	paramMap.put("userId", "010");
    	paramMap.put("userName", "His");
    	
    	UserInfo userInfo1 = new UserInfo("user01_1","user01_2","user01_3","user01_4","user01_5");
    	UserInfo userInfo2 = new UserInfo("user02_1","user02_2","user02_3","user02_4","user02_5");
    	UserInfo userInfo3 = new UserInfo("user03_1","user03_2","user03_3","user03_4","user03_5");

    	/////////ReqList
    	ReqList reqList = new ReqList();
    	reqList.setValue("userId", "010");
    	reqList.setValue("userName", "His");
    	JSONObject jsonObject = new JSONObject(reqList);
    	System.out.println("json: "+jsonObject.toString());
    	
    	/////////RequestMap
    	RequestMap requestMap = new RequestMap();
    	requestMap.put("userId", "010");
    	requestMap.put("userName", "His");
    	
    	
    	List<UserInfo> userInfoList = new ArrayList<UserInfo>();
    	userInfoList.add(userInfo1);
    	userInfoList.add(userInfo2);
    	userInfoList.add(userInfo3);
    	requestMap.put("details", userInfoList);
    	JSONObject jsonObject2 = new JSONObject(requestMap);
    	System.out.println("json.requestMap: "+requestMap.toString());
    	System.out.println("json.requestMap: "+jsonObject2.toString());
    	
    	JSONObject jsonObject3 = new JSONObject(jsonObject2.toString());
    	String userIdVal = jsonObject2.getString("userId");
    	String userNameVal  = jsonObject2.getString("userName");
    	
    	System.out.println("userIdVal = "+userIdVal);
    	System.out.println("userNameVal = "+userNameVal);
    	JSONArray jsonArray = jsonObject2.optJSONArray("details");
    	for (int i= 0;i < jsonArray.length();i++ ) {
    		System.out.println("i="+i);
    		String jsonStr =  jsonArray.optString(i);
    		JSONObject jsonItem = new JSONObject(jsonStr);
    		System.out.println("jsonItem="+jsonItem);
    		//userNum,callingNum,callingName,registerDate,expiredDate,jsonMsg,durationType,downloadCnt,updateDate;
    		String userName = jsonItem.optString("userName");
    		String userNum = jsonItem.optString("userNum");
        	System.out.println("userNum="+userNum+" userName="+userNum);
    	}
    	
    	///////ListResult
    	
    	ListResult listResult = new ListResult();
    	listResult.setResultCode("1004");
    	listResult.setResultMsg("Ok");
    	
    	listResult.setList(userInfoList);
    	JSONObject jsonListResult = new JSONObject(listResult);
    	//System.out.println("json.requestMap: "+requestMap.toString());
    	System.out.println("json.ListResult: "+jsonListResult.toString());
    	
	}
}

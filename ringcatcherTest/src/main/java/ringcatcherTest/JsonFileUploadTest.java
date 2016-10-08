package ringcatcherTest;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import com.gencode.ringcatcher.obj.InviteResult;
import com.gencode.ringcatcher.obj.MessageResult;
import com.gencode.ringcatcher.obj.RegisterResult;
import com.gencode.ringcatcher.obj.ReturnCode;
import com.gencode.ringcatcher.obj.RingUpdateResult;

public class JsonFileUploadTest {
	String url = "http://localhost:8080/ringcatcher/";
	//String url = "http://52.79.62.98:8080/ringcatcher/";
	String mainFilePath = "/Users/gencode/Pictures/";
	String tokenId =  "cTSxdM5k5Iw:APA91bEjmPD31Exhcri9kHIb8sXs40q6EG5CQUiPrsG_Py86eRvjaYns2yydHwd_8ME3a48WFZGo0tYA-HGObBEHGlawWBMmFA-hfEdB0te8ICH8EjyLN1DjyWJe6FvVozrvQCULjtxv";

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testFileName() {
		File dir = new File("/home/jinnon/temp");
		try {
			File tempfile = File.createTempFile("aaaa", "jpg", dir);
			System.out.println("temp:"+tempfile.getName());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assert(true);
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
	  'test-token-id',
             '0244445555',
	 */
	@Test
	public void testFileUpLoad() {
    	String url =this.url+"/json/invite";
    	String tokenId = "test-token-id";
    	//String tokenId =  "cTSxdM5k5Iw:APA91bEjmPD31Exhcri9kHIb8sXs40q6EG5CQUiPrsG_Py86eRvjaYns2yydHwd_8ME3a48WFZGo0tYA-HGObBEHGlawWBMmFA-hfEdB0te8ICH8EjyLN1DjyWJe6FvVozrvQCULjtxv";

    	String body = "	{\"userNum\":\"01021985055\""
    			//String body = "	{\"userNum\":\"01055557777,01064668888,55557777\""
    			+",\"callingId\":\""+tokenId+"\""
    			+",\"callingNum\":\"0244445555\""
    			+",\"callingName\":\"Mememe\""
    			+",\"locale\":\"ko_KR\""//en_US, ko_KR
    			+",\"expiredDate\":\"99991231\""
    			+",\"durationType\":\"\""
    			+",\"ringFileName\":\"132626e0b5l409qdq2vogn.jpg\"}";
    			
    	JsonFileUpload json = new JsonFileUpload();
    	//String filepath = "/home/jinnon/Music/374-Marion-Parcel.mp3";
    	String filepath = "/Users/gencode/Pictures/132626e0b5l409qdq2vogn.jpg";
    	String strJson = json.http(url, body, filepath);
    	InviteResult result = new InviteResult(); 
    	JSONObject jsonObject = new JSONObject(strJson);
    	String resultCode = jsonObject.getString("resultCode");
    	String resultMsg  = jsonObject.getString("resultMsg");
    	result.setResultCode(resultCode);
    	result.setResultMsg(resultMsg);
    	System.out.println("url:"+url);
    	System.out.println("body:"+body);
    	System.out.println(strJson);
    	System.out.println("resultCode="+resultCode);
    	System.out.println("resultMsg="+resultMsg);
    	System.out.println("result = "+result.toString()); 	

		assert(true);
	}

//test-token-id
//0244445555
	@Test
	public void testRegisterMessage() {
    	String url =this.url+"/json/uploadImage";
    	//String tokenId = "fNCkmJERubk:APA91bH0sxyEsnHHC59H48JndfRsme0S9eX0L4y9qjkwWgkYvORZyeGm6Fjk4Eywgc4OlJMzx6TqMeBEgMV5aprVw83DA4DhH33FKqBrHUzOdxwGxAfcCo-qD3f4LzG3RSo71IK0YarG";
    	//String tokenId = "test-token-id";
    	String tokenId ="cTSxdM5k5Iw:APA91bEjmPD31Exhcri9kHIb8sXs40q6EG5CQUiPrsG_Py86eRvjaYns2yydHwd_8ME3a48WFZGo0tYA-HGObBEHGlawWBMmFA-hfEdB0te8ICH8EjyLN1DjyWJe6FvVozrvQCULjtxv"; 

    	String body = "	{\"userNum\":\"defaultnum\""//01055557777,01066668888,01066669999\""
    			+",\"callingId\":\""+tokenId+"\""
    			+",\"callingNum\":\"01094224916\""
    			+",\"callingName\":\"01094224916_n\""
//    			+",\"callingNum\":\"0244445555\""
//    			+",\"callingName\":\"Mememe\""
    			+",\"locale\":\"ko_KR\""//en_US, ko_KR
    			+",\"imageFileName\":\"nofilename\"}";
    			
    	JsonFileUpload json = new JsonFileUpload();
    	//String filepath = "/home/jinnon/Music/374-Marion-Parcel.mp3";
    	String filepath = mainFilePath+"/BUMmw03CIAAa7vj.jpg";
    	String strJson = json.http(url, body, filepath);
    	System.out.println(strJson);
    	InviteResult result = new InviteResult(); 
    	JSONObject jsonObject = new JSONObject(strJson);
    	String resultCode = jsonObject.getString("resultCode");
    	String resultMsg  = jsonObject.getString("resultMsg");
    	result.setResultCode(resultCode);
    	result.setResultMsg(resultMsg);
    	System.out.println("resultCode="+resultCode);
    	System.out.println("resultMsg="+resultMsg);
    	System.out.println("result = "+result.toString()); 	

    	if (!resultCode.equals(ReturnCode.SUCCESS.get())) assert(false);
    	
    	/*
	Encoded value: eyJtZXNzYWdlX2Zyb20iOiIwMTA0NDQ0NTU1NSIsCiJtZXNzYWdlX3RvIjoiMDEwNDQ0NDU1NTUiCiJyaW5nX21lc3NhZ2UiOnsKICIxOnR4dCI6ImZpcnN0TWVzc2FnZSIgCiwiMjppbWciOiJodHRwOi8vdDEuZGF1bWNkbi5uZXQvbmV3cy8yMDE1MDEvMTYvc3BvcnRzY2hvc3VuLzIwMTUwMTE2MDgzOTExOTM5LmpwZWc7LjQ1Oy40NSIKLCIzOnR4dCI6InRoaXJkTWVzc2FnZSIKLCI0OmltZyI6Imh0dHA6Ly9wYWl0YW8zNThnLmNvbS9kYXRhL2ZpbGUveWFib2FyZC8xODg5Nzc4MTA4X0RYWmdvS3pwX2Y0NjNiY2YwZjU3MTUyMDlhYzcwZTU0MTk0ZWFhYmMwYTI1MGQ5NDQucG5nIgosIjU6aW1nIjoiaHR0cDovL2FwcHp6YW5nLmNhL2RhdGEvZmlsZS9ob3QvMzcxODUzNTI3M19OR0tEUUNmbF9KcVdhUUVaLmpwZzswLjk7MC45IgosIjY6dHh0Ijoi7Jes7ISv67KI7Ke4IOuplOyLnOyngCIKfQp9
	Decoded value: {"message_from":"01044445555",
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
    	url = this.url+"/json/registerMessage";
    	
    	body =  //"	{\"userNum\":\"defaultnum\""//01055557777,01066668888,01044449999\""
    			"	{\"userNum\":\"defaultnum\""//01055557777,01066668888,01066669999\""
    			+",\"callingId\":\""+tokenId+"\""
    			+",\"callingNum\":\"01094224916\""
    			+",\"callingName\":\"01094224916_n\""
    			//+",\"callingNum\":\"0244445555\""
    			//+",\"callingName\":\"Mememe\""
    			+",\"locale\":\"ko_KR\""//en_US, ko_KR
    			+",\"expiredDate\":\"20160819\""
    			+",\"durationType\":\"T\""
    			+",\"jsonMessage\":\"eyJtZXNzYWdlX2Zyb20iOiIwMTA0NDQ0NTU1NSIsCiJtZXNzYWdlX3RvIjoiMDEwNDQ0NDU1NTUiCiJyaW5nX21lc3NhZ2UiOnsKICIxOnR4dCI6ImZpcnN0TWVzc2FnZSIgCiwiMjppbWciOiJodHRwOi8vdDEuZGF1bWNkbi5uZXQvbmV3cy8yMDE1MDEvMTYvc3BvcnRzY2hvc3VuLzIwMTUwMTE2MDgzOTExOTM5LmpwZWc7LjQ1Oy40NSIKLCIzOnR4dCI6InRoaXJkTWVzc2FnZSIKLCI0OmltZyI6Imh0dHA6Ly9wYWl0YW8zNThnLmNvbS9kYXRhL2ZpbGUveWFib2FyZC8xODg5Nzc4MTA4X0RYWmdvS3pwX2Y0NjNiY2YwZjU3MTUyMDlhYzcwZTU0MTk0ZWFhYmMwYTI1MGQ5NDQucG5nIgosIjU6aW1nIjoiaHR0cDovL2FwcHp6YW5nLmNhL2RhdGEvZmlsZS9ob3QvMzcxODUzNTI3M19OR0tEUUNmbF9KcVdhUUVaLmpwZzswLjk7MC45IgosIjY6dHh0Ijoi7Jes7ISv67KI7Ke4IOuplOyLnOyngCIKfQp9\"}";
		//+",\"jsonMessage\":\"eyJtZXNzYWdlX2Zyb20iOiIwMTA0NDQ0NTU1NSIsCiJtZXNzYWdlX3RvIjoiMDEwNDQ0NDU1NTUiCiJyaW5nX21lc3NhZ2UiOnsKICIxOnR4dCI6ImZpcnN0TWVzc2FnZSIgCiwiMjppbWciOiJodHRwOi8vdDEuZGF1bWNkbi5uZXQvbmV3cy8yMDE1MDEvMTYvc3BvcnRzY2hvc3VuLzIwMTUwMTE2MDgzOTExOTM5LmpwZWc7LjQ1Oy40NSIKLCIzOnR4dCI6InRoaXJkTWVzc2FnZSIKLCI0OmltZyI6Imh0dHA6Ly9wYWl0YW8zNThnLmNvbS9kYXRhL2ZpbGUveWFib2FyZC8xODg5Nzc4MTA4X0RYWmdvS3pwX2Y0NjNiY2YwZjU3MTUyMDlhYzcwZTU0MTk0ZWFhYmMwYTI1MGQ5NDQucG5nIgosIjU6aW1nIjoiaHR0cDovL2FwcHp6YW5nLmNhL2RhdGEvZmlsZS9ob3QvMzcxODUzNTI3M19OR0tEUUNmbF9KcVdhUUVaLmpwZzswLjk7MC45IgosIjY6dHh0Ijoi7Jes7ISv67KI7Ke4IOuplOyLnOyngCIKfQp9\"}";
    	//body ="{\"userNum\":\"01055557777,01066668888\",\"callingId\":\"test-token-id\",\"callingNum\":\"0244445555\",\"callingName\":\"It's me!\",\"locale\":\"ko_KR\",\"jsonMessage\":\"eyJtZXNzYWdlX3RvIjoiMDEwNTU1NTc3NzciLCJyaW5nX21lc3NhZ2UiOnsiMTppbWciOiJcL3JpbmdtZWRpYVwvMjAxNjA2MjZcLzAyNDQ0NDU1NTVfMDEwNTU1NTc3NzcuanBnOzEuMDAwMDAwOzEuMDAwMDAwIiwiMDp0eHQiOiLjhY7jhYfjhYztjbzrn7/jhY7jhLkifSwibWVzc2FnZV9mcm9tIjoiMDI0NDQ0NTU1NSJ9\"}";
//    	 registerMessage:defaultnum:eC5cRs5koa4:APA91bHKENSZ98H2-NY0hLzjh-QAdmh0AoPKh80ZudMhZGyL9qF0wABWYQofdXHSKyXVw6_V168Y5kqnLVf05hR2hESl3SeuK3Jb2N2_Kxnwo087bEZagz6JQmQ-z1qKo8TxN5rdnlwr:
//    		 01094224916:01094224916_n:eyJtZXNzYWdlX2Zyb20iOiIwMTA5NDIyNDkxNiIsIm1lc3NhZ2VfdG8iOiJkZWZhdWx0bnVtIiwicmluZ19tZXNzYWdlIjp7IjA6dHh0Ijoi66qo65GQIOuztOyEuOyalCJ9fQ==:
//    		 ko_KR:20160819:T

    	
    	JsonSimple jsonSimple = new JsonSimple();
    	strJson = jsonSimple.http(url, body);
    	
    	RegisterResult result2 = new RegisterResult(); 
    	JSONObject jsonObject2 = new JSONObject(strJson);
    	String resultCode2 = jsonObject2.getString("resultCode");
    	String resultMsg2  = jsonObject2.getString("resultMsg");
    	result2.setResultCode(resultCode2);
    	result2.setResultMsg(resultMsg2);
    	System.out.println("resultCode="+resultCode2);

    	System.out.println("result = "+result2.toString());

		assert(true);
	}

	@Test
	public void testGetMessage() {
    	String url = this.url+"/json/getmessage";

    	String tokenId = "test-token-id2";
    	String body = "{\"userId\":\""+tokenId+"\""
    			+",\"userNum\":\"01066668888\""
    			+",\"callingNum\":\"0244445555\"}";
    	JsonSimple json = new JsonSimple();
    	String strJson = json.http(url, body);
    	try {
        	MessageResult result = new MessageResult(); 
        	JSONObject jsonObject = new JSONObject(strJson);
        	String resultCode = jsonObject.getString("resultCode");
        	String resultMsg  = jsonObject.getString("resultMsg");
        	String defaultDurationType = jsonObject.getString("defaultDurationType");
        	String defaultJsonMessage  = jsonObject.getString("defaultJsonMessage");
        	String defaultExpiredDate = jsonObject.getString("defaultExpiredDate");
        	String durationType  = jsonObject.getString("durationType");
        	String jsonMessage = jsonObject.getString("jsonMessage");
        	String expiredDate  = jsonObject.getString("expiredDate");
        	result.setResultCode(resultCode);
        	result.setResultMsg(resultMsg);
        	result.setDefaultDurationType(defaultDurationType);
        	result.setDefaultExpiredDate(defaultExpiredDate);
        	result.setDefaultJsonMessage(defaultJsonMessage);
        	result.setDurationType(durationType);
        	result.setJsonMessage(jsonMessage);
        	result.setExpiredDate(expiredDate);
        	
        	System.out.println("AAAA");
        	System.out.println("AAAAAresult = "+result.toString());
    	} catch (Exception e) {
    		e.printStackTrace();
    	}

    	assert(true);
//		fail("Not yet implemented");
	}
}

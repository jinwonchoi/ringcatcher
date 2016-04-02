package ringcatcherTest;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

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
	,"callingId":"2134129p3481"
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
    	
    	String body = "	{\"userNum\":\"010444448888\""
    			+",\"callingId\":\"2134129p3481\""
    			+",\"callingNum\":\"01066663333\""
    			+",\"ringFileName\":\"0004.jpg\"}";
    			
    	JsonFileUpload json = new JsonFileUpload();
    	//String filepath = "/home/jinnon/Music/374-Marion-Parcel.mp3";
    	String filepath = "/home/jinnon/Pictures/Wallpapers/0004.jpg";
    	json.http(url, body, filepath);
		
		fail("Not yet implemented");
	}

}

package ringcatcherTest;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JsonFileUpload {

    public static void main(String[] args) {
    	String url = "http://localhost:8080/ringcatcher/json/ringfileup";
    	
    	String body = "{\"userId\":\"asdfasdfasdf\",\"userNum\":\"weweweewe\",\"userEmail\":\"jinnonspot@gmail.com\",\"recomId\":\"weweweewe\"}";
    	JsonFileUpload json = new JsonFileUpload();
    	//String filepath = "/home/jinnon/Music/374-Marion-Parcel.mp3";
    	String filepath = "/home/jinnon/Pictures/ugirls/13.jpg";
    	json.http(url, body, filepath);

    }

    public void http(String url, String body, String filepath) {

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpPost request = new HttpPost(url);
            File file = new File(filepath);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();         
            builder.setMode(HttpMultipartMode.STRICT);
            builder.addBinaryBody
              ("upfile", file, ContentType.DEFAULT_BINARY, filepath);
            builder.addTextBody("json", body, ContentType.APPLICATION_JSON);
            //builder.addPart("text", new StringBody(body, ContentType.TEXT_PLAIN));
            // 
            HttpEntity entity = builder.build();
            request.setEntity(entity);
            HttpResponse result = httpClient.execute(request);

            String json = EntityUtils.toString(result.getEntity(), "UTF-8");
            try {
                JSONParser parser = new JSONParser();
                Object resultObject = parser.parse(json);

                if (resultObject instanceof JSONArray) {
                    JSONArray array=(JSONArray)resultObject;
                    for (Object object : array) {
                        JSONObject obj =(JSONObject)object;
                        
                        System.out.println(obj);
                    }

                }else if (resultObject instanceof JSONObject) {
                    JSONObject obj =(JSONObject)resultObject;
                    System.out.println(obj);
                }

            } catch (Exception e) {
            	e.printStackTrace();
            }

        } catch (IOException ex) {
        	ex.printStackTrace();
        }
    }
    
}
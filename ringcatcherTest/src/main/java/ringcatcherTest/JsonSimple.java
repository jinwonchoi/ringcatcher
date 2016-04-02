package ringcatcherTest;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JsonSimple {

    public static void main(String[] args) {
    	String url = "http://localhost:8080/ringcatcher/json/register";
    	
    	String body = "{\"userId\":\"asdfasdfasdf\",\"userNum\":\"weweweewe\",\"userEmail\":\"jinnonspot@gmail.com\",\"recomId\":\"weweweewe\"}";
    	JsonSimple json = new JsonSimple();
    	json.http(url, body);

    }

    public void http(String url, String body) {

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpPost request = new HttpPost(url);
            StringEntity params = new StringEntity(body, ContentType.APPLICATION_JSON);
            //request.addHeader("content-type", "application/json");
            request.setEntity(params);
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
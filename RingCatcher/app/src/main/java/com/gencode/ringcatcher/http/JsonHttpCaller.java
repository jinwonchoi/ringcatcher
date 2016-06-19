package com.gencode.ringcatcher.http;

import android.net.Uri;
import android.util.Log;

import com.gencode.ringcatcher.obj.InviteResult;
import com.gencode.ringcatcher.obj.InviteUploadRequest;
import com.gencode.ringcatcher.obj.RegisterRequest;
import com.gencode.ringcatcher.obj.RegisterResult;
import com.gencode.ringcatcher.obj.RingUpdateRequest;
import com.gencode.ringcatcher.obj.RingUpdateResult;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;;

public class JsonHttpCaller {
    final static public String TAG = JsonHttpCaller.class.getSimpleName();
//
//    public static void main(String[] args) {
//    	String url = "http://localhost:8080/ringcatcher/json/register";
//
//    	String body = "{\"userId\":\"asdfasdfasdf\",\"userNum\":\"weweweewe\",\"userEmail\":\"jinnonspot@gmail.com\",\"recomId\":\"weweweewe\"}";
//    	JsonHttpCaller json = new JsonHttpCaller();
//    	json.http(url, body);
//
//    }
    /**
     * 	 * 1 앱으로 가입
     * 등록정보 폰번호
     */
    public RegisterResult registerUser(RegisterRequest request) {
        RegisterResult result = new RegisterResult();

        try {
            String url = HttpConstants.RING_CATCHER_HOME+"/json/register";
            String body = "{\"userNum\":\""+request.getUserPhoneNum()+"\""
                    +",\"userId\":\""+request.getTokenId()+"\""
                    +",\"userName\":\""+request.getUserNick()+"\""
                    +",\"userEmail\":\""+request.getUserEmail()+"\""
                    +",\"recomId\":\""+request.getRecommendPhoneNum()+"\""
                    +",\"overwrite\":\""+request.isOverwrite()+"\"}";

            String json = http(url, body);
            JSONObject jsonRootObject = new JSONObject(json);

            //Get the instance of JSONArray that contains JSONObjects
            result.setResultCode(jsonRootObject.optString("resultCode"));
            result.setResultMsg(jsonRootObject.optString("resultMsg"));

            Log.d(TAG, "result:"+result);
        } catch (JSONException e) {
            Log.e(TAG, "register error request:"+request, e);
            result = null;
        }
        return result;
    }

    /**
     * 2 음원등록
     */
    public InviteResult inviteUploadMedia(InviteUploadRequest request) {
        InviteResult result = new InviteResult();

        try {
            Uri fileUri = Uri.parse(request.getFilePath());
            String fileName = new File(fileUri.getPath()).getName();
            String url = HttpConstants.RING_CATCHER_HOME+"/json/invite";
            String body = "	{\"userNum\":\""+request.getFriendPhoneNum()+"\""
                    +",\"callingId\":\""+request.getTokenId()+"\""
                    +",\"callingNum\":\""+request.getCallingPhoneNum()+"\""
                    +",\"callingName\":\""+request.getCallingNickName()+"\""
                    +",\"locale\":\""+request.getLocale()+"\""//en_US, ko_KR
                    +",\"ringFileName\":\""+fileName+"\"}";

            String json = multipart(url, body, request.getFilePath());
            JSONObject jsonRootObject = new JSONObject(json);

            //Get the instance of JSONArray that contains JSONObjects
            result.setResultCode(jsonRootObject.optString("resultCode"));
            result.setResultMsg(jsonRootObject.optString("resultMsg"));

            Log.d(TAG, "result:"+result);
        } catch (JSONException e) {
            Log.e(TAG, "inviteUploadMedia error request:"+request, e);
            result = null;
        }
        return result;

    }

    /**
     * 	3. 대기중인 링정보 체크 및 다운로드
     */
    public RingUpdateResult updateRing(RingUpdateRequest request) {
        RingUpdateResult result = null;
        String url = HttpConstants.RING_CATCHER_HOME+"/json/ringupdate";

        try {
            result = _queryRing(url, request);
        } catch (JSONException e) {
            Log.e(TAG, "updateRing error request:"+request, e);
        }
        return result;
    }

    /**
     * 	4. 기존정보 재다운로드
     */
    public RingUpdateResult checkoutRing(RingUpdateRequest request) {
        RingUpdateResult result = null;
        String url = HttpConstants.RING_CATCHER_HOME+"/json/ringcheckout";

        try {
            result = _queryRing(url, request);
        } catch (JSONException e) {
            Log.e(TAG, "updateRing error request:"+request, e);
        }
        return result;
    }


    public RingUpdateResult _queryRing(String url, RingUpdateRequest request) throws JSONException{
        RingUpdateResult result = null;

        String body = "{\"userId\":\""+request.getUserid()+"\""
                +",\"userNum\":\""+request.getUserNum()+"\"}";
        try {
            result = new RingUpdateResult();
            String strJson = http(url, body);
            Log.d(TAG, "json="+strJson);
            JSONObject jsonObject = new JSONObject(strJson);
            result.setResultCode(jsonObject.getString("resultCode"));
            result.setResultMsg(jsonObject.getString("resultMsg"));
            JSONArray jsonArray = jsonObject.optJSONArray("updateList");
            for (int i= 0;i < jsonArray.length();i++ ) {
                System.out.println("i="+i);
                String jsonStr =  jsonArray.getString(i);
                JSONObject jsonItem = new JSONObject(jsonStr);
                Log.d(TAG, "jsonItem="+jsonItem);

                String callingNum = jsonItem.optString("callingNum");
                Log.d(TAG, "callingNum="+callingNum);
                String filePath = jsonItem.getString("filePath");
                Log.d(TAG, "filePath="+filePath);
                result.setUpdateItem(callingNum, filePath);
            }
            Log.d(TAG, "result = "+result);
        } catch (JSONException e) {
            Log.e(TAG, "register error request:"+request, e);
        }
        return result;
    }

    public String multipart(String url, String body, String filepath) {
        String result = "";
        try  {
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();

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
            HttpResponse response = httpClient.execute(request);

            result = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return result;
    }


    public String http(String url, String body) {
        String json = "";
        try  {
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost request = new HttpPost(url);
            StringEntity params = new StringEntity(body);
            params.setContentType(ContentType.APPLICATION_JSON.toString());
            //request.addHeader("content-type", "application/json");
            request.setEntity(params);

            HttpResponse result = httpClient.execute(request);

            json = EntityUtils.toString(result.getEntity(), "UTF-8");
        } catch (IOException ex) {
            Log.e(TAG, "http error body:"+body,ex);
        }
        return json;
    }

//    public String http(String url, String body) {
//        HttpClient httpclient = new DefaultHttpClient();
//        HttpPost httppost = new HttpPost("<YOUR_SERVICE_URL>");
//
//        try {
//
//            JSONObject jsonobj = new JSONObject();
//
//            jsonobj.put("name", "Aneh");
//            jsonobj.put("age", "22");
//
//            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//            nameValuePairs.add(new BasicNameValuePair("req", jsonobj.toString()));
//
//            Log.e("mainToPost", "mainToPost" + nameValuePairs.toString());
//
//            // Use UrlEncodedFormEntity to send in proper format which we need
//            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//
//            // Execute HTTP Post Request
//            HttpResponse response = httpclient.execute(httppost);
//            InputStream inputStream = response.getEntity().getContent();
//            InputStreamToStringExample str = new InputStreamToStringExample();
//            responseServer = str.getStringFromInputStream(inputStream);
//            Log.e("response", "response -----" + responseServer);
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
}
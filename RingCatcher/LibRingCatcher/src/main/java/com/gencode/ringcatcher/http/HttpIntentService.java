package com.gencode.ringcatcher.http;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.gencode.ringcatcher.obj.JsonConstants;
import com.gencode.ringcatcher.obj.RegisterMessageRequest;
import com.gencode.ringcatcher.obj.RegisterResult;
import com.gencode.ringcatcher.obj.UploadImageRequest;
import com.gencode.ringcatcher.obj.UploadImageResult;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class HttpIntentService extends IntentService {
    final String TAG = this.getClass().getName();

    public HttpIntentService() {
        super("HttpIntentService");
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionMessageRegister(Context context, String userNum, String callingId, String callingNum
            , String callingName, HashMap<String, String> messageMap) {
        Intent intent = new Intent(context, HttpIntentService.class);
        intent.setAction(HttpConstants.ACTION_MESSAGE_REGISTER);
        intent.putExtra(JsonConstants.userNum, userNum);
        intent.putExtra(JsonConstants.callingId, callingId);
        intent.putExtra(JsonConstants.callingNum, callingNum);
        intent.putExtra(JsonConstants.callingName, callingName);
        intent.putExtra(JsonConstants.messageMap, messageMap);

        context.startService(intent);
    }
    /**
     *
     * @param intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (HttpConstants.ACTION_IMAGE_UPLOAD.equals(action)) {
                //handleActionUploadImage(intent);
            } else if (HttpConstants.ACTION_MESSAGE_REGISTER.equals(action)) {
                handleActionRegisterMessage(intent);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionRegisterMessage(Intent intent) {

        String tokenId   = intent.getStringExtra(JsonConstants.callingId);
        String userNum   = intent.getStringExtra(JsonConstants.userNum);
        String callingNum = intent.getStringExtra(JsonConstants.callingNum);
        String callingName = intent.getStringExtra(JsonConstants.callingName);
        HashMap<String, String> messageMap = (HashMap<String, String>)intent.getSerializableExtra(JsonConstants.messageMap);
        MessageWrapper messageWrapper;
        Intent intentResult = new Intent(HttpConstants.ACTION_BROADCAST_MESSAGE_REGISTER);

        JsonHttpCaller caller = new JsonHttpCaller();

        try {

            //key   format : "%d:img" or "%d:txt"
            //value format : "string" or "image_url;xscale;yscale"
            for (Map.Entry<String, String> entry : messageMap.entrySet()) {
                //jsonObject.put(entry.getKey(),entry.getValue());
                if (!entry.getKey().endsWith("img")) continue;

                String[] imageValue = entry.getValue().split(";");
                String imageUrl;
                String scaleX = "";
                String scaleY = "";
                String resultImageUrl;
                if (imageValue.length > 1) {
                    imageUrl = imageValue[0];
                    scaleX = imageValue[1];
                    scaleY = imageValue[2];
                } else {
                    imageUrl = imageValue[0];
                }
                UploadImageRequest uploadImageRequest = new UploadImageRequest();
                uploadImageRequest.setTokenId(tokenId);
                uploadImageRequest.setFriendPhoneNum(userNum);
                uploadImageRequest.setCallingPhoneNum(callingNum);
                uploadImageRequest.setCallingNickName(callingName);
                uploadImageRequest.setLocale(Locale.KOREA.toString()); //ko_KR | en_US
                uploadImageRequest.setImageFileName(imageUrl);
                UploadImageResult uploadImageResult = new UploadImageResult();

                uploadImageResult = caller.uploadMessageImage(uploadImageRequest);
                if (ReturnCode.SUCCESS.equals(uploadImageResult.getResultCode())
                    ||ReturnCode.UPDATE_OK.equals(uploadImageResult.getResultCode())) {
                    if (scaleX.equals("")) {
                        resultImageUrl= uploadImageResult.getFileUrl();
                    } else {
                        resultImageUrl = String.format("%s;%s;%s", uploadImageResult.getFileUrl(), scaleX, scaleY);
                    }
                    entry.setValue(resultImageUrl);
                } else {
                    throw new Exception("Image file upload failed:"+imageUrl);
                }
            }

            messageWrapper = new MessageWrapper(callingNum, userNum, messageMap);
            RegisterMessageRequest registerMessageRequest = new RegisterMessageRequest();
            registerMessageRequest.setTokenId(tokenId);
            registerMessageRequest.setFriendPhoneNum(userNum);
            registerMessageRequest.setCallingPhoneNum(callingNum);
            registerMessageRequest.setCallingNickName(callingName);
            registerMessageRequest.setLocale(Locale.KOREA.toString()); //ko_KR | en_US
            registerMessageRequest.setJsonMessage(messageWrapper.toEncodedString());
            RegisterResult registerResult = null;

            registerResult = caller.registerMessage(registerMessageRequest);
            intentResult.putExtra(JsonConstants.resultCode, registerResult.getResultCode());
            intentResult.putExtra(JsonConstants.resultMsg, registerResult.getResultMsg());

        } catch (Exception e) {
            Log.e(TAG, "Calling Asynch uploadImage", e);
            intentResult.putExtra(JsonConstants.resultCode, ReturnCode.ERROR_FILEUP.get());
            intentResult.putExtra(JsonConstants.resultMsg, ReturnCode.STR_ERROR_FILEUP.get());
        }
        // Broadcasts the Intent to receivers in this app.
        LocalBroadcastManager.getInstance(this).sendBroadcast(intentResult);
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionUploadImage(Intent intent) {
        String tokenId   = intent.getStringExtra(JsonConstants.userId);
        String userNum   = intent.getStringExtra(JsonConstants.userNum);
        String callingNum = intent.getStringExtra(JsonConstants.callingNum);
        String callingName = intent.getStringExtra(JsonConstants.callingName);
        String filePath = intent.getStringExtra(JsonConstants.filePath);

        UploadImageRequest uploadImageRequest= new UploadImageRequest();
        uploadImageRequest.setTokenId(tokenId);
        uploadImageRequest.setFriendPhoneNum(userNum);
        uploadImageRequest.setCallingPhoneNum(callingNum);
        uploadImageRequest.setCallingNickName(callingName);
        uploadImageRequest.setLocale(Locale.KOREA.toString()); //ko_KR | en_US
        uploadImageRequest.setImageFileName(filePath);
        UploadImageResult uploadImageResult= null;
        try {
            JsonHttpCaller caller = new JsonHttpCaller();
            uploadImageResult = caller.uploadMessageImage(uploadImageRequest);
        } catch (Exception e) {
            Log.e(TAG, "Calling Asynch uploadImage",e);
        }

        Intent intentResult =
                new Intent(HttpConstants.ACTION_BROADCAST_IMAGE_UPLOAD);
        intentResult.putExtra(JsonConstants.resultCode, uploadImageResult.getResultCode());
        intentResult.putExtra(JsonConstants.resultMsg, uploadImageResult.getResultMsg());
        intentResult.putExtra(JsonConstants.fileUrl, uploadImageResult.getFileUrl());

        // Broadcasts the Intent to receivers in this app.
        LocalBroadcastManager.getInstance(this).sendBroadcast(intentResult);
        //throw new UnsupportedOperationException("Not yet implemented");
    }
}

package com.gencode.ringcatcher.task;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.gencode.ringcatcher.R;
import com.gencode.ringcatcher.common.RingBearer;
import com.gencode.ringcatcher.gcm.GcmActivity;
import com.gencode.ringcatcher.gcm.QuickstartPreferences;
import com.gencode.ringcatcher.gcm.RegistrationIntentService;
import com.gencode.ringcatcher.http.JsonHttpCaller;
import com.gencode.ringcatcher.http.ReturnCode;
import com.gencode.ringcatcher.obj.RegisterRequest;
import com.gencode.ringcatcher.obj.RegisterResult;

/**
 * Created by Administrator on 2016-04-20.
 */
public class AsyncRegisterUser extends AsyncTask<Void, Void, RegisterResult> {
    final static String TAG = AsyncRegisterUser.class.getSimpleName();

    IRegisterTask delegate = null;
    public AsyncRegisterUser(IRegisterTask task) {
        delegate = task;
    }

    /**
     * http로 서버에 사용자 등록
     */
    @Override
    protected RegisterResult doInBackground(Void... args) {
        String matePhoneNumer = RingBearer.getInstance().getFriendPhoneNumber();
        String matePhoneNick = RingBearer.getInstance().getFriendNick();
        String mediaUrl = RingBearer.getInstance().getMediaUrl();

        String tokenId = RingBearer.getInstance().getTokenId();
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setTokenId(tokenId);
        registerRequest.setUserPhoneNum(RingBearer.getInstance().getMyPhoneNumber());
        registerRequest.setUserNick(RingBearer.getInstance().getMyPhoneNick());
        registerRequest.setOverwrite(true);
        RegisterResult registerResult = null;
        if (tokenId == null || tokenId.equals("")) {
            Log.e(TAG, "Calling Asynch RegisterUser tokenId=null");
            return null;
        }

        try {
            JsonHttpCaller caller = new JsonHttpCaller();
            registerResult = caller.registerUser(registerRequest);
        } catch (Exception e) {
            Log.e(TAG, "Calling Asynch RegisterUser",e);
        }
        return registerResult;
    }

    @Override
    protected void onPostExecute(RegisterResult strResult) {
        super.onPostExecute(strResult);
        delegate.OnTaskCompleted(strResult);
    }
}
package com.gencode.ringcatcher.task;

import android.os.AsyncTask;
import android.util.Log;

import com.gencode.ringcatcher.common.QuickstartPreferences;
import com.gencode.ringcatcher.common.RingBearer;
import com.gencode.ringcatcher.http.JsonHttpCaller;
import com.gencode.ringcatcher.obj.JsonConstants;
import com.gencode.ringcatcher.obj.RingUpdateRequest;
import com.gencode.ringcatcher.obj.RingUpdateResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2016-04-20.
 */
public class AsyncUpdateRing extends AsyncTask<String, Void, RingUpdateResult> {
    final static String TAG = AsyncUpdateRing.class.getSimpleName();
    IUpdateRingTask delegate = null;
    public AsyncUpdateRing(IUpdateRingTask task) {
        delegate = task;
    }
    /**
     * http로 서버에 친구에게 음원등록
     */
    @Override
    protected RingUpdateResult doInBackground(String... args) {
        RingUpdateRequest ringUpdateRequest = new RingUpdateRequest();
        ringUpdateRequest.setUserid(RingBearer.getInstance().getTokenId());
        ringUpdateRequest.setUserNum(RingBearer.getInstance().getMyPhoneNumber());
        RingUpdateResult inviteResult= null;

        try {
            JsonHttpCaller caller = new JsonHttpCaller();
            if (QuickstartPreferences.MENU_UPDATE_RING_INFO.equals(args[0])) {
                inviteResult = caller.updateRing(ringUpdateRequest);
            } else {
                inviteResult = caller.checkoutRing(ringUpdateRequest);
            }
        } catch (Exception e) {
            Log.e(TAG, "Calling Asynch InviteAndUpload",e);
        }
        return inviteResult;
    }

    @Override
    protected void onPostExecute(RingUpdateResult strResult) {
        super.onPostExecute(strResult);
        delegate.OnTaskCompleted(strResult);
    }
}
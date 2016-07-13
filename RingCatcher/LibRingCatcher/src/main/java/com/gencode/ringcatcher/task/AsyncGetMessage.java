package com.gencode.ringcatcher.task;

import android.os.AsyncTask;
import android.util.Log;

import com.gencode.ringcatcher.http.JsonHttpCaller;
import com.gencode.ringcatcher.obj.MessageRequest;
import com.gencode.ringcatcher.obj.MessageResult;

/**
 * Created by Administrator on 2016-04-20.
 */
public class AsyncGetMessage extends AsyncTask<String, Void, MessageResult> {
    final static String TAG = AsyncGetMessage.class.getSimpleName();
    IGetMessageTask delegate = null;
    public AsyncGetMessage(IGetMessageTask task) {
        delegate = task;
    }
    /**
     * http로 서버에 친구에게 음원등록
     */
    @Override
    protected MessageResult doInBackground(String... args) {
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setUserid(args[0]);
        messageRequest.setUserNum(args[1]);
        messageRequest.setCallingNum(args[2]);
        MessageResult result= null;

        try {
            JsonHttpCaller caller = new JsonHttpCaller();
            result = caller.getMessage(messageRequest);
        } catch (Exception e) {
            Log.e(TAG, "Calling Asynch InviteAndUpload",e);
        }
        return result;
    }

    @Override
    protected void onPostExecute(MessageResult strResult) {
        super.onPostExecute(strResult);
        delegate.OnTaskCompleted(strResult);
    }
}
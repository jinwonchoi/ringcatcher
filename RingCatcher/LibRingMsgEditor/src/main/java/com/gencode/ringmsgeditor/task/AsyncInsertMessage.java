package com.gencode.ringmsgeditor.task;

import android.os.AsyncTask;
import android.util.Log;
import com.gencode.ringmsgeditor.ContentResolverHelper;

import java.util.Locale;
import java.util.Map;

/**
 * Created by Administrator on 2016-04-20.
 */
public class AsyncInsertMessage extends AsyncTask<Map<String,String>, Void, Boolean> {
    final static String TAG = AsyncInsertMessage.class.getSimpleName();
    String mMessageFrom;
    String mMessageTo;
    ContentResolverHelper mContentResolverHelper;
    IInsertMessageTask delegate;
    public AsyncInsertMessage(IInsertMessageTask task, ContentResolverHelper contentResolverHelper, String messageFrom, String messageTo) {
        delegate = task;
        mContentResolverHelper = contentResolverHelper;
        mMessageFrom = messageFrom;
        mMessageTo = messageTo;
    }

    /**
     * 친구에게 음원등록
     */
    @Override
    protected Boolean doInBackground(Map<String,String>... args) {
        try {
            mContentResolverHelper.checkQuery();
            if (!mContentResolverHelper.doInsert(mMessageFrom,mMessageTo, args[0]))
                throw new Exception(String.format("insert fail message from[%s] message to[%s]", mMessageFrom,mMessageTo));
            //ServerUtilities.send(txt, profileEmail);
        } catch (Exception ex) {
            Log.e(TAG, "Message could not be sent",ex);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        delegate.onTaskCompleted(result);
    }
}

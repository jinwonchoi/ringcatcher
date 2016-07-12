package com.gencode.ringcatcher.task;

import android.os.AsyncTask;
import android.util.Log;

import com.gencode.ringcatcher.common.RingBearer;
import com.gencode.ringcatcher.http.JsonHttpCaller;
import com.gencode.ringcatcher.obj.InviteResult;
import com.gencode.ringcatcher.obj.InviteUploadRequest;
import com.gencode.ringcatcher.obj.UploadImageRequest;
import com.gencode.ringcatcher.obj.UploadImageResult;

import java.util.Locale;

/**
 * Created by Administrator on 2016-04-20.
 */
public class AsyncUploadImage extends AsyncTask<UploadImageRequest, Void, UploadImageResult> {
    final static String TAG = AsyncUploadImage.class.getSimpleName();
    IUploadImageTask delegate = null;
    public AsyncUploadImage(IUploadImageTask task) {
        delegate = task;
    }

    /**
     * http로 서버에 친구에게 음원등록
     */
    @Override
    protected UploadImageResult doInBackground(UploadImageRequest... args) {
        UploadImageResult uploadImageResult= null;
        try {
            JsonHttpCaller caller = new JsonHttpCaller();
            uploadImageResult = caller.uploadMessageImage(args[0]);
        } catch (Exception e) {
            Log.e(TAG, "Calling Asynch InviteAndUpload",e);
        }
        return uploadImageResult;
    }

    @Override
    protected void onPostExecute(UploadImageResult strResult) {
        super.onPostExecute(strResult);
        delegate.OnTaskCompleted(strResult);
    }
}

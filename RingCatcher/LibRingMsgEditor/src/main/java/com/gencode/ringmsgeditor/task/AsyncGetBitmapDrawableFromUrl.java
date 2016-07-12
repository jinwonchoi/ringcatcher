package com.gencode.ringmsgeditor.task;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import com.gencode.ringmsgeditor.ContentResolverHelper;
import com.gencode.ringmsgeditor.Utils;

import java.util.Map;

/**
 * Created by Administrator on 2016-04-20.
 */
public class AsyncGetBitmapDrawableFromUrl extends AsyncTask<String, Void, Drawable> {
    final static String TAG = AsyncGetBitmapDrawableFromUrl.class.getSimpleName();
    Context mContext;
    IGetBitmapDrawableTask delegate;
    public AsyncGetBitmapDrawableFromUrl(IGetBitmapDrawableTask task, Context context) {
        delegate = task;
        mContext = context;
    }

    /**
     * 친구에게 음원등록
     */
    @Override
    protected Drawable doInBackground(String... args) {
        try {
            if (args[0].startsWith("http")) {
                return Utils.drawableFromUrl(mContext, args[0]);
            } else {
                return Utils.drawableFromUri(mContext, args[0]);
            }
        } catch (Exception ex) {
            Log.e(TAG, "get bitmap drawable failed from :"+args[0],ex);
            return null;
        }
    }

    @Override
    protected void onPostExecute(Drawable drawable) {
        super.onPostExecute(drawable);
        delegate.onTaskCompleted(drawable);
    }
}

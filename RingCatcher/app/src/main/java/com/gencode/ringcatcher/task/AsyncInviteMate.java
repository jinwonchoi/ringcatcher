package com.gencode.ringcatcher.task;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.gencode.ringcatcher.R;
import com.gencode.ringcatcher.common.RingBearer;
import com.gencode.ringcatcher.http.JsonHttpCaller;
import com.gencode.ringcatcher.obj.InviteResult;
import com.gencode.ringcatcher.obj.InviteUploadRequest;

import java.util.Locale;

/**
 * Created by Administrator on 2016-04-20.
 */
public class AsyncInviteMate extends AsyncTask<Void, Void, InviteResult> {
    final static String TAG = AsyncInviteMate.class.getSimpleName();
    IInvteMateTask delegate = null;
    public AsyncInviteMate(IInvteMateTask task) {
        delegate = task;
    }

    /**
     * http로 서버에 친구에게 음원등록
     */
    @Override
    protected InviteResult doInBackground(Void... args) {
        InviteUploadRequest inviteUploadRequest= new InviteUploadRequest();
        inviteUploadRequest.setTokenId("test-token-id");
        inviteUploadRequest.setFriendPhoneNum(RingBearer.getInstance().getFriendPhoneNumber());
        inviteUploadRequest.setCallingPhoneNum("0244445555");
        inviteUploadRequest.setCallingNickName("It's me!");
        inviteUploadRequest.setLocale(Locale.KOREA.toString()); //ko_KR | en_US
        inviteUploadRequest.setFilePath(RingBearer.getInstance().getMediaUrl());
        InviteResult inviteResult= null;

        try {
            JsonHttpCaller caller = new JsonHttpCaller();
            inviteResult = caller.inviteUploadMedia(inviteUploadRequest);
        } catch (Exception e) {
            Log.e(TAG, "Calling Asynch InviteAndUpload",e);
        }
        return inviteResult;
    }

    @Override
    protected void onPostExecute(InviteResult strResult) {
        super.onPostExecute(strResult);
        delegate.OnTaskCompleted(strResult);
    }
}

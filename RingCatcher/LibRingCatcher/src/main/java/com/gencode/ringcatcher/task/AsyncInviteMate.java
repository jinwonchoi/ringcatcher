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
import com.gencode.ringcatcher.obj.JsonConstants;

import java.util.Locale;

/**
 * Created by Administrator on 2016-04-20.
 */
public class AsyncInviteMate extends AsyncTask<String, Void, InviteResult> {
    final static String TAG = AsyncInviteMate.class.getSimpleName();
    IInvteMateTask delegate = null;
    public AsyncInviteMate(IInvteMateTask task) {
        delegate = task;
    }

    /**
     * http로 서버에 친구에게 음원등록
     * args[0]:tkenid, args[1] callingNum, args[2] callingName, args[3] localStr
     */
    @Override
    protected InviteResult doInBackground(String... args) {
        InviteUploadRequest inviteUploadRequest= new InviteUploadRequest();
        inviteUploadRequest.setTokenId(args[0]);
        inviteUploadRequest.setFriendPhoneNum(args[1]);
        inviteUploadRequest.setCallingPhoneNum(args[2]/*"0244445555"*/);
        inviteUploadRequest.setCallingNickName(args[3]/*"It's me!"*/);
        inviteUploadRequest.setLocale(args[4]);//Locale.KOREA.toString()); //ko_KR | en_US
        inviteUploadRequest.setFilePath(args[5]);
        inviteUploadRequest.setExpiredDate(args[6]);
        inviteUploadRequest.setDurationType(args[7]);
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

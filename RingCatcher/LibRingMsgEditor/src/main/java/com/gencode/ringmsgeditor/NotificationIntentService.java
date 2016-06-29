package com.gencode.ringmsgeditor;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import com.gencode.ringcatcher.gcm.QuickstartPreferences;
import com.gencode.ringcatcher.http.MessageWrapper;
import com.gencode.ringmsgeditor.task.AsyncInsertMessage;
import com.gencode.ringmsgeditor.task.IInsertMessageTask;

import org.json.JSONException;

import java.util.Map;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class NotificationIntentService extends IntentService {
    final String TAG = this.getClass().getName();
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String EXTRA_CALLING_NUM = "callingNum";
    private static final String EXTRA_CALLING_NAME = "callingName";
    private static final String EXTRA_JSON_MESSAGE= "jsonMessage";

    public NotificationIntentService() {
        super("NotificationIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionNotifyMessageRegister(Context context, String callingNum, String callingName, String jsonMessage) {
        Intent intent = new Intent(context, NotificationIntentService.class);
        intent.setAction(QuickstartPreferences.ACTION_MESSAGE_REGISTER);
        intent.putExtra(EXTRA_CALLING_NUM, callingNum);
        intent.putExtra(EXTRA_CALLING_NAME, callingName);
        intent.putExtra(EXTRA_JSON_MESSAGE, jsonMessage);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (QuickstartPreferences.ACTION_MESSAGE_REGISTER.equals(action)) {
                final String callingNum = intent.getStringExtra(EXTRA_CALLING_NUM);
                final String callingName = intent.getStringExtra(EXTRA_CALLING_NAME);
                final String jsonMessage = intent.getStringExtra(EXTRA_JSON_MESSAGE);
                handleActionMessageRegister(callingNum, callingName,jsonMessage);
            } else {
                Log.d(TAG, "onHandleIntent action:"+action);
            }
        }
    }

    /**
     * 받은 메시지를 DB에 저장
     */
    private void handleActionMessageRegister(String callingNum,String callingName,String jsonMessage) {
        try {
            String myPhoneNumber = Utils.getMyPhoneNumber(this);
            //테스트용 : 0244445555/01055557777 2개번호를 번갈아 사용.
            if (getResources().getString(R.string.test_my_phone_number).equals(callingNum))
                myPhoneNumber =getResources().getString(R.string.test_my_phone_number2);
            MessageWrapper messageWrapper = new MessageWrapper(jsonMessage, true);
            Map<String, String> messageMap = messageWrapper.getMessageList();

            new AsyncInsertMessage(new IInsertMessageTask() {
                @Override
                public void onTaskCompleted(boolean result) {
                    Log.d(TAG, "AsyncInsertMessage.onTaskCompleted result="+result);
                }
            }, new ContentResolverHelper(this), callingNum, myPhoneNumber).execute(messageMap);
        } catch (JSONException e) {
            Log.e(TAG, "handleActionMessageRegister",e);
        }
    }

}

package com.gencode.sampleapp;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import com.gencode.ringcatcher.gcm.QuickstartPreferences;
import com.gencode.ringcatcher.obj.JsonConstants;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * GCM을 통해 상대방이 등록한 ring info를 받으면 사용화면에 보여주기 보다,
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class RingRegistrationIntentService extends IntentService {
    final String TAG = this.getClass().getName();

    public RingRegistrationIntentService() {
        super("RingRegistrationIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String callingNum, String callingName, String ringSrcUrl) {
        Intent intent = new Intent(context, RingRegistrationIntentService.class);
        intent.setAction(QuickstartPreferences.ACTION_RING_REGISTER);
        intent.putExtra(JsonConstants.callingNum, callingNum);
        intent.putExtra(JsonConstants.callingName, callingName);
        intent.putExtra(JsonConstants.ringSrcUrl, ringSrcUrl);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (QuickstartPreferences.ACTION_RING_REGISTER.equals(action)) {
                final String callingNum = intent.getStringExtra(JsonConstants.callingNum);
                final String callingName = intent.getStringExtra(JsonConstants.callingName);
                final String ringSrcUrl = intent.getStringExtra(JsonConstants.ringSrcUrl);
                handleActionRingRegister(callingNum, callingName,ringSrcUrl);
            } else {
                Log.d(TAG, "onHandleIntent action:"+action);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionRingRegister(String callingNum,String callingName,String ringSrcUrl) {
        try {
            Log.d(TAG, String.format("handleActionRingRegister callingNum[%s]callingName[%s]ringSrcUrl[%s]", callingNum, callingName, ringSrcUrl));
        } catch (Exception e) {
            Log.e(TAG, "handleActionMessageRegister",e);
        }

    }
}

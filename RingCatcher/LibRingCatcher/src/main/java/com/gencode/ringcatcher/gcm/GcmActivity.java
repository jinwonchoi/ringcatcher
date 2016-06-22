package com.gencode.ringcatcher.gcm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.gencode.ringcatcher.common.RingBearer;
import com.gencode.ringcatcher.obj.JsonConstants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

abstract public class GcmActivity extends AppCompatActivity {
    private static final String TAG = GcmActivity.class.getSimpleName();

    protected static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    protected BroadcastReceiver mRegistrationBroadcastReceiver;
    protected BroadcastReceiver mRingNotificationBroadcastReceiver;
    protected boolean isReceiverRegistered;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        initializeGcmService();

        setTokenIdInRingBearer(this);
    }

    protected void initializeGcmService() {

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //GCM에서 token을 받은 경우 처리할 일.
                //서버에 token id/phone number등록은 구현activity에서 처리
                setTokenIdInRingBearer(context);            }
        };

        mRingNotificationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //GCM에서 ring 등록정보를 noti받은 경우
                String callingNum = intent.getStringExtra(JsonConstants.callingNum);
                Log.d(TAG, "savedInstanceState callingNum="+callingNum);
                //상대방이 ring message를 보내서 gcm으로 받은 경우.
                //구현하는 activity에서 db 저장등을 구현해야 한다.
                if (callingNum != null && !callingNum.equals("")) {
                    processNotificationMessage();
                }
            }
        };


        // Registering BroadcastReceiver
        registerReceiver();

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    private void setTokenIdInRingBearer(Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        String tokenId = sharedPreferences
                .getString(QuickstartPreferences.TOKEN_ID_FROM_GCM, "");
        RingBearer.getInstance().setTokenId(tokenId);
        Log.d(TAG,"setTokenIdInRingBearer tokenId:"+tokenId );

    }
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRingNotificationBroadcastReceiver);
        isReceiverRegistered = false;
        super.onPause();
    }

    private void registerReceiver(){
        LocalBroadcastManager.getInstance(this).registerReceiver(mRingNotificationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.NOTIFY_RING_REGISTER));
        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.TOKEN_ID_RECEIVED));
            isReceiverRegistered = true;
        }
    }

    /**
     * Persist registration to third-party servers.
     *
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     */
    abstract protected void processNotificationMessage();

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

}

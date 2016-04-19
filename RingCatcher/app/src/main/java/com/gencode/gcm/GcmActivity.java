package com.gencode.gcm;

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
import android.widget.Toast;

import com.gencode.ringcatcher.R;
import com.gencode.ringcatcher.obj.JsonConstants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

abstract public class GcmActivity extends AppCompatActivity {
    private static final String TAG = GcmActivity.class.getSimpleName();

    protected static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    protected BroadcastReceiver mRegistrationBroadcastReceiver;
    protected boolean isReceiverRegistered;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreateAaaaAA");
        initializeGcmService();

        Intent intent = getIntent();
        String callerNum = intent.getStringExtra(JsonConstants.callerNum);
        Log.d(TAG, "savedInstanceState callerNum="+callerNum);
        if (callerNum != null && !callerNum.equals("")) {
            sendRegistrationToServer();
        }
    }

    protected void initializeGcmService() {

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                    Toast.makeText(context, R.string.gcm_send_message, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, R.string.token_error_message, Toast.LENGTH_LONG).show();
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

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
        super.onPause();
    }

    private void registerReceiver(){
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
    abstract protected void sendRegistrationToServer();
//    {
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        String token = sharedPreferences.getString(QuickstartPreferences.TOKEN_ID_FROM_GCM,"");
//        Log.d(TAG, "sendRegistrationToServer Token Id="+token);
//        // TODO: Implement this method to send any registration to your app's servers.
//        // Add custom implementation, as needed.
//        // You should store a boolean that indicates whether the generated token has been
//        // sent to your server. If the boolean is false, send the token to your server,
//        // otherwise your server should have already received the token.
//        //sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, true).apply();
//        try {
//            //call server
//            //sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, true).apply();
//
//        } catch (Exception e) {
//            //sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false).apply();
//
//        }
//    }
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

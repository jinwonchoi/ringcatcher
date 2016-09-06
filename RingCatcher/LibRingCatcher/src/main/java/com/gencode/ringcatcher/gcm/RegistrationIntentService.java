package com.gencode.ringcatcher.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;

import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;

import com.gencode.ringcatcher.utils.Logger;
import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;
import java.util.Random;

import com.gencode.ringcatcher.R;   // package 이름과 틀려서 ...

/*
    현상 : WIFI-OFF , LTE/3G-ON 상태에서 SERVICE-NOT_AVAILABLE 오류가 계속해서 발생함.

    http://stackoverflow.com/questions/17188982/how-to-fix-google-cloud-messaging-registration-error-service-not-available

     - gooole play service가 백그라운드로 돌지 못하도록 세팅되어 있으면 위와 같은 오류가 발생함..
       5.0 에서는 google play service 환경설정이 없음..

       Settings->Data usage->Backgroud data->Google Settings 가 백그라운드에서 돌수 있도록 세팅할것..
    http://stackoverflow.com/questions/22877855/gcm-registration-service-not-available-on-mobile-network


    - token 획득 실패시 여러번 retry 하면 3g-only 에서도 token 획득이 되는 경우가 있었음..
    http://stackoverflow.com/questions/25129611/gcm-register-service-not-available/25183437#25183437
 */
public class RegistrationIntentService extends IntentService
{
    private static final String TAG = "[" + RegistrationIntentService.class.getSimpleName() + "]";
    private static final String[] TOPICS = {"global"};
    public boolean mbDebug = true;

    public RegistrationIntentService()
    {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {                                                                                               if ( mbDebug ) Logger.i(TAG, "[onHandleIntent] start ==== " );
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int i;

        Random randomno = new Random();
        final int MAX_ATTEMPTS = 10;
        final int BACKOFF_MILLI_SECONDS = 2000;
        long backoff = BACKOFF_MILLI_SECONDS + randomno.nextInt(1000);                              if (mbDebug) Logger.i(TAG, "[onHandleIntent] backoff : " + backoff );

        for ( i=0 ; i < MAX_ATTEMPTS ; ++i )
        {
            try
            {
                // [START register_for_gcm]
                // Initially this call goes out to the network to retrieve the token, subsequent calls
                // are local.
                // R.string.gcm_defaultSenderId (the Sender ID) is typically derived from google-services.json.
                // See https://developers.google.com/cloud-messaging/android/start for details on this file.
                // [START get_token]

                //InstanceID instanceID = InstanceID.getInstance(this);
                InstanceID instanceID = InstanceID.getInstance( getApplicationContext() );

                String token = instanceID.getToken(
                        getString(R.string.gcm_defaultSenderId),
                        GoogleCloudMessaging.INSTANCE_ID_SCOPE,
                        null);

                // [END get_token]
                                                                                                    if (mbDebug) Logger.i(TAG, "[onHandleIntent] GCM Registration Token: " + token);

                sendRegistrationToServer(token);

                // Subscribe to topic channels
                subscribeTopics(token);

                sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, true).apply();
                sharedPreferences.edit().putString(QuickstartPreferences.TOKEN_ID, token).apply();

                String wifi_only =
                        sharedPreferences.getString(QuickstartPreferences.WIFI_ONLY, "first");      if (wifi_only.equalsIgnoreCase("first")) sharedPreferences.edit().putString(QuickstartPreferences.WIFI_ONLY, "N").apply();

                String test_mode =
                        sharedPreferences.getString(QuickstartPreferences.WIFI_ONLY, "first");      if (test_mode.equalsIgnoreCase("first")) sharedPreferences.edit().putString(QuickstartPreferences.TEST_MODE, "N").apply();

            }
            catch (Exception e)
            {
                                                                                                    if (mbDebug) Logger.e(TAG, "[onHandleIntent] Failed to complete token refresh", e);
                // If an exception happens while fetching the new token or updating our registration data
                // on a third-party server, this ensures that we'll attempt the update at a later time.
                sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false).apply();

                if ( i == MAX_ATTEMPTS )
                    break;
                                                                                                    if (mbDebug) Logger.e(TAG, "[onHandleIntent] Couldn't get token; waiting "+String.valueOf(backoff) + "ms");

                try {
                    Thread.sleep(backoff);
                }
                catch (InterruptedException e1) {
                    break;
                }
            }

            backoff *= 2;
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(QuickstartPreferences.REGISTRATION_COMPLETE);      if ( mbDebug ) Logger.i(TAG, "[onHandleIntent] " + QuickstartPreferences.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);                if ( mbDebug ) Logger.i(TAG, "[onHandleIntent] finish ==== " );

        // [END register_for_gcm]

    }

    /**
     * Persist registration to third-party servers.
     *
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
    }

    /**
     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
     *
     * @param token GCM token
     * @throws IOException if unable to reach the GCM PubSub service
     */
    // [START subscribe_topics]
    private void subscribeTopics(String token) throws IOException {                                 if ( mbDebug ) Logger.i(TAG, "[subscribeTopics] start ==== " );
        GcmPubSub pubSub = GcmPubSub.getInstance(this);                                             if ( mbDebug ) Logger.i(TAG, "[subscribeTopics] token : " + token );
                                                                                                    if ( pubSub == null ) if ( mbDebug ) Logger.i(TAG, "[subscribeTopics] pubSub is null " );
        for (String topic : TOPICS) {
            pubSub.subscribe(token, "/topics/" + topic, null);                                      if ( mbDebug ) Logger.i(TAG, "[subscribeTopics] topic : " +  token + "/topics/" + topic );
        }                                                                                           if ( mbDebug ) Logger.i(TAG, "[subscribeTopics] finish ==== " );
    }
    // [END subscribe_topics]

}


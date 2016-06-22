/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gencode.ringcatcher.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.gencode.ringcatcher.R;
import com.gencode.ringcatcher.obj.JsonConstants;
import com.google.android.gms.gcm.GcmListenerService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class GencodeGcmListenerService extends GcmListenerService {

    private static final String TAG = GencodeGcmListenerService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        String packageName = this.getPackageName();
        Intent intent = this.getPackageManager().getLaunchIntentForPackage(packageName);
        String className = intent.getComponent().getClassName();
        Log.d(TAG, "onCreate");
    }

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        Log.d(TAG, "onMessageReceived From: " + from);
        Log.d(TAG, "onMessageReceived Message: " + message);

        if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }

        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        sendNotification(message);
        // [END_EXCLUDE]
    }
    // [END receive_message]



    /**
     * GCM에서 노티를 받았을때
     * 1. ring 등록info를 받았을때 --> GcmActivity상속자에 message 정보추가 후 intent 호출(ring message를 DB등록은 구현자가 처리)
     * 2. message 등록 info를 받았을때-> com.gencode.ringmsgeditor 로 message 정보추가 후 intent 호출(내부적으로 message를 DB등록함)
     * 3. 1,2번 작업후 폰에 노티디스플레이 함.
     * 
     * @param message GCM message received.
     */
    private void sendNotification(String message) {


        JSONObject jsonRootObject = null;
        try {
            Log.d(TAG, "sendNotification message="+message);
            jsonRootObject = new JSONObject(message);
            String displayMsg = null;
            String title = null;
            String actType = jsonRootObject.optString(JsonConstants.actType);
            Intent intent = null;
            Log.d(TAG, "jsonRootObject.actType:"+actType);
            String notificationAction = null;
            if (actType!=null && actType.equals(JsonConstants.ACT_TYPE_RING)){
                displayMsg = String.format(getResources().getString(R.string.noti_body_ring), jsonRootObject.optString(JsonConstants.callingName),jsonRootObject.optString(JsonConstants.callingNum));
                title = getResources().getString(R.string.noti_title_ring);
                Log.d(TAG, "jsonRootObject.toString:"+jsonRootObject.toString());
                //intent = this.getPackageManager().getLaunchIntentForPackage(this.getPackageName());
                intent = new Intent();
                intent.setAction(QuickstartPreferences.ACTION_RING_REGISTER);
                intent.putExtra(JsonConstants.callingNum,jsonRootObject.optString(JsonConstants.callingNum));
                intent.putExtra(JsonConstants.callingName,jsonRootObject.optString(JsonConstants.callingName));
                intent.putExtra(JsonConstants.ringSrcUrl,jsonRootObject.optString(JsonConstants.ringSrcUrl));
                Log.d(TAG, QuickstartPreferences.NOTIFY_RING_REGISTER);
                notificationAction = QuickstartPreferences.ACTION_RING_VIEW;

            } else if (actType!=null && actType.equals(JsonConstants.ACT_TYPE_MESSAGE)){
                displayMsg = String.format(getResources().getString(R.string.noti_body_message), jsonRootObject.optString(JsonConstants.callingName),jsonRootObject.optString(JsonConstants.callingNum));
                title = getResources().getString(R.string.noti_title_message);

                intent = new Intent(QuickstartPreferences.ACTION_MESSAGE_REGISTER);
                intent.putExtra(JsonConstants.callingNum,jsonRootObject.optString(JsonConstants.callingNum));
                intent.putExtra(JsonConstants.callingName,jsonRootObject.optString(JsonConstants.callingName));
                intent.putExtra(JsonConstants.jsonMessage,jsonRootObject.optString(JsonConstants.jsonMessage));

                Log.d(TAG, QuickstartPreferences.NOTIFY_MESSAGE_REGISTER);
                notificationAction = QuickstartPreferences.ACTION_MESSAGE_VIEW;
            }
            getApplicationContext().startService(intent);
            //notify용으로 변경
            intent.setAction(notificationAction);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            //{"userNum":"01055557777","callerNum":"0244445555","ringSrcUrl":"\/ringmedia\/20160419\/0244445555_01055557777.jpg","callerName":"Mememe"}
            /*
                displayMsg = String.format("%s(%s)님이 벨소리 변경을 요청하셨습니다.", jsonRootObject.optString(JsonConstants.callerName),jsonRootObject.optString(JsonConstants.callerNum));
                title = "Ringcatcher에서 확인하세요.";
                displayMsg = String.format("You have a new ring sound from %s(%s).", jsonRootObject.optString(JsonConstants.callerName),jsonRootObject.optString(JsonConstants.callerNum));
                title = "Please your Ringcatcher's updates.";

                displayMsg = String.format("%s(%s)님의 메시지가 도착하였습니다.", jsonRootObject.optString(JsonConstants.callerName),jsonRootObject.optString(JsonConstants.callerNum));
                title = "Ringcatcher에서 확인하세요.";
                displayMsg = String.format("You have a new message from %s(%s).", jsonRootObject.optString(JsonConstants.callerName),jsonRootObject.optString(JsonConstants.callerNum));
                title = "Please your Ringcatcher's updates.";
            * */

            //// TODO: 2016-06-19
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_stat_ic_notification)
                    .setContentTitle(title)
                    .setContentText(displayMsg)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        } catch (JSONException e) {
            Log.e(TAG, "JSON parsing failed:"+message);
        }
    }
}

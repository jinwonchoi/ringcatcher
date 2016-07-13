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

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.gencode.ringcatcher.obj.JsonConstants;

public class RingNotificationService extends IntentService {

    private static final String TAG = RingNotificationService.class.getSimpleName();
    private static final String[] TOPICS = {"global"};

    public RingNotificationService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            String callingNum = intent.getStringExtra(JsonConstants.callingNum);
            String callingName = intent.getStringExtra(JsonConstants.callingName);
            String ringSrcUrl = intent.getStringExtra(JsonConstants.ringSrcUrl);
            String expiredDate = intent.getStringExtra(JsonConstants.expiredDate);
            String durationType = intent.getStringExtra(JsonConstants.durationType);
            Log.d(TAG,"onHandleIntent callingNum="+callingNum );
            if (callingNum != null && !callingNum.equals("")) {
                Intent notifyingIntent = new Intent(QuickstartPreferences.NOTIFY_RING_REGISTER);
                notifyingIntent.putExtra(JsonConstants.callingNum, callingNum);
                notifyingIntent.putExtra(JsonConstants.callingName, callingName);
                notifyingIntent.putExtra(JsonConstants.ringSrcUrl, ringSrcUrl);

                notifyingIntent.putExtra(JsonConstants.expiredDate, expiredDate);
                notifyingIntent.putExtra(JsonConstants.durationType, durationType);

                // Notify UI that registration has completed, so the progress indicator can be hidden.
                Log.d(TAG, QuickstartPreferences.NOTIFY_RING_REGISTER);
                LocalBroadcastManager.getInstance(this).sendBroadcast(notifyingIntent);
            }

        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
        }
    }
}

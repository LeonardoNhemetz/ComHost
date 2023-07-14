
package com.iurd_com.Menssagens;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.iurd_com.R;
import com.sinch.android.rtc.NotificationResult;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchHelpers;
import com.sinch.android.rtc.calling.CallNotificationResult;
import com.sinch.android.rtc.messaging.Message;
import com.sinch.android.rtc.messaging.MessageClient;

import java.util.Map;

public class FirebaseMenssagem extends FirebaseMessagingService {

    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (SinchHelpers.isSinchPushPayload( remoteMessage.getData() ))
        {


        } else {
            // it's NOT Sinch message - process yourself
        }
    }


}

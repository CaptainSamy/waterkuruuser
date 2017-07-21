package jp.co.wssj.iungo.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.model.ErrorMessage;
import jp.co.wssj.iungo.model.firebase.FirebaseModel;
import jp.co.wssj.iungo.model.firebase.NotificationMessage;
import jp.co.wssj.iungo.screens.MainActivity;
import jp.co.wssj.iungo.utils.Constants;
import jp.co.wssj.iungo.utils.Logger;

/**
 * Created by tuanle on 5/30/17.
 */

public class FirebaseMsgService extends FirebaseMessagingService {

    private static final String TAG = FirebaseMsgService.class.getSimpleName();

    private FirebaseModel mFirebaseModel;

    private NotificationManager mNotificationManager;

    public static final String EXTRA_NOTIFICATION = "extra_notification";

    @Override
    public void onCreate() {
        super.onCreate();
        mFirebaseModel = new FirebaseModel(this);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Logger.d(TAG, "From: " + remoteMessage.getFrom());

        Intent sentToActivity = new Intent();
        sentToActivity.setAction(Constants.ACTION_SERVICE_ACTIVITY);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(sentToActivity);


        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Logger.d(TAG, "Message data payload: " + remoteMessage.getData());
            mFirebaseModel.parseNotificationData(remoteMessage.getData(), new FirebaseModel.IParseNotificationCallback() {

                @Override
                public void onSuccess(NotificationMessage notificationMessage) {
                    Logger.d(TAG, "#parseNotificationData onSuccess " + notificationMessage.getMessage());
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(FirebaseMsgService.this).setContentText(notificationMessage.getMessage());
                    if (!TextUtils.isEmpty(notificationMessage.getTitle())) {
                        builder.setContentTitle(notificationMessage.getTitle());
                    }
                    builder.setLights(Color.BLUE, 500, 500);
                    long[] pattern = {500, 500, 500, 500, 500, 500, 500, 500, 500};
                    builder.setVibrate(pattern);
                    Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    builder.setSound(alarmSound);
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder.setColor(getResources().getColor(R.color.colorMain));
                        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.logo_app);
                        builder.setLargeIcon(largeIcon);
                        builder.setSmallIcon(R.drawable.image_notification);
                    } else {
                        builder.setSmallIcon(R.mipmap.logo_app);
                    }
//                    Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.logo_app);
//                    builder.setSmallIcon(R.mipmap.logo_app).setLargeIcon(largeIcon);
//                    builder.setLargeIcon(largeIcon);

                    Intent intent = new Intent(FirebaseMsgService.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_SINGLE_TOP |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(EXTRA_NOTIFICATION, notificationMessage);
                    PendingIntent pendingIntent = PendingIntent.getActivity(FirebaseMsgService.this, (int) notificationMessage.getPushId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(pendingIntent);
                    mNotificationManager.notify((int) notificationMessage.getPushId(), builder.build());
                }

                @Override
                public void onFailure(ErrorMessage errorMessage) {
                    Logger.d(TAG, "#parseNotificationData failure " + errorMessage);
                }
            });
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Logger.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }
}

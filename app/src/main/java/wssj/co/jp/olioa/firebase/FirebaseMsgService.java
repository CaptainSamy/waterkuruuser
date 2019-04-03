package wssj.co.jp.olioa.firebase;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;

import wssj.co.jp.olioa.BuildConfig;
import wssj.co.jp.olioa.R;
import wssj.co.jp.olioa.model.ErrorMessage;
import wssj.co.jp.olioa.model.firebase.FirebaseModel;
import wssj.co.jp.olioa.model.firebase.NotificationMessage;
import wssj.co.jp.olioa.screens.MainActivity;
import wssj.co.jp.olioa.utils.Constants;
import wssj.co.jp.olioa.utils.Logger;

/**
 * Created by tuanle on 5/30/17.
 */

public class FirebaseMsgService extends FirebaseMessagingService {

    private static final String TAG = FirebaseMsgService.class.getSimpleName();

    private FirebaseModel mFirebaseModel;

    private NotificationManager mNotificationManager;

    public static final String EXTRA_BUNDLE = "extra_bundle";

    public static final String KEY_NOTIFICATION = "key_notification";

    @Override
    public void onCreate() {
        super.onCreate();
        mFirebaseModel = new FirebaseModel(this);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        Logger.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Logger.d(TAG, "Message data payload: " + remoteMessage.getData());
            mFirebaseModel.parseNotificationData(remoteMessage.getData(), new FirebaseModel.IParseNotificationCallback() {

                @Override
                public void onSuccess(NotificationMessage notificationMessage) {
                    Intent sentToActivity = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(KEY_NOTIFICATION, notificationMessage);
                    sentToActivity.putExtra(KEY_NOTIFICATION, bundle);
                    sentToActivity.setAction(Constants.ACTION_REFRESH_LIST_PUSH);
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(sentToActivity);
                    if (!isAppOnTop()) {
                        Intent intent = new Intent(FirebaseMsgService.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_SINGLE_TOP |
                                Intent.FLAG_ACTIVITY_NEW_TASK);
                        Bundle b = new Bundle();
                        bundle.putParcelable(KEY_NOTIFICATION, notificationMessage);
                        sentToActivity.putExtra(KEY_NOTIFICATION, bundle);
                        intent.putExtras(b);
                        Logger.d(TAG, "#parseNotificationData onSuccess " + notificationMessage.getMessage());
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(FirebaseMsgService.this);
                        builder.setContentTitle(notificationMessage.getTitle());
                        if (!TextUtils.isEmpty(notificationMessage.getMessage())) {
                            builder.setContentText(notificationMessage.getMessage());
                        }
                        builder.setLights(Color.BLUE, 500, 500);
                        long[] pattern = {500, 500, 500, 500, 500, 500, 500, 500, 500};
                        builder.setVibrate(pattern);
                        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        builder.setSound(alarmSound);
                        builder.setAutoCancel(true);
                        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            builder.setColor(getResources().getColor(R.color.colorMain));
                        }
                        builder.setSmallIcon(R.mipmap.image_notification);
                        PendingIntent pendingIntent = PendingIntent.getActivity(FirebaseMsgService.this, (int) notificationMessage.getPushId(), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
                        builder.setContentIntent(pendingIntent);
                        mNotificationManager.notify((int) notificationMessage.getPushId(), builder.build());
                    } else {
                        try {
                            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                            r.play();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(ErrorMessage errorMessage) {
                    Logger.d(TAG, "#parseNotificationData failure " + errorMessage);
                }
            });
        }
        if (remoteMessage.getNotification() != null) {
            Logger.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    protected boolean isAppOnTop() {
        ActivityManager am = (ActivityManager) this.getSystemService(Service.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks;
        tasks = am.getRunningTasks(1);
        ActivityManager.RunningTaskInfo running = tasks.get(0);
        String packageName = running.topActivity.getPackageName();
        if (packageName.equals(BuildConfig.APPLICATION_ID)) {
            return true;
        } else {
            return false;
        }
    }
}

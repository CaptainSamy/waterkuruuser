package wssj.co.jp.obis.firebase;

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

import wssj.co.jp.obis.BuildConfig;
import wssj.co.jp.obis.R;
import wssj.co.jp.obis.model.ErrorMessage;
import wssj.co.jp.obis.model.firebase.FirebaseModel;
import wssj.co.jp.obis.model.firebase.NotificationMessage;
import wssj.co.jp.obis.screens.MainActivity;
import wssj.co.jp.obis.utils.Constants;
import wssj.co.jp.obis.utils.Logger;

/**
 * Created by tuanle on 5/30/17.
 */

public class FireBaseMsgService extends FirebaseMessagingService {

    private static final String TAG = FireBaseMsgService.class.getSimpleName();

    public static final String ACTION_PUSH_CHAT = "1000";

    public static final String ACTION_PUSH_GROUP = "2000";

    private FirebaseModel mFireBaseModel;

    private NotificationManager mNotificationManager;

    public static final String KEY_NOTIFICATION = "key_notification";

    @Override
    public void onCreate() {
        super.onCreate();
        mFireBaseModel = new FirebaseModel(this);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        Logger.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Logger.d(TAG, "Message data payload: " + remoteMessage.getData());
            mFireBaseModel.parseNotificationData(remoteMessage.getData(), new FirebaseModel.IParseNotificationCallback() {

                @Override
                public void onSuccess(NotificationMessage notificationMessage) {
                    //app not running
                    Intent sentToActivity = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(KEY_NOTIFICATION, notificationMessage);
                    sentToActivity.putExtra(KEY_NOTIFICATION, bundle);
                    sentToActivity.setAction(Constants.ACTION_REFRESH_LIST_PUSH);
                    //app  is top stack
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(sentToActivity);
                    if (isAppOnTop()) {
                        try {
                            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                            r.play();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Intent intent = new Intent(FireBaseMsgService.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_SINGLE_TOP |
                                Intent.FLAG_ACTIVITY_NEW_TASK);
                        //intent.putExtra(KEY_NOTIFICATION, notificationMessage.getAction());
                        Logger.d(TAG, "#parseNotificationData onSuccess " + notificationMessage.getMessage());
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(FireBaseMsgService.this);
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
                        builder.setSmallIcon(R.drawable.image_notification);
                        PendingIntent pendingIntent = PendingIntent.getActivity(FireBaseMsgService.this, (int) notificationMessage.getPushId(), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
                        builder.setContentIntent(pendingIntent);
                        mNotificationManager.notify((int) notificationMessage.getPushId(), builder.build());

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

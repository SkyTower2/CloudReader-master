package com.example.jingbin.cloudreader.service;

import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.example.jingbin.cloudreader.R;
import com.example.jingbin.cloudreader.ui.MainActivity;

/**
 * @author ml
 * @date 2024-11-22 9:43
 * 描述：前台服务
 */
public class CloudReaderService extends Service {
    public static final String NOTIFICATION_CHANNEL_ID = "cloudReader";
    private static final int BACKUP_SERVICE_NOTIFICATION_ID = Integer.MAX_VALUE >> 2;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        int pendingIntentFlag;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                getApplicationInfo().targetSdkVersion >= Build.VERSION_CODES.S) {
            // Targeting S+ (version 31 and above) requires that one of FLAG_IMMUTABLE or FLAG_MUTABLE be specified when creating a PendingIntent.
            // Strongly consider using FLAG_IMMUTABLE, only use FLAG_MUTABLE if some functionality depends on the PendingIntent being mutable, e.g.
            // if it needs to be used with inline replies or bubbles.
            pendingIntentFlag = PendingIntent.FLAG_IMMUTABLE;
        } else {
            pendingIntentFlag = PendingIntent.FLAG_UPDATE_CURRENT;
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, pendingIntentFlag);

        String applicationName = getApplicationName();
        if (TextUtils.isEmpty(applicationName)) {
            applicationName = getPackageName();
        }

        Builder builder = new Builder(this)
                //设置大图标，不设置则默认为程序图标
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_cloudreader))
                //设置标题
                .setContentTitle(getString(R.string.app_name))
                //设置内容
                .setContentText(getString(R.string.logcat_notify_content, applicationName))
                //设置小图标
                .setSmallIcon(R.drawable.ic_cloudreader_mip)
                .setContentIntent(pendingIntent);

        //设置通知渠道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //通知渠道的id
            String notificationChannelId = NOTIFICATION_CHANNEL_ID;
            NotificationChannel channel = new NotificationChannel(notificationChannelId,
                    getString(R.string.logcat_notify_channel_name), NotificationManager.IMPORTANCE_MIN);
            //配置通知渠道的属性
            channel.setDescription(getString(R.string.logcat_notify_channel_description));
            //关闭声音通知
            channel.setImportance(NotificationManager.IMPORTANCE_MIN);
            //关闭震动通知
            channel.enableVibration(false);
            //关闭闪光灯通知
            channel.enableLights(false);

            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
            builder.setChannelId(notificationChannelId);
        } else {
            //关闭声音通知
            builder.setSound(null)
                    //关闭震动通知
                    .setVibrate(null)
                    //关闭闪光灯通知
                    .setLights(0, 0, 0);
        }

        //设置通知为常驻通知(防止通知被用户手动清除)
        builder.setOngoing(true)
                .setVisibility(Notification.VISIBILITY_PUBLIC); //锁屏显示完整通知

        //将服务和通知绑定在一起，成为前台服务
        startForeground(BACKUP_SERVICE_NOTIFICATION_ID, builder.build());
        return super.onStartCommand(intent, flags, startId);
    }

    private String getApplicationName() {
        PackageManager packageManager = getPackageManager();
        try {
            return packageManager.getApplicationLabel(packageManager
                    .getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA)).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
}

package com.liemi.basemall.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import com.liemi.basemall.R;
import com.netmi.baselibrary.ui.MApplication;
import com.netmi.baselibrary.utils.Logs;

/*
* 状态栏消息相关
* */
public class NotificationUtils {
    //消息种类，目前只有普通消息和注册消息
    public static final int MESSAGE_NORMAL = 100000;//普通消息，信鸽发送的普通消息
    public static final int MESSAGE_REGISTER = 100001;//注册消息，用户注册成功以后会发送一条消息

    private static int messageId = 1000;//默认消息id
    private static String notificationId = "channel_message";
    private static String name = "三享读书";
    private static Context context = MApplication.getAppContext();

    //发送消息
    public static void sendNotification(String title,String content,PendingIntent pi){
        sendNotification(MESSAGE_NORMAL,messageId,title,content,pi);
    }

    //发送消息
    public static void sendNotification(int type,int id, String title, String content, PendingIntent pi) {

        //获取NotificationManager实例
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel mChannel = new NotificationChannel(notificationId, name, NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(mChannel);
            notification = new Notification.Builder(context)
                    .setChannelId(notificationId)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setSmallIcon(R.mipmap.icon_company)
                    .setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE)
                    .setContentIntent(pi)
                    .build();
        } else {
            Logs.i("PUSH_MESSAGE 版本较低");
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.icon_company)
                    .setChannelId(notificationId)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE)
                    .setContentIntent(pi);
            notification = notificationBuilder.build();
        }

        Logs.i("PUSH_MESSAGE Notification配置完成");
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        if(notificationManager != null) {
            Logs.i("PUSH_MESSAGE 显示通知 ");
            notificationManager.notify("pushMessage", id, notification);
        }else{
            Logs.i("PUSH_MESSAGE notificationManager为空，无法显示通知 ");
        }
    }
}

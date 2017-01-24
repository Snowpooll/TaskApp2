package jp.techacademy.kubota.satoru.taskapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.provider.Settings;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import io.realm.Realm;

/**
 * Created by snowpool on 17/01/20.
 */

public class TaskAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //notification setting
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.small_icon);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.large_icon));
        builder.setWhen(System.currentTimeMillis());
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setAutoCancel(true);

        //EXTRA_TASK get ID, ID is get Task context
        int taskId = intent.getIntExtra(MainActivity.EXTRA_TASK,-1);
        Realm realm =Realm.getDefaultInstance();
        Task task = realm.where(Task.class).equalTo("id",taskId).findFirst();
        realm.close();

        //notification tap is app wake
        Intent startAppIntent = new Intent(context,MainActivity.class);
        startAppIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,startAppIntent,0);
        builder.setContentIntent(pendingIntent);

        //notification show
        NotificationManager notificationManager =(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(task.getId(),builder.build());
    }
}

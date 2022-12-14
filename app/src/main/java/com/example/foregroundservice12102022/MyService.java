package com.example.foregroundservice12102022;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Created by pphat on 12/12/2022.
 */
public class MyService extends Service {

    final String CHANNEL_ID = "My channel";
    Notification notification;
    List<Integer> listNumbers;
    boolean isSort = false;
    Context context;
    NotificationManager notificationManager;
    Handler handler;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        notification = createNotification(this,"Service đang chạy");
        listNumbers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            listNumbers.add(new Random().nextInt(100) + 1);
        }
        handler = new Handler(Looper.getMainLooper(), callback);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        startForeground(1, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!isSort) {
            isSort = true;
            new MyThread(handler, listNumbers).start();
        }
        return START_NOT_STICKY;
    }

    private Handler.Callback callback = new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            notification = createNotification(context, message.obj.toString());
            notificationManager.notify(1, notification);
            return true;
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
    }

    public Notification createNotification(Context context, String message) {
        Uri sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.nhac_1);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Thông báo")
                .setContentText(message)
//                .setSound(sound)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "Demo", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        return builder.build();
    }
}

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
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

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
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notification = createNotification(this);
        listNumbers = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            listNumbers.add(new Random().nextInt(100) + 1);
        }
        startForeground(1, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!isSort) {
            isSort = true;
            sortNumber(listNumbers, new OnSortListener() {
                @Override
                public void onFinish(List<Integer> newListNumber) {
                    String textValue = TextUtils.join(", ", newListNumber);
                    Log.d("pphat", textValue);
                    isSort = false;
                }
            });
        }
        return START_NOT_STICKY;
    }

    private void sortNumber(List<Integer> listNumbers, OnSortListener onSortListener) {
        boolean isCorrect = true;
        int indexPlus, tmpValue;
        for (int i = 0; i < listNumbers.size(); i++) {
            indexPlus = i + 1;
            if (indexPlus >= listNumbers.size()) break;
            if (listNumbers.get(i) > listNumbers.get(indexPlus)) {
                tmpValue = listNumbers.get(indexPlus);
                listNumbers.set(indexPlus, listNumbers.get(i));
                listNumbers.set(i, tmpValue);
                isCorrect = false;
            }
        }

        if (!isCorrect) {
            sortNumber(listNumbers, onSortListener);
        } else {
            onSortListener.onFinish(listNumbers);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
    }

    public Notification createNotification(Context context) {
        Uri sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.nhac_1);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Thông báo")
                .setContentText("Service đang chạy")
//                .setSound(sound)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "Demo", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        return builder.build();
    }

    interface OnSortListener {
        void onFinish(List<Integer> newList);
    }
}

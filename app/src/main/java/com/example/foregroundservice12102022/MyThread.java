package com.example.foregroundservice12102022;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pphat on 12/14/2022.
 */
public class MyThread extends Thread {
    private Handler handler;
    private List<Integer> listNumbers = new ArrayList<>();

    public MyThread(Handler handler, List<Integer> integers) {
        this.handler = handler;
        listNumbers.addAll(integers);
    }

    @Override
    public void run() {
        super.run();
        sortNumber(listNumbers, new OnSortListener() {
            @Override
            public void onFinish(List<Integer> newListNumber) {
                String textValue = TextUtils.join(", ", newListNumber);
                Message message = new Message();
                message.obj = newListNumber;
                message.what = 1;
                handler.sendMessage(message);
            }
        });
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
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (!isCorrect) {
            sortNumber(listNumbers, onSortListener);
        } else {
            onSortListener.onFinish(listNumbers);
        }
    }

    interface OnSortListener {
        void onFinish(List<Integer> newList);
    }
}

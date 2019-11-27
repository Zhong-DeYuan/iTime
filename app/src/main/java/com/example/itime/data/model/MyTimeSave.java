package com.example.itime.data.model;

import android.os.Handler;
import android.os.Message;

import com.example.itime.data.FileDataSource;

import java.util.Timer;
import java.util.TimerTask;


public class MyTimeSave {
    private Timer mTimer;
    private FileDataSource fileDataSource;
    private ColorInt colorInt;
    private Handler timeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                fileDataSource.saveItem();
                fileDataSource.saveLabels();
                fileDataSource.setColor(colorInt.getColor());
                fileDataSource.saveColor();
            }
        }
    };

    public MyTimeSave(FileDataSource fileDataSource,ColorInt colorInt){
        this.fileDataSource = fileDataSource;
        this.colorInt = colorInt;
        mTimer = new Timer();
    }

    public void startRun() {
        TimerTask mTimerTask = new TimerTask() {
            @Override
            public void run() {
                Message message = Message.obtain();
                message.what = 1;
                timeHandler.sendMessage(message);
            }
        };
        mTimer.schedule(mTimerTask,0,1000);
    }
}

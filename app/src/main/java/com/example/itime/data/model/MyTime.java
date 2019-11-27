package com.example.itime.data.model;

import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class MyTime {
    private long mDay;
    private long mHour;
    private long mMin;
    private long mSecond;
    private Timer mTimer;
    private boolean judge_total;
    private  Calendar calendarNow = Calendar.getInstance();
    private long Timeapart;
    private int period;
    private int State = 3,State1 = 3;
    private Handler timeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                computeTime(judge_total);
                if(State == 0 || State == 2)
                    setText0(textViewShow);
                else if(State == 1)
                    setText1(textViewShow);

                if(State1 == 0 || State1 == 2)
                    setText0(textViewShow1);
                else if(State1 == 1)
                    setText1(textViewShow1);

                if (mSecond == 0 &&  mDay == 0 && mHour == 0 && mMin == 0 ) {
                    if(period == 0) {
                        judge_total = false;
                        if(textView_show != null)
                            textView_show.setText("已经");
                    }
                    else {
                        mDay += period;
                        long TimeTarget = calendarTarget.getTimeInMillis();
                        long seconds = ((long)period) * 24 * 60 * 60 * 1000;
                        TimeTarget += seconds;
                        calendarTarget.setTimeInMillis(TimeTarget);
                        if(textViewYear != null){
                            if(State == 2)
                                textViewYear.setText(calendarTarget.get(Calendar.YEAR)+"年"
                                        +(calendarTarget.get(Calendar.MONTH)+1)+"月"+calendarTarget.get(Calendar.DAY_OF_MONTH)+"日 周"+Week(calendarTarget.get(Calendar.DAY_OF_WEEK)));
                            else
                                textViewYear.setText(calendarTarget.get(Calendar.YEAR)+"年"+(calendarTarget.get(Calendar.MONTH)+1)+"月"+calendarTarget.get(Calendar.DAY_OF_MONTH)+"日");
                        }
                        if(textViewYear1 != null){
                            if(State1 == 2)
                                textViewYear1.setText(calendarTarget.get(Calendar.YEAR)+"年"
                                        +(calendarTarget.get(Calendar.MONTH)+1)+"月"+calendarTarget.get(Calendar.DAY_OF_MONTH)+"日 周"+Week(calendarTarget.get(Calendar.DAY_OF_WEEK)));
                            else
                                textViewYear1.setText(calendarTarget.get(Calendar.YEAR)+"年"+(calendarTarget.get(Calendar.MONTH)+1)+"月"+calendarTarget.get(Calendar.DAY_OF_MONTH)+"日");
                        }
                    }
                }
            }
        }
    };

    public TextView textViewShow,textViewShow1;
    public Calendar calendarTarget;
    public  TextView textViewYear,textViewYear1;
    public TextView textView_show;

    public void setTextView_show(TextView textView_show) {
        this.textView_show = textView_show;
        Judge();
    }

    public MyTime(){}

    public void setMyTime(Calendar calendarTarget, int period) {
        this.calendarTarget = calendarTarget;
        this.period = period;

        calendarNow = Calendar.getInstance();

        long TimeNow = calendarNow.getTimeInMillis();
        long TimeTarget = this.calendarTarget.getTimeInMillis();

        if(TimeNow <= TimeTarget) {
            judge_total = true;
            Timeapart = TimeTarget - TimeNow;
        }
        else{
            judge_total = false;
            Timeapart = TimeNow - TimeTarget;
        }

        Timeapart /= 1000;

        mSecond = Timeapart % 60;
        mMin = (Timeapart / 60) % 60;
        mHour = (Timeapart / 3600) %24;
        mDay = (Timeapart / 86400);

        if(period != 0 && (!judge_total)){
            int i = 0;
            long Period = period*24*60*60*1000;
            while(Timeapart >= Period){
                Timeapart -= Period;
                i++;
            }
            i++;
            mDay = (i*period)-mDay;
            TimeTarget += (i*Period);
            calendarTarget.setTimeInMillis(TimeTarget);
            judge_total = true;
        }
        if(textViewYear != null){
            if(State == 2)
                textViewYear.setText(calendarTarget.get(Calendar.YEAR)+"年"
                        +(calendarTarget.get(Calendar.MONTH)+1)+"月"+calendarTarget.get(Calendar.DAY_OF_MONTH)+"日 周"+Week(calendarTarget.get(Calendar.DAY_OF_WEEK)));
            else
                textViewYear.setText(calendarTarget.get(Calendar.YEAR)+"年"+(calendarTarget.get(Calendar.MONTH)+1)+"月"+calendarTarget.get(Calendar.DAY_OF_MONTH)+"日");
        }
        if(textViewYear1 != null){
            if(State1 == 2)
                textViewYear1.setText(calendarTarget.get(Calendar.YEAR)+"年"
                        +(calendarTarget.get(Calendar.MONTH)+1)+"月"+calendarTarget.get(Calendar.DAY_OF_MONTH)+"日 周"+Week(calendarTarget.get(Calendar.DAY_OF_WEEK)));
            else
                textViewYear1.setText(calendarTarget.get(Calendar.YEAR)+"年"+(calendarTarget.get(Calendar.MONTH)+1)+"月"+calendarTarget.get(Calendar.DAY_OF_MONTH)+"日");
        }
        mTimer = new Timer();
    }

    public void setTextView1(TextView textView_1,int State_1){
        textViewShow = textView_1;
        State = State_1;
    }

    public void setTextView2(TextView textView_2,int State_2){
            textViewShow1 = textView_2;
            State1 = State_2;
    }

    public void setTextViewYear(TextView textViewYear){
        this.textViewYear = textViewYear;
    }

    public void setTextViewYear1(TextView textViewYear1){
        this.textViewYear1 = textViewYear1;
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

    private void computeTime(boolean judge) {
        if(judge) {
            mSecond--;
            if (mSecond < 0) {
                mMin--;
                mSecond = 59;
                if (mMin < 0) {
                    mMin = 59;
                    mHour--;
                    if (mHour < 0) {
                        mHour = 23;
                        mDay--;
                        if (mDay < 0) {
                            mDay = 0;
                            mHour = 0;
                            mMin = 0;
                            mSecond = 0;
                        }
                    }
                }
            }
        }
        else{
            mSecond++;
            if (mSecond >59) {
                mMin++;
                mSecond = 0;
                if (mMin >59) {
                    mMin = 0;
                    mHour++;
                    if (mHour >23) {
                        mHour = 0;
                        mDay++;
                    }
                }
            }
        }
    }

    private void Judge(){
        if(textView_show!=null){
            if(judge_total)
                textView_show.setText("只剩");
            else
                textView_show.setText("已经");
        }
    }

    public void Stop(){
        if(mTimer != null) {
            mTimer.cancel();
        }
    }

    private void setText0(TextView textViewSet){
        if (mDay == 0) {
            if (mHour == 0) {
                if (mMin == 0)
                    textViewSet.setText(mSecond + "秒");
                else
                    textViewSet.setText(mMin + "分钟" + mSecond + "秒");
            } else
                textViewSet.setText(mHour + "小时" + mMin + "分钟" + mSecond + "秒");
        } else
            textViewSet.setText(mDay + "天" + mHour + "小时" + mMin + "分钟" + mSecond + "秒");
    }

    private void setText1(TextView textViewSet){
        if (mDay == 0) {
            if (mHour == 0) {
                if (mMin == 0)
                    textViewSet.setText(mSecond + "秒");
                else
                    textViewSet.setText(mMin + "分钟");
            } else
                textViewSet.setText(mHour + "小时");
        } else
            textViewSet.setText(mDay + "天");
    }

    private String Week(int week){
        if(week == 1){
            return "日";
        }
        else if(week == 2){
            return "一";
        }
        else if(week == 3){
            return "二";
        }
        else if(week == 4){
            return "三";
        }
        else if(week == 5){
            return "四";
        }
        else if(week == 6){
            return "五";
        }
        else if(week == 7){
            return "六";
        }
        return "";
    }
}

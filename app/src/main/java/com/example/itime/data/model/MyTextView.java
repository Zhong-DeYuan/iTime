package com.example.itime.data.model;

import android.widget.TextView;

import java.io.Serializable;

public class MyTextView implements Serializable {
    private TextView textView;
    private int state;

    public MyTextView(TextView textView, int state) {
        this.textView = textView;
        this.state = state;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void changeState(){
        state = (state+1)%2;
    }
}

package com.example.itime.data.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

public class MyItem implements Serializable {
    private String title;
    private String description;
    private Calendar calendar;
    private int preiod;
    private byte[] pictureResource;
    private ArrayList<String> labels;

    public MyItem(String title, String description, Calendar calendar, int preiod, Bitmap pictureResource, ArrayList<String> labels) {
        this.title = title;
        this.description = description;
        this.calendar = calendar;
        this.preiod = preiod;
        setBytes(pictureResource);
        this.labels = labels;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public int getPreiod() {
        return preiod;
    }

    public void setPreiod(int preiod) {
        this.preiod = preiod;
    }

    public byte[] getPictureResource() {
        return pictureResource;
    }

    public void setPictureResource(byte[] pictureResource) {
        this.pictureResource = pictureResource;
    }

    public ArrayList<String> getLabels() {
        return labels;
    }

    public void setLabels(ArrayList<String> labels) {
        this.labels = labels;
    }

    public void setBytes(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, baos);
        pictureResource = baos.toByteArray();
    }

    public  Bitmap getBitmap(){
        return BitmapFactory.decodeByteArray(pictureResource, 0, pictureResource.length);
    }
}

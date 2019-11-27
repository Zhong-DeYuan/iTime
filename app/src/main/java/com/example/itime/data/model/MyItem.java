package com.example.itime.data.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

public class MyItem implements Serializable {
    private String title;
    private String description;
    private Calendar calendar;
    private int preiod;
    private int pictureResource;
    private ArrayList<String> labels;

    public MyItem(String title, String description, Calendar calendar, int preiod, int pictureResource, ArrayList<String> labels) {
        this.title = title;
        this.description = description;
        this.calendar = calendar;
        this.preiod = preiod;
        this.pictureResource = pictureResource;
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

    public int getPictureResource() {
        return pictureResource;
    }

    public void setPictureResource(int pictureResource) {
        this.pictureResource = pictureResource;
    }

    public ArrayList<String> getLabels() {
        return labels;
    }

    public void setLabels(ArrayList<String> labels) {
        this.labels = labels;
    }
}

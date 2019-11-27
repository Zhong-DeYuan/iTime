package com.example.itime.data.model;


public class Item1 {
    private int pictureId;
    private String title;
    private String description;

    public Item1(int pictureId, String title, String description) {
        this.pictureId = pictureId;
        this.title = title;
        this.description = description;
    }

    public int getPictureId() {
        return pictureId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setPictureId(int pictureId) {
        this.pictureId = pictureId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void append(String description_ok) {
        description = description + description_ok;
    }
}

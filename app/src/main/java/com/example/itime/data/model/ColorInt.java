package com.example.itime.data.model;

import java.io.Serializable;

public class ColorInt implements Serializable {
    private int color;
    private int position;

    public ColorInt(int color) {
        this.color = color;
        position = 30;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}

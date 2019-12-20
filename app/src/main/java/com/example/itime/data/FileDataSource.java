package com.example.itime.data;
import android.content.Context;

import com.example.itime.data.model.ColorInt;
import com.example.itime.data.model.MyItem;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by jszx on 2019/10/14.
 */

public class FileDataSource {
    private Context context;

    public FileDataSource(Context context) {
        this.context = context;
    }

    private ArrayList<MyItem> MyItems=new ArrayList<MyItem>();
    private ArrayList<String> MyLabels=new ArrayList<>();
    private ColorInt color=new ColorInt(0);

    public void setColor(ColorInt color) {
        this.color = color;
    }

    public void setMyItems(ArrayList<MyItem> myItems) {
        MyItems = myItems;
    }

    public void setMyLabels(ArrayList<String> myLabels) {
        MyLabels = myLabels;
    }

    public ArrayList<MyItem> getItems() {
        return MyItems;
    }

    public void saveItem() {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    context.openFileOutput("MyItems.txt",Context.MODE_PRIVATE)
            );
            outputStream.writeObject(MyItems);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveLabels(){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    context.openFileOutput("MyLabels.txt",Context.MODE_PRIVATE)
            );
            outputStream.writeObject(MyLabels);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveColor(){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    context.openFileOutput("Color.txt",Context.MODE_PRIVATE)
            );
            outputStream.writeObject(color);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<MyItem> loadItem() {
        try{
            ObjectInputStream inputStream = new ObjectInputStream(
                    context.openFileInput("MyItems.txt")
            );
            MyItems = (ArrayList<MyItem>) inputStream.readObject();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return MyItems;
    }

    public ArrayList<String> loadLabels() {
        try{
            ObjectInputStream inputStream = new ObjectInputStream(
                    context.openFileInput("MyLabels.txt")
            );
            MyLabels = (ArrayList<String>) inputStream.readObject();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return MyLabels;
    }

    public ColorInt loadColor() {
        try{
            ObjectInputStream inputStream = new ObjectInputStream(
                    context.openFileInput("Color.txt")
            );
            color = (ColorInt) inputStream.readObject();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return color;
    }
}
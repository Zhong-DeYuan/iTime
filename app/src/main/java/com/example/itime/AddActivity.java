package com.example.itime;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.example.itime.data.model.Item1;
import com.example.itime.data.model.MyItem;
import com.example.itime.data.model.MyTextView;

import static android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT;

public class AddActivity extends AppCompatActivity {

    private ImageButton buttonOk, buttonCancel;
    private ArrayList<Item1> theItem1s;
    private Item1sArrayAdapter theAdapter;
    private ListView listViewShow;
    private ArrayList<String> transerLabels;
    private ArrayList<MyTextView> Label;
    private Drawable drawableLeft;
    private EditText editTextTitle,editTextDescription;
    private int MyPeriod = 0,PictureResource;
    private MyItem transerItem;
    private int State;
    private View temp;

    public MyItem thisItem;
    public Calendar calendar= Calendar.getInstance(Locale.CHINA);
    public Calendar calendar_new = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Intent intent = getIntent();
        transerLabels = intent.getStringArrayListExtra("transerLabels");
        State = intent.getIntExtra("theEditPosition",-1);
        if(State!=-1){
            Bundle bundle = intent.getExtras();
            transerItem = (MyItem) bundle.getSerializable("Edit_Item");
        }
        int Color = intent.getIntExtra("Color",0);

        InitLabels();

        int random = (int) (Math.random() * 10 + 1);
        String name = "a" + random;
        Context ctx = getBaseContext();
        PictureResource = getResources().getIdentifier(name, "drawable", ctx.getPackageName());

        drawableLeft = getResources().getDrawable(R.mipmap.ic_launcher_check);
        drawableLeft.setBounds(0, 0, drawableLeft.getMinimumWidth(), drawableLeft.getMinimumHeight());

        buttonOk = this.findViewById(R.id.button_label_ok);
        buttonCancel = this.findViewById(R.id.button_cancel);
        listViewShow = this.findViewById(R.id.listView_chooce);
        editTextTitle = this.findViewById(R.id.editText_title);
        editTextDescription = this.findViewById(R.id.editText_description);
        temp=findViewById(R.id.relativeLayout_add);
        if(Color!=0)
            temp.setBackgroundColor(Color);
        theItem1s = new ArrayList<Item1>();
        Init();
        theAdapter = new Item1sArrayAdapter(this,R.layout.add_item,theItem1s);

        listViewShow.setAdapter(theAdapter);

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextTitle.getText().toString().isEmpty())
                    Toast.makeText(AddActivity.this, "主题不能为空！", Toast.LENGTH_SHORT).show();
                else{
                    String Title = editTextTitle.getText().toString().trim();
                    String Description = editTextDescription.getText().toString().trim();
                    ArrayList<String> TheLabel = new ArrayList<>();
                    int k = 0;
                    Intent intent = new Intent();
                    intent.putExtra("return_Labels",transerLabels);
                    while(k<Label.size()){
                        if(Label.get(k).getState() == 1)
                            TheLabel.add(Label.get(k).getTextView().getText().toString().trim());
                        k++;
                    }
                    if(State==-1) {
                        thisItem = new MyItem(Title, Description, calendar_new, MyPeriod, PictureResource, TheLabel);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("A_Item", thisItem);
                        intent.putExtras(bundle);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                    else{
                        transerItem.setTitle(Title);
                        transerItem.setDescription(Description);
                        transerItem.setCalendar(calendar_new);
                        transerItem.setPreiod(MyPeriod);
                        transerItem.setPictureResource(PictureResource);
                        transerItem.setLabels(TheLabel);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Return_Item", transerItem);
                        intent.putExtras(bundle);
                        intent.putExtra("Return_position",State);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
            }
        });
        listViewShow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    showSelectItem(AddActivity.this, THEME_DEVICE_DEFAULT_LIGHT,theAdapter, calendar,calendar_new);
                }
                else if(position == 1){
                    showListDialog(theAdapter);
                }
                else  if(position == 2){
                    showPictureDialog();
                }
                else if(position == 3){
                    showLabelDialog();
                }
            }
        });

        listViewShow.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0)
                    longTouchCustomDialog(theAdapter);
                return true;
            }
        });

        if(State!=-1)
            Init1();
    }

    private void InitLabels(){
        Label = new ArrayList<>();
        if(State == -1){
            int i = 0;
            while(i<transerLabels.size()){
                TextView textViewok = new TextView(AddActivity.this);
                textViewok.setText(transerLabels.get(i));
                textViewok.setTextSize(30);
                Label.add(new MyTextView(textViewok,0));
                i++;
            }
        }
        else{
            int i = 0;
            while(i<transerLabels.size()){
                TextView textViewok = new TextView(AddActivity.this);
                textViewok.setText(transerLabels.get(i));
                textViewok.setTextSize(30);
                int j = 0,State_mid = 0;
                while(j<transerItem.getLabels().size()){
                    if(transerLabels.get(i).equals(transerItem.getLabels().get(j))){
                        State_mid = 1;
                        break;
                    }
                    j++;
                }
                Label.add(new MyTextView(textViewok,State_mid));
                i++;
            }
        }
    }

    private void Init1() {
        editTextTitle.setText(transerItem.getTitle());
        editTextDescription.setText(transerItem.getDescription());
        PictureResource = transerItem.getPictureResource();
        temp.setBackgroundResource(PictureResource);
        theItem1s.get(0).setDescription(transerItem.getCalendar().get(Calendar.YEAR)+"年"+
                (transerItem.getCalendar().get(Calendar.MONTH)+1)+"月"+transerItem.getCalendar().get(Calendar.DAY_OF_MONTH)+"日");
        theItem1s.get(1).setDescription(transerItem.getPreiod()+"天");
        String OK = "";
        int j = 0;
        while ((j<transerItem.getLabels().size())){
            if(OK.isEmpty())
                OK = "已选："+ transerItem.getLabels().get(j).trim();
            else
                OK = OK + "," + transerItem.getLabels().get(j).trim();
            j++;
        }
        theItem1s.get(3).setDescription(OK);
    }

    private void Init() {
        theItem1s.add(new Item1(R.mipmap.ic_launcher_time, "日期", "长按使用日期计算器"));
        theItem1s.add(new Item1(R.mipmap.ic_launcher_refresh, "重复设置", "无"));
        theItem1s.add(new Item1(R.mipmap.ic_launcher_picture, "图片", ""));
        theItem1s.add(new Item1(R.mipmap.ic_launcher_label, "添加标签", ""));
    }

    private static void showSelectItem(final Activity activity, final int themeResId,
                                       final Item1sArrayAdapter TheAdapter, final Calendar calendar,final Calendar calendar_new){
        new DatePickerDialog(activity, themeResId, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar_new.set(year,monthOfYear,dayOfMonth);
                String Year = year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日";
                TheAdapter.changeDescription(0,Year,true);
                TheAdapter.notifyDataSetChanged();
                new TimePickerDialog( activity,themeResId,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                calendar_new.set(Calendar.HOUR_OF_DAY,hourOfDay);
                                calendar_new.set(Calendar.MINUTE,minute);
                                String Time_new = " " + hourOfDay + ":" + minute;
                                TheAdapter.changeDescription(0,Time_new,false);
                                TheAdapter.notifyDataSetChanged();
                            }
                        }
                        , calendar.get(Calendar.HOUR_OF_DAY)
                        , calendar.get(Calendar.MINUTE)
                        // true表示采用24小时制
                        ,true).show();
            }
        }
                , calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showListDialog(final Item1sArrayAdapter ShowAdapter){
        final String[] items = {"每周","每月" ,"每年","自定义","无"};
        AlertDialog.Builder dialog3 = new AlertDialog.Builder (this).setTitle ("周期")
                .setItems (items, new DialogInterface.OnClickListener () {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //如果点击小王的话那么which保存的就是0
                        if(which != 3) {
                            ShowAdapter.changeDescription(1, items[which], true);
                            ShowAdapter.notifyDataSetChanged();
                            if(which == 0)
                                MyPeriod = 7;
                            else if(which == 1)
                                MyPeriod = 30;
                            else if(which == 2)
                                MyPeriod = 365;
                            else if(which == 4)
                                MyPeriod = 0;
                        }
                        else{
                            showCustomDialog(ShowAdapter);
                        }
                    }
                });
        dialog3.show ();
    }

    private void showCustomDialog(final Item1sArrayAdapter ShowAdapter){
        final Dialog dialog;
        LayoutInflater inflater=LayoutInflater.from( this );
        final View myview=inflater.inflate(R.layout.dialog_layout,null);//引用自定义布局
        AlertDialog.Builder builder=new AlertDialog.Builder( this );
        builder.setView( myview );
        dialog=builder.create();//创建对话框
        dialog.show();//显示对话框
        myview.findViewById(R.id.button_cycle_quit).setOnClickListener(new View.OnClickListener() {//获取布局里面按钮
            @Override
            public void onClick(View v) {
                dialog.dismiss();//点击按钮对话框消失
            }
        } );
        myview.findViewById(R.id.button_cycle_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText_number = myview.findViewById(R.id.editText_cycle);
                String number = editText_number.getText().toString().trim();
                MyPeriod = Integer.parseInt(number);
                ShowAdapter.changeDescription(1,number+"天",true);
                ShowAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
    }

    private void longTouchCustomDialog(final Item1sArrayAdapter ShowAdapter){
        final Dialog dialog;
        LayoutInflater inflater=LayoutInflater.from( this );
        final View myview=inflater.inflate(R.layout.long_touch_layout,null);//引用自定义布局
        AlertDialog.Builder builder=new AlertDialog.Builder( this );
        builder.setView( myview );
        dialog=builder.create();//创建对话框
        dialog.show();//显示对话框
        myview.findViewById(R.id.button_long_quit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        final EditText editTextAfter = myview.findViewById(R.id.editText_long_after);
        final EditText editTextBefore = myview.findViewById(R.id.editText_long_before);
        final TextView textViewAfter = myview.findViewById(R.id.textView_after_time);
        final TextView textViewBefore = myview.findViewById(R.id.textView_before_time);
        final Calendar calendarNow = Calendar.getInstance();
        textViewAfter.setText(calendarNow.get(Calendar.YEAR)+"年"+calendarNow.get(Calendar.MONTH)+"月"+calendarNow.get(Calendar.DAY_OF_MONTH)+"日");
        textViewBefore.setText(calendarNow.get(Calendar.YEAR)+"年"+calendarNow.get(Calendar.MONTH)+"月"+calendarNow.get(Calendar.DAY_OF_MONTH)+"日");

        editTextAfter.addTextChangedListener(new TextWatcher() {
            String number_before;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                number_before = editTextAfter.getText().toString().trim();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editTextAfter.getText().toString().length() < 5) {
                    long timeNower = calendarNow.getTimeInMillis();
                    long number_change;
                    String content = editTextAfter.getText().toString().trim();
                    if (content.isEmpty())
                        number_change = 0;
                    else
                        number_change = Long.parseLong(content);
                    timeNower += (number_change * 1000 * 60 * 60 * 24);
                    Calendar calendarChange = Calendar.getInstance();
                    calendarChange.setTimeInMillis(timeNower);
                    textViewAfter.setText(calendarChange.get(Calendar.YEAR) + "年" + calendarChange.get(Calendar.MONTH) + "月" + calendarChange.get(Calendar.DAY_OF_MONTH) + "日");
                }
                else
                    editTextAfter.setText(number_before);
            }
        });

        editTextBefore.addTextChangedListener(new TextWatcher() {
            String number_before;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                number_before = editTextBefore.getText().toString().trim();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editTextBefore.getText().toString().length() < 5) {
                    long timeNower = calendarNow.getTimeInMillis();
                    long number_change;
                    String content = editTextBefore.getText().toString().trim();
                    if (content.isEmpty())
                        number_change = 0;
                    else
                        number_change = Long.parseLong(content);
                    timeNower = timeNower - (number_change * 1000 * 60 * 60 * 24);
                    Calendar calendarChange = Calendar.getInstance();
                    calendarChange.setTimeInMillis(timeNower);
                    textViewBefore.setText(calendarChange.get(Calendar.YEAR) + "年" + calendarChange.get(Calendar.MONTH) + "月" + calendarChange.get(Calendar.DAY_OF_MONTH) + "日");
                }
                else
                    editTextBefore.setText(number_before);
            }
        });

        myview.findViewById(R.id.button_after).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextAfter.getText().toString().isEmpty())
                    dialog.dismiss();
                long Add = Long.parseLong(editTextAfter.getText().toString().trim())*1000*24*60*60;
                Add += calendarNow.getTimeInMillis();
                calendar_new.setTimeInMillis(Add);
                String Show = calendar_new.get(Calendar.YEAR)+"年"+calendar_new.get(Calendar.MONTH)+"月"+calendar_new.get(Calendar.DAY_OF_MONTH)+"日";
                ShowAdapter.changeDescription(0,Show,true);
                ShowAdapter.notifyDataSetChanged();
                new TimePickerDialog( AddActivity.this,THEME_DEVICE_DEFAULT_LIGHT,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                calendar_new.set(Calendar.HOUR_OF_DAY,hourOfDay);
                                calendar_new.set(Calendar.MINUTE,minute);
                                String Time_new = " " + hourOfDay + ":" + minute;
                                ShowAdapter.changeDescription(0,Time_new,false);
                                ShowAdapter.notifyDataSetChanged();
                            }
                        }
                        , calendar.get(Calendar.HOUR_OF_DAY)
                        , calendar.get(Calendar.MINUTE)
                        // true表示采用24小时制
                        ,true).show();
                dialog.dismiss();
            }
        });
        myview.findViewById(R.id.button_before).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextBefore.getText().toString().isEmpty())
                    dialog.dismiss();
                long Sub = Long.parseLong(editTextBefore.getText().toString().trim())*1000*24*60*60;
                Sub = calendarNow.getTimeInMillis() - Sub;
                calendar_new.setTimeInMillis(Sub);
                String Show = calendar_new.get(Calendar.YEAR)+"年"+calendar_new.get(Calendar.MONTH)+"月"+calendar_new.get(Calendar.DAY_OF_MONTH)+"日";
                ShowAdapter.changeDescription(0,Show,true);
                ShowAdapter.notifyDataSetChanged();
                new TimePickerDialog( AddActivity.this,THEME_DEVICE_DEFAULT_LIGHT,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                calendar_new.set(Calendar.HOUR_OF_DAY,hourOfDay);
                                calendar_new.set(Calendar.MINUTE,minute);
                                String Time_new = " " + hourOfDay + ":" + minute;
                                ShowAdapter.changeDescription(0,Time_new,false);
                                ShowAdapter.notifyDataSetChanged();
                            }
                        }
                        , calendar.get(Calendar.HOUR_OF_DAY)
                        , calendar.get(Calendar.MINUTE)
                        // true表示采用24小时制
                        ,true).show();
                dialog.dismiss();
            }
        });
    }

    private void showPictureDialog(){
        final String[] items = {"一","二" ,"三","四","五","六","七","八","九","十"};
        AlertDialog.Builder dialog3 = new AlertDialog.Builder (this).setTitle ("本APP提供以下十张图片")
                .setItems (items, new DialogInterface.OnClickListener () {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int number = which+1;
                        String name = "a"+number;
                        Context ctx=getBaseContext();
                        int resId = getResources().getIdentifier(name, "drawable" , ctx.getPackageName());
                        PictureResource = resId;
                        temp.setBackgroundResource(resId);
                    }
                });
        dialog3.show ();
    }

    private void showLabelDialog(){
        final Dialog dialog;
        LayoutInflater inflater=LayoutInflater.from( this );
        final View myview=inflater.inflate(R.layout.label_layout,null);//引用自定义布局
        final LinearLayout temp= myview.findViewById(R.id.LinearLayout_label);
        int i =0;
        while(i<Label.size()){

            final MyTextView myTextView = Label.get(i);
            final TextView textViewShow = new TextView(this);
            textViewShow.setText(myTextView.getTextView().getText().toString().trim());
            textViewShow.setTextSize(30);
            textViewShow.setBackgroundResource(R.drawable.textview_circle);
            if(myTextView.getState() == 0){
                textViewShow.setCompoundDrawables(null,null,null,null);
            }
            else if(myTextView.getState() == 1){
                textViewShow.setCompoundDrawables(drawableLeft, null, null, null);
            }
            textViewShow.setClickable(true);
            textViewShow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myTextView.changeState();
                    if(myTextView.getState() == 0){
                        textViewShow.setCompoundDrawables(null,null,null,null);
                    }
                    else if(myTextView.getState() == 1){
                        textViewShow.setCompoundDrawables(drawableLeft, null, null, null);
                    }
                }
            });
            temp.addView(textViewShow);

            TextView space = new TextView(this);
            space.setText(" ");
            space.setTextSize(30);
            temp.addView(space);
            i++;
        }
        AlertDialog.Builder builder=new AlertDialog.Builder( this );
        builder.setView( myview );
        dialog=builder.create();//创建对话框
        dialog.show();//显示对话框
        myview.findViewById(R.id.button_label_quit).setOnClickListener(new View.OnClickListener() {//获取布局里面按钮
            @Override
            public void onClick(View v) {
                dialog.dismiss();//点击按钮对话框消失
            }
        } );
        myview.findViewById(R.id.button_label_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String OK = "";
                int j = 0;
                while ((j<Label.size())){
                    if(Label.get(j).getState() == 1){
                        if(OK.isEmpty())
                            OK = Label.get(j).getTextView().getText().toString().trim();
                        else
                            OK = OK + "," + Label.get(j).getTextView().getText().toString().trim();
                    }
                    j++;
                }
                if(!OK.isEmpty())
                    OK = "已选："+OK;
                theAdapter.changeDescription(3,OK,true);
                theAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        myview.findViewById(R.id.button_addLabel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddLabelDialog();
                dialog.dismiss();
            }
        });
    }

    private void showAddLabelDialog(){
        final Dialog dialog;
        LayoutInflater inflater=LayoutInflater.from( this );
        final View myview=inflater.inflate(R.layout.add_label_layout,null);//引用自定义布局
        AlertDialog.Builder builder=new AlertDialog.Builder( this );
        builder.setView( myview );
        dialog=builder.create();//创建对话框
        dialog.show();//显示对话框
        myview.findViewById(R.id.button_add_label_quit).setOnClickListener(new View.OnClickListener() {//获取布局里面按钮
            @Override
            public void onClick(View v) {
                dialog.dismiss();//点击按钮对话框消失
            }
        } );
        myview.findViewById(R.id.button_add_label_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText_label = myview.findViewById(R.id.editText_add_label);
                String Content = editText_label.getText().toString().trim();
                if(!Content.isEmpty()){
                    boolean JUDGE = true;
                    int n = 0;
                    while(n<transerLabels.size()){
                        if(transerLabels.get(n).equals(Content)){
                            JUDGE = false;
                            break;
                        }
                        n++;
                    }
                    if(JUDGE) {
                        TextView textViewLabel = new TextView(AddActivity.this);
                        textViewLabel.setText(Content);
                        textViewLabel.setBackgroundResource(R.drawable.textview_circle);
                        textViewLabel.setTextSize(30);
                        MyTextView myTextView = new MyTextView(textViewLabel, 0);
                        Label.add(myTextView);
                        transerLabels.add(Content);
                    }
                    else
                        Toast.makeText(AddActivity.this, "iTime：已有该标题", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(AddActivity.this, "iTime：标签名不能为空", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    protected class Item1sArrayAdapter extends ArrayAdapter<Item1>
    {
        private  int resourceId;
        public Item1sArrayAdapter(@NonNull Context context, int resource, @NonNull List<Item1> objects) {
            super(context, resource, objects);
            resourceId = resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater mInflater= LayoutInflater.from(this.getContext());
            View item = mInflater.inflate(this.resourceId,null);

            ImageView img = (ImageView)item.findViewById(R.id.imageView_item1);
            TextView title = (TextView)item.findViewById(R.id.textView_title_1);
            TextView description = (TextView)item.findViewById(R.id.textView_description_1);

            Item1 item1 = this.getItem(position);
            img.setImageResource(item1.getPictureId());
            title.setText(item1.getTitle());
            description.setText(item1.getDescription());

            return item;
        }

        public void changeDescription(int position,String description_ok,boolean judge) {
            Item1 item = this.getItem(position);

            if(judge)
                item.setDescription(description_ok);
            else if(!judge)
                item.append(description_ok);
        }
    }
}

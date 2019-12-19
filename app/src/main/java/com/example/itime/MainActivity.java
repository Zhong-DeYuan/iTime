package com.example.itime;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.itime.data.FileDataSource;
import com.example.itime.data.model.ColorBar;
import com.example.itime.data.model.ColorInt;
import com.example.itime.data.model.Item1;
import com.example.itime.data.model.MyItem;
import com.example.itime.data.model.MyTime;
import com.example.itime.data.model.MyTimeSave;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 901;
    public static final int REQUEST_CODE1 = 902;
    public static ArrayList<MyItem> theItems;
    ArrayList<View> list;
    ViewPager myViewPager;
    ListView listViewShow;
    ItemsArrayAdapter theAdapter;
    ArrayList<String> theLabels;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private ArrayList<MyTime> theTimes;
    private NavigationView navigationView;
    private int ItemID = 1;
    private ColorInt Color = new ColorInt(0);
    private DrawerLayout drawer;
    private FileDataSource fileDataSource;
    private MyTimeSave myTimeSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                if(theItems.size()<5) {
                    Intent intent = new Intent(MainActivity.this, AddActivity.class);
                    intent.putExtra("transerLabels",theLabels);
                    intent.putExtra("Color",Color.getColor());
                    startActivityForResult(intent, REQUEST_CODE);
                }
                else
                    Toast.makeText(MainActivity.this, "最大不能超过五个哦！", Toast.LENGTH_SHORT).show();
            }
        });
        InitData();
        if(Color.getColor() != 0)
            InitColor();
//       myTimeSave = new MyTimeSave(fileDataSource,Color);
//       myTimeSave.startRun();

        theTimes = new ArrayList<>();
        for(int i=0;i<5;i++){
            theTimes.add(new MyTime());
        }

        theAdapter = new ItemsArrayAdapter(this,R.layout.list_view_item_layout,theItems);

        myViewPager = this.findViewById(R.id.viewPager);
        list = new ArrayList<>();

        listViewShow = this.findViewById(R.id.ListView_Show);
        listViewShow.setAdapter(theAdapter);

        myViewPager.setAdapter(new GuideAdapter());
        myViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        listViewShow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Start(position);
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.nav_app_bar_open_drawer_description, R.string.mr_controller_close_description);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.getMenu().getItem(0).getSubMenu().getItem(0).setOnMenuItemClickListener(new myDeletedialog());
        navigationView.getMenu().getItem(0).getSubMenu().getItem(1).setOnMenuItemClickListener(new MyOnItemListener());
        navigationView.getMenu().getItem(0).getSubMenu().getItem(2).setOnMenuItemClickListener(new ShowAllItemListener());
        navigationView.getMenu().getItem(1).setOnMenuItemClickListener(new ShowColorBarListener());

        Init();
    }

    private void InitColor(){
        toolbar.setBackgroundColor(Color.getColor());
        fab.setBackgroundTintList(ColorStateList.valueOf(Color.getColor()));
    }

    private void Start(int position){
        Intent intent = new Intent(MainActivity.this,DetailActivity.class);
        intent.putExtra("thePosition",position);
        intent.putExtra("transerLabels",theLabels);
        startActivityForResult(intent,REQUEST_CODE1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch(requestCode){
                case REQUEST_CODE:
                    theLabels = data.getStringArrayListExtra("return_Labels");
                    LabelChange();
                    for(int i = 0;i<5;i++){
                        theTimes.get(i).Stop();
                    }
                    listViewShow.setAdapter(theAdapter);
                    Init();
                    break;
            }
        }
        else if(resultCode == RESULT_CANCELED){
            switch (requestCode){
                case REQUEST_CODE1:
                    int POsition = data.getIntExtra("Delete",-1);
                    if(POsition != -1) {
                        theItems.remove(POsition);
                        theAdapter.notifyDataSetChanged();
                        for(int i = 0;i<5;i++){
                            theTimes.get(i).Stop();
                        }
                        listViewShow.setAdapter(theAdapter);
                        Init();
                    }
                    break;
            }
        }
        else if(resultCode == RESULT_FIRST_USER){
            switch (requestCode){
                case REQUEST_CODE1:
                    theLabels = data.getStringArrayListExtra("return_Labels");
                    LabelChange();
                    int returnPosition = data.getIntExtra("Change_Position",-1);
                    for(int i = 0;i<5;i++){
                        theTimes.get(i).Stop();
                    }
                    listViewShow.setAdapter(theAdapter);
                    Init();
                    break;
            }
        }
    }

    private void Init(){
        list.clear();
        int i = 0;
        while(i<theItems.size()){
            final View view = LayoutInflater.from(getApplication()).inflate(R.layout.view_pager_image_layout, null);
            final int j = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Start(list.indexOf(view));
                }
            });
            TextView textViewTitle = view.findViewById(R.id.textView_show_title);
            textViewTitle.setText(theItems.get(i).getTitle());
            TextView textViewTime = view.findViewById(R.id.textView_show_time);
            Calendar calendar = theItems.get(i).getCalendar();
            textViewTime.setText(calendar.get(Calendar.YEAR)+"年"+(calendar.get(Calendar.MONTH)+1)+"月"+calendar.get(Calendar.DAY_OF_MONTH)+"日");
            TextView textViewHour = view.findViewById(R.id.textView_show_hour);
            MyTime myTime = theTimes.get(i);
            myTime.setMyTime(calendar,theItems.get(i).getPreiod());
            myTime.startRun();
            myTime.setTextView2(textViewHour,0);
            myTime.setTextViewYear(textViewTime);
            ImageView imageView = view.findViewById(R.id.imageView_View_Pager);
            imageView.setImageBitmap(theItems.get(i).getBitmap());
            list.add(view);
            i++;
        }
        myViewPager.setAdapter(new GuideAdapter());
        theAdapter.notifyDataSetChanged();

        int m = navigationView.getMenu().getItem(0).getSubMenu().size()-3;
        while(m<theLabels.size()){
            navigationView.getMenu().getItem(0).getSubMenu().add(ItemID, ItemID,ItemID,theLabels.get(m));
            ItemID++;
            navigationView.getMenu().getItem(0).getSubMenu().getItem(m+3).setOnMenuItemClickListener(new myChoocedialog());
            m++;
        }
    }

    private void Init_change(final ArrayList<MyItem> theInItems){
        for(int j =0;j<5;j++){
            theTimes.get(j).Stop();
        }
        list.clear();
        int i = 0;
        while(i<theInItems.size()){
            final View view = LayoutInflater.from(getApplication()).inflate(R.layout.view_pager_image_layout, null);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Start(list.indexOf(view));
                }
            });
            TextView textViewTitle = view.findViewById(R.id.textView_show_title);
            textViewTitle.setText(theInItems.get(i).getTitle());
            TextView textViewTime = view.findViewById(R.id.textView_show_time);
            Calendar calendar = theInItems.get(i).getCalendar();
            textViewTime.setText(calendar.get(Calendar.YEAR)+"年"+(calendar.get(Calendar.MONTH)+1)+"月"+calendar.get(Calendar.DAY_OF_MONTH)+"日");
            TextView textViewHour = view.findViewById(R.id.textView_show_hour);
            MyTime myTime = theTimes.get(i);
            myTime.setMyTime(calendar,theInItems.get(i).getPreiod());
            myTime.startRun();
            myTime.setTextView2(textViewHour,0);
            myTime.setTextViewYear(textViewTime);
            ImageView imageView = view.findViewById(R.id.imageView_View_Pager);
            imageView.setImageBitmap(theInItems.get(i).getBitmap());
            list.add(view);
            i++;
        }
        myViewPager.setAdapter(new GuideAdapter());
        ItemsArrayAdapter NewAdapter = new ItemsArrayAdapter(this,R.layout.list_view_item_layout,theInItems);
        listViewShow.setAdapter(NewAdapter);
        NewAdapter.notifyDataSetChanged();
    }

    private ArrayList<MyItem> findLabels(String content){
        ArrayList<MyItem> theLabelList = new ArrayList<>();
        int i = 0;
        while(i<theItems.size()){
            int j = 0;
            while(j<theItems.get(i).getLabels().size()){
                if(theItems.get(i).getLabels().get(j).equals(content)){
                    MyItem includeItem = theItems.get(i);
                    theLabelList.add(includeItem);
                    break;
                }
                j++;
            }
            i++;
        }
        return theLabelList;
    }

    private void LabelChange(){
        int add = navigationView.getMenu().getItem(0).getSubMenu().size() - 3;
        while(add < theLabels.size()){
            navigationView.getMenu().getItem(0).getSubMenu().add(ItemID,ItemID,ItemID,theLabels.get(add));
            ItemID++;
            navigationView.getMenu().getItem(0).getSubMenu().getItem(add+3).setOnMenuItemClickListener(new myChoocedialog());
            add++;
        }
    }

    private class GuideAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return list.size();
        }
        @Override
        public boolean isViewFromObject(View view, Object object)
        {
            return view == (View)object;
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(list.get(position));
            return list.get(position);
        }

        @Override
        public void destroyItem(@NonNull View container, int position, @NonNull Object object) {
            ((ViewPager) container).removeView(list.get(position));
        }

        @Override public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
        @Override public void destroyItem(ViewGroup container, int position, Object object) {
            if(list.size() != 0) {
                View imageView = list.get(position % list.size());
                container.removeView(imageView);
            }
            else{
                container.removeViewAt(0);
            }
        }
    }

    protected class ItemsArrayAdapter extends ArrayAdapter<MyItem>
    {
        private  int resourceId;
        private ArrayList<MyItem> theuserItems;
        public ItemsArrayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<MyItem> objects) {
            super(context, resource, objects);
            resourceId=resource;
            theuserItems = (ArrayList<MyItem>)objects;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater mInflater= LayoutInflater.from(this.getContext());
            View item = mInflater.inflate(this.resourceId,null);

            TextView textView1,textView2,textView3,textView4,textView5;
            ImageView imageView1;
            textView1 = item.findViewById(R.id.textView_show_word);
            textView2 = item.findViewById(R.id.textView_showItem_time);
            textView3 = item.findViewById(R.id.textView_listView_title);
            textView4 = item.findViewById(R.id.textView_listView_time);
            textView5 = item.findViewById(R.id.textView_listView_description);
            imageView1 = item.findViewById(R.id.imageView_listView_item);

            textView3.setText(theuserItems.get(position).getTitle());
            Calendar calendar = theuserItems.get(position).getCalendar();
            textView4.setText(calendar.get(Calendar.YEAR)+"年"+(calendar.get(Calendar.MONTH)+1)+"月"+calendar.get(Calendar.DAY_OF_MONTH)+"日");
            textView5.setText(theuserItems.get(position).getDescription());
            imageView1.setImageBitmap(theuserItems.get(position).getBitmap());

            MyTime myTime1 = theTimes.get(position);
            myTime1.setTextView1(textView2,1);
            myTime1.setTextViewYear1(textView4);
            myTime1.setTextView_show(textView1);

            return item;
        }
    }

    private class myChoocedialog implements MenuItem.OnMenuItemClickListener{
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            String contet = item.getTitle().toString();
            Init_change(findLabels(contet));
            return false;
        }
    }

    private class myDeletedialog implements MenuItem.OnMenuItemClickListener{
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            final Dialog dialog;
            final LayoutInflater inflater=LayoutInflater.from(MainActivity.this);
            final View myview=inflater.inflate(R.layout.delete_label,null);//引用自定义布局
            AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
            builder.setView( myview );
            dialog=builder.create();//创建对话框
            dialog.show();//显示对话框
            final EditText input = myview.findViewById(R.id.editText_input_label);
            myview.findViewById(R.id.button_label_delete).setOnClickListener(new View.OnClickListener() {//获取布局里面按钮
                @Override
                public void onClick(View v) {
                    dialog.dismiss();//点击按钮对话框消失
                }
            } );
            LinearLayout linearLayout = myview.findViewById(R.id.LinearLayout_label_delete);
            int o = 3;
            while(o < navigationView.getMenu().getItem(0).getSubMenu().size()){
                Button button = new Button(MainActivity.this);
                String TITLE = navigationView.getMenu().getItem(0).getSubMenu().getItem(o).getTitle().toString();
                button.setText(TITLE);
                button.setTextSize(30);
                linearLayout.addView(button);
                TextView textView = new TextView(MainActivity.this);
                textView.setText(" ");
                textView.setTextSize(30);
                linearLayout.addView(textView);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Button button1 = (Button)v;
                        int p = 3;
                        while(p<navigationView.getMenu().getItem(0).getSubMenu().size()){
                            if(button1.getText().toString().equals(navigationView.getMenu().getItem(0).getSubMenu().getItem(p).getTitle().toString()))
                                break;
                            p++;
                        }
                        navigationView.getMenu().getItem(0).getSubMenu()
                                .removeItem(navigationView.getMenu().getItem(0).getSubMenu().getItem(p).getItemId());
                        p = p -3;
                        theLabels.remove(p);
                        deleteLabelInTime(button1.getText().toString());
                        dialog.dismiss();
                    }
                });
                o++;
            }
            return false;
        }
    }

    private void deleteLabelInTime(String Label){
        int i = 0;
        while(i<theItems.size()){
            int j = 0;
            while(j<theItems.get(i).getLabels().size()){
                if(Label.equals(theItems.get(i).getLabels().get(j))){
                    theItems.get(i).getLabels().remove(j);
                    break;
                }
                j++;
            }
            i++;
        }
    }

    private class MyOnItemListener implements MenuItem.OnMenuItemClickListener{
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            final Dialog dialog;
            final LayoutInflater inflater=LayoutInflater.from(MainActivity.this);
            final View myview=inflater.inflate(R.layout.input_dialog,null);//引用自定义布局
            AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
            builder.setView( myview );
            dialog=builder.create();//创建对话框
            dialog.show();//显示对话框
            final EditText input = myview.findViewById(R.id.editText_input_label);
            myview.findViewById(R.id.button_inputlabel_quit).setOnClickListener(new View.OnClickListener() {//获取布局里面按钮
                @Override
                public void onClick(View v) {
                    dialog.dismiss();//点击按钮对话框消失
                }
            } );
            myview.findViewById(R.id.button_inputlabel_ok).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(input.getText().toString().trim().isEmpty())
                        Toast.makeText(MainActivity.this, "不能输入空标签", Toast.LENGTH_SHORT).show();
                    else{
                        boolean JUDGE = true;
                        String Content = input.getText().toString().trim();
                        for(int q = 0;q<theLabels.size();q++){
                            if(Content.equals(theLabels.get(q))){
                                JUDGE = false;
                                break;
                            }
                        }
                        if(JUDGE) {
                            navigationView.getMenu().getItem(0).getSubMenu().add(ItemID,ItemID,ItemID,input.getText().toString().trim());
                            ItemID++;
                            int newnumber = navigationView.getMenu().getItem(0).getSubMenu().size() - 1;
                            MenuItem menuItem = navigationView.getMenu().getItem(0).getSubMenu().getItem(newnumber);
                            menuItem.setOnMenuItemClickListener(new myChoocedialog());
                            theLabels.add(Content);
                        }
                        else
                            Toast.makeText(MainActivity.this, "该标签已存在", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
            });
            return false;
        }
    }

    private class ShowAllItemListener implements MenuItem.OnMenuItemClickListener{
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            for(int i = 0;i<5;i++){
                theTimes.get(i).Stop();
            }
            listViewShow.setAdapter(theAdapter);
            Init();
            return false;
        }
    }

    private class ShowColorBarListener implements MenuItem.OnMenuItemClickListener{
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            final int colorOld = Color.getColor();
            final Dialog dialog;
            final LayoutInflater inflater=LayoutInflater.from(MainActivity.this);
            final View myview=inflater.inflate(R.layout.color_choose_layout,null);//引用自定义布局
            AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
            builder.setView( myview );
            dialog=builder.create();//创建对话框
            dialog.show();//显示对话框
            final ColorBar colorBar = myview.findViewById(R.id.ColorBar_show);
            colorBar.setOnView(toolbar,fab,Color);
            Button buttonOk = myview.findViewById(R.id.button_color_ok);
            Button buttonQuit = myview.findViewById(R.id.button_color_cancel);

            buttonQuit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Color.setColor(colorOld);
                    InitColor();
                    dialog.dismiss();
                }
            });
            buttonOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            drawer.closeDrawers();
            return false;
        }
    }

    private void InitData() {
        fileDataSource=new FileDataSource(this);
        theItems = fileDataSource.loadItem();
        theLabels = fileDataSource.loadLabels();
        Color.setColor(fileDataSource.loadColor());
    }

    @Override
    protected void onPause() {
        fileDataSource.setColor(Color.getColor());
        fileDataSource.saveItem();
        fileDataSource.saveColor();
        fileDataSource.saveLabels();
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        fileDataSource.setColor(Color.getColor());
        fileDataSource.saveItem();
        fileDataSource.saveColor();
        fileDataSource.saveLabels();
        super.onSaveInstanceState(outState);
    }
}

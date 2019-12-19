package com.example.itime;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.itime.data.model.MyItem;
import com.example.itime.data.model.MyTime;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 903;
    private ImageView imageView1,imageView2,imageView3,imageView4;
    private ImageButton buttonReturn,buttonDelete,buttonEdit;
    private TextView textViewTitle,textViewYear,textViewTime,textViewDescription;
    private int position;
    private ImageView relativeLayout;
    private MyTime myTime = new MyTime();
    private ArrayList<String> transerLabels;
    MyItem myItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        imageView1 = this.findViewById(R.id.imageView_ok1);
        imageView2 = this.findViewById(R.id.imageView_ok2);
        imageView3 = this.findViewById(R.id.imageView_ok3);
        imageView4 = this.findViewById(R.id.imageView_ok4);

        imageView1.setImageResource(R.mipmap.ic_launcher_bend2);
        imageView2.setImageResource(R.mipmap.ic_launcher_calendar);
        imageView3.setImageResource(R.mipmap.ic_launcher_quick_picture);
        imageView4.setImageResource(R.mipmap.ic_launcher_window);

        buttonReturn = findViewById(R.id.button_detail_return);
        buttonDelete = findViewById(R.id.button_delete);
        buttonEdit = findViewById(R.id.button_edit);

        textViewTitle = findViewById(R.id.textView_detail_title);
        textViewYear = findViewById(R.id.textView_detail_year);
        textViewTime = findViewById(R.id.textView_detail_time);
        textViewDescription = findViewById(R.id.textView_detail_description);

        relativeLayout = findViewById(R.id.Relative_Detail_Layout);

        final Intent intent = getIntent();
        transerLabels = intent.getStringArrayListExtra("transerLabels");
        position = intent.getIntExtra("thePosition", -1);
        myItem = MainActivity.theItems.get(position);

        relativeLayout.setImageBitmap(myItem.getBitmap());
        textViewTitle.setText(myItem.getTitle());
        textViewDescription.setText(myItem.getDescription());

        myTime.setTextView1(textViewTime,2);
        myTime.setTextViewYear(textViewYear);

        myTime.setMyTime(myItem.getCalendar(),myItem.getPreiod());
        myTime.startRun();

        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent();
                intent3.putExtra("Change_Position",position);
                intent3.putExtra("return_Labels",transerLabels);
                setResult(RESULT_FIRST_USER,intent3);
                finish();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new android.app.AlertDialog.Builder(DetailActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("询问")
                        .setMessage("您确定要删除吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent1 = new Intent();
                                intent1.putExtra("Delete",position);
                                setResult(RESULT_CANCELED,intent1);
                                finish();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .create().show();
            }
        });

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(DetailActivity.this,AddActivity.class);
                intent2.putExtra("theEditPosition",position);
                intent2.putExtra("transerLabels",transerLabels);
                startActivityForResult(intent2,REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case REQUEST_CODE:
                    int PosiTion = data.getIntExtra("Return_position",-1);
                    if(PosiTion!=-1)
                        myItem = MainActivity.theItems.get(PosiTion);
                    myTime.Stop();
                    myTime.setMyTime(myItem.getCalendar(),myItem.getPreiod());
                    myTime.startRun();
                    relativeLayout.setImageBitmap(myItem.getBitmap());
                    textViewTitle.setText(myItem.getTitle());
                    textViewDescription.setText(myItem.getDescription());
                    transerLabels = data.getStringArrayListExtra("return_Labels");
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent4 = new Intent();
        intent4.putExtra("Change_Position",position);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Change_Item",myItem);
        intent4.putExtras(bundle);
        intent4.putExtra("return_Labels",transerLabels);
        setResult(RESULT_FIRST_USER,intent4);
        super.onBackPressed();
    }
}

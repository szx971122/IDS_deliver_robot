package com.example.myapplication;
//import android.content.ContentValues;
//import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
//import android.widget.TextClock;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.Activity;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class sendActivity extends AppCompatActivity implements OnClickListener {
    private List<String> startr = new ArrayList<String>();//创建一个String类型的数组列表。
    private TextView myTextView1;
    private TextView myTextView2;
    private Spinner mySpinnerStart;
    private Spinner mySpinnerDestin;
    private ArrayAdapter<String> adapter;//创建一个数组适配器
    private EditText input;
    private Button enter;

    private SharedPreferences sharedPref;
    private static final String fileName = "conserve";

    public String room1;
    public String room2;
    public String str;
    public String text;



    @Override
    protected void onCreate(Bundle saveInstanceState1) {


        super.onCreate(saveInstanceState1);
        setContentView(R.layout.bring_interface);

        input=(EditText) findViewById(R.id.input);
        enter=(Button) findViewById(R.id.enter);
        str=input.getText().toString();

        startr.add("101");
        startr.add("102");
        startr.add("103");
        startr.add("104");
        startr.add("105");
        startr.add("106");

        myTextView1 = (TextView) findViewById(R.id.tv1);//作用在创建点击事件时的文本说明。
        myTextView2 = (TextView) findViewById(R.id.tv3);
        mySpinnerStart = (Spinner) findViewById(R.id.roomStart);
        mySpinnerDestin = (Spinner) findViewById(R.id.roomDestin);


        //1.为下拉列表定义一个数组适配器，这个数组适配器就用到里前面定义的list。装的都是list所添加的内容
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, startr);//样式为原安卓里面有的android.R.layout.simple_spinner_item，让这个数组适配器装list内容。
        //2.为适配器设置下拉菜单样式。adapter.setDropDownViewResource
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //3.以上声明完毕后，建立适配器,有关于spinner这个控件的建立。用到myspinnerStart
        mySpinnerStart.setAdapter(adapter);
        mySpinnerDestin.setAdapter(adapter);
        //4.为下拉列表设置各种点击事件，以响应菜单中的文本item被选中了，用setOnItemSelectedListener


        mySpinnerStart.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {//选择item的选择点击监听事件


            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                // 将所选mySpinner 的值带入myTextView 中
                String room1=adapter.getItem(arg2);
                myTextView1.setText("Departure Room Number " + room1 );//文本说明

                SharedPreferences sharedPref = getSharedPreferences(
                        fileName, Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("Room1", adapter.getItem(arg2));
                editor.commit();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                myTextView1.setText("Please choose your departure room number");
            }
        });
        mySpinnerDestin.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {//选择item的选择点击监听事件


            public void onItemSelected(AdapterView<?> arg4, View arg5,
                                       int arg6, long arg7) {

                // TODO Auto-generated method stub
                // 将所选mySpinner 的值带入myTextView 中
                String room2=adapter.getItem(arg6);
                //int num2 = Integer.parseInt(room2);
                myTextView2.setText("Destination Room Number " + room2 );//文本说明

                SharedPreferences sharedPref = getSharedPreferences(
                        fileName, Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("Room2", adapter.getItem(arg6));
                editor.commit();

            }

            public void onNothingSelected(AdapterView<?> arg4) {
                // TODO Auto-generated method stub
                myTextView2.setText("Please choose your destination room number");
            }

        });


    }




    public void onClick(View v){
        SharedPreferences sharedPref = getSharedPreferences(fileName, 0);
        String r1=sharedPref.getString("Room1","100");
        String r2=sharedPref.getString("Room2","001");
       // String rs=r1+r2;
        switch (v.getId()){
            case R.id.enter:
                Toast.makeText(getApplicationContext(), "Object: " + input.getText().toString() + " Deliver From " + r1 +" To " + r2 , Toast.LENGTH_LONG).show();
                break;

            default:
                break;

        }
    }



}


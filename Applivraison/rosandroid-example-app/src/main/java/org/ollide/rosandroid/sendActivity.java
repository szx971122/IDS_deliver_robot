package org.ollide.rosandroid;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class sendActivity extends MainActivity {
//implements View.OnClickListener

    private List<String> startR = new ArrayList<String>();//创建一个String类型的数组列表。
    private Spinner mySpinnerStart;
    private ArrayAdapter<String> adapter;//创建一个数组适配器

    private TextView myTextView1;
    private Button call_robot;

    private SharedPreferences sharedPref;
    private static final String fileName = "conserve";

    public String str;



    @Override
    protected void onCreate(Bundle saveInstanceState1) {
        super.onCreate(saveInstanceState1);
        setContentView(R.layout.send_interface);


        startR.add("101");
        startR.add("102");
        startR.add("103");
        startR.add("104");
        startR.add("105");
        startR.add("114");

        myTextView1 = (TextView) findViewById(R.id.textView3);//作用在创建点击事件时的文本说明。
        mySpinnerStart = (Spinner) findViewById(R.id.roomStart);

        //1.为下拉列表定义一个数组适配器，这个数组适配器就用到里前面定义的list。装的都是list所添加的内容
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, startR);//样式为原安卓里面有的android.R.layout.simple_spinner_item，让这个数组适配器装list内容。
        //2.为适配器设置下拉菜单样式。adapter.setDropDownViewResource
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //3.以上声明完毕后，建立适配器,有关于spinner这个控件的建立。用到myspinnerStart
        mySpinnerStart.setAdapter(adapter);
        //4.为下拉列表设置各种点击事件，以响应菜单中的文本item被选中了，用setOnItemSelectedListener

        mySpinnerStart.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {//选择item的选择点击监听事件


            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                // 将所选mySpinner 的值带入myTextView 中
                String room1 = adapter.getItem(arg2);
                myTextView1.setText("Departure Room Number " + room1);//文本说明

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






            /*  switch (v.getId()){
                case R.id.enter:
                    Toast.makeText(getApplicationContext(), "Object: " + input.getText().toString() + " Deliver From " + r1 +" To " + r2 , Toast.LENGTH_LONG).show();

                    break;

                default:
                    break;

            }*/

        call_robot = (Button) findViewById(R.id.call);
        call_robot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    node.is_goal_confirmed = true;
                    judge=false;
                    String prx = "L";
                    SharedPreferences sharedPref = getSharedPreferences("conserve", 0);
                    r1 = sharedPref.getString("Room1", "100");
                    str = prx + r1;
                    node.strGoalId = str;
                    Intent intent = new Intent(sendActivity.this, sendCall.class);
                    intent.putExtra("delInfor", str);
                    startActivity(intent);


                }
        });
    }
}
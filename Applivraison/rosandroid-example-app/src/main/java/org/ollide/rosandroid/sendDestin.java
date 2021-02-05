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

public class sendDestin extends MainActivity  {

//implements View.OnClickListener


    private List<String> startR = new ArrayList<String>();//创建一个String类型的数组列表。

    private TextView myTextView2;

    private Spinner mySpinnerDestin;
    private ArrayAdapter<String> adapter;//创建一个数组适配器
    private EditText input;

    private Button send_confirm;

    private SharedPreferences sharedPref;
    private static final String fileName = "conserve";
    public String str_input;



    @Override
    protected void onCreate(Bundle saveInstanceState3) {


        super.onCreate(saveInstanceState3);
        setContentView(R.layout.send_destination);

        input = (EditText) findViewById(R.id.input);

        str_input = input.getText().toString();


        startR.add("101");
        startR.add("102");
        startR.add("103");
        startR.add("104");
        startR.add("105");
        startR.add("114");


        myTextView2 = (TextView) findViewById(R.id.textView4);

        mySpinnerDestin = (Spinner) findViewById(R.id.roomDestin);


        //1.为下拉列表定义一个数组适配器，这个数组适配器就用到里前面定义的list。装的都是list所添加的内容
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, startR);//样式为原安卓里面有的android.R.layout.simple_spinner_item，让这个数组适配器装list内容。
        //2.为适配器设置下拉菜单样式。adapter.setDropDownViewResource
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //3.以上声明完毕后，建立适配器,有关于spinner这个控件的建立。用到myspinnerStart

        mySpinnerDestin.setAdapter(adapter);
        //4.为下拉列表设置各种点击事件，以响应菜单中的文本item被选中了，用setOnItemSelectedListener


        mySpinnerDestin.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {//选择item的选择点击监听事件


            public void onItemSelected(AdapterView<?> arg4, View arg5,
                                       int arg6, long arg7) {

                // TODO Auto-generated method stub
                // 将所选mySpinner 的值带入myTextView 中
                String room2 = adapter.getItem(arg6);
                //int num2 = Integer.parseInt(room2);
                myTextView2.setText("Destination Room Number " + room2);//文本说明

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


        send_confirm = (Button) findViewById(R.id.confirm1);
        send_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prx = "L";
                SharedPreferences sharedPref = getSharedPreferences("conserve", 0);
                r2 = sharedPref.getString("Room2", "001");
                node.strGoalId = prx + r2;
                node.is_goal_confirmed = true;

                Toast.makeText(getApplicationContext(), "We start to send your package.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(sendDestin.this, sendWait_Complete.class);
                startActivity(intent);
            }
        });
    }
}

package com.exemple.mysecondapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class CalendarActivity  extends AppCompatActivity {

    private EditText Course;
    private EditText StartTime;
    private EditText EndTime;
    private EditText Class;
    private Button Send;
    private Button Logout;
    private String start;
    private String end;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        Course = (EditText)findViewById(R.id.idCourse);
        StartTime = (EditText)findViewById(R.id.idStartTime);
        EndTime = (EditText)findViewById(R.id.idEndTime);
        Class = (EditText)findViewById(R.id.idClass);
        Send = (Button)findViewById(R.id.idButtonSend);
        Logout = (Button)findViewById(R.id.idButtonLogout);
        start = StartTime.getText().toString();
        end = EndTime.getText().toString();

        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start = StartTime.getText().toString();
                end = EndTime.getText().toString();
                validate(Course.getText().toString(), StartTime.getText().toString(), EndTime.getText().toString(), Class.getText().toString());
            }
        });

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginActivity.sp.edit().putBoolean("logged",false).apply();
                Intent intent = new Intent(CalendarActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void validate(String userCourse, String userStartTime, String userEndTime, String userClass){
        if((userCourse.equals("Info")) && (userClass.equals("I2"))){
            Intent intent = new Intent(CalendarActivity.this, OperationActivity.class);
            Bundle b1 = new Bundle();
            b1.putString("string", start);
            Bundle b2 = new Bundle();
            b2.putString("string", end);
            intent.putExtra("timeb", b1);
            intent.putExtra("time", b2);
            startActivity(intent);
        }else{
            Send.setEnabled(false);
        }
    }
}

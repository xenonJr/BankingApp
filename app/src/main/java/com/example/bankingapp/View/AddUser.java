package com.example.bankingapp.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.telephony.SmsManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bankingapp.Helpers.ExJobService;
import com.example.bankingapp.Helpers.SmsDeliveredReceiver;
import com.example.bankingapp.Helpers.SmsSentReceiver;
import com.example.bankingapp.Model.User;
import com.example.bankingapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import com.twilio.Twilio;
import com.twilio.converter.Promoter;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.net.URI;
import java.math.BigDecimal;


public class AddUser extends AppCompatActivity {

    final Calendar myCalendar= Calendar.getInstance();
    public Button datePicker,sendSMS,addUser;
    public EditText name,addEmail,addPhone;
    public Context mContext;
    public boolean datePicked = false;
    public static final String ACCOUNT_SID = "AC54b2fbbaa152fdb10ee1206c78a190f1";
    public static final String AUTH_TOKEN = "65ca32e7a1f57f81dfd0440dc39a6dce";

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        datePicker = findViewById(R.id.set_bday);
        name = findViewById(R.id.username);
        sendSMS= findViewById(R.id.sendSMS);
        addUser = findViewById(R.id.submit);
        addEmail = findViewById(R.id.email);
        addPhone = findViewById(R.id.phone);
        mContext = getApplicationContext();

        firebaseDatabase = FirebaseDatabase.getInstance("https://banking-app-15a1e-default-rtdb.firebaseio.com/");
        databaseReference = firebaseDatabase.getReference("Users");


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddUser.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        sendSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  sendAPI("09");
                 // twilloSend();
            }
        });

        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateForm()){
                    User user = new User(name.getText().toString(),addEmail.getText().toString(),"88"+addPhone.getText().toString(),datePicker.getText().toString());
                    databaseReference.child("id-"+addPhone.getText().toString()).setValue(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(mContext,"User Added",Toast.LENGTH_SHORT).show();

                                        }
                                    });


                }else{
                    Toast.makeText(mContext,"Fill up all infos",Toast.LENGTH_SHORT);
                }
            }
        });
    }

    private void updateLabel(){
        String myFormat="MM/dd";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        datePicked = true;
        datePicker.setText(dateFormat.format(myCalendar.getTime()));
    }

    void sendAPI(String phone){
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "api_key=2LVwNs752Vns2AzrMq2OvlKVAvt&api_secret=15iqMjRvoNRCmDMQyLi5J8W6fYLzYIHLDCgX3TeV&from=MOVIDER&to=%2B8801793114799&text=hello%20it%20is%20me");
        Request request = new Request.Builder()
                .url("https://api.movider.co/v1/sms")
                .post(body)
                .addHeader("accept", "application/json")
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .build();

        try {
          client.newCall(request).execute();
        }catch (Exception e ){
            Toast.makeText(mContext, "Ex : "+ e, Toast.LENGTH_SHORT).show();
        }
    }

    boolean validateForm(){
        if(name.getText().toString().equals("") || addEmail.getText().toString().equals("") || addPhone.getText().toString().equals("") || datePicked==false){
            return false;
        }
        return true;
    }

    void scheduleJob(View v ){
        ComponentName componentName = new ComponentName(this, ExJobService.class);
        JobInfo jobInfo = new JobInfo.Builder(123,componentName)
                .setPersisted(true)
                .setPeriodic(15*60*1000).build();

        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resCode = scheduler.schedule(jobInfo);
        if(resCode == JobScheduler.RESULT_SUCCESS){
            Log.d("TAG","Job Success");
        }else{
            Log.d("TAG","Job Failed");

        }
    }


    void cancelJob(View v){
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(123);
        Log.d("TAG","Job Canceled");


    }

    void twilloSend(){
         String ACCOUNT_SID = "AC7991c52108e654535bfeef8d9240230b";
         String AUTH_TOKEN = "fbb9516b1cf19638f3fc910031ed4942";
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                        new com.twilio.type.PhoneNumber("+8801793114799"),
                        "MGc10c167b40864e3b8adfee984adff11f",
                        "Hello this is test")
                .create();

        System.out.println(message.getSid());
    }

}
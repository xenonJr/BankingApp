package com.example.bankingapp.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.bankingapp.Adapter.UserAdapter;
import com.example.bankingapp.Model.User;
import com.example.bankingapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HomeActivity extends AppCompatActivity {

    public Button addUser,showUser,sendSms;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addUser=findViewById(R.id.add_user);
        showUser = findViewById(R.id.show_users);
        sendSms = findViewById(R.id.send_sms);

        firebaseDatabase = FirebaseDatabase.getInstance("https://banking-app-15a1e-default-rtdb.firebaseio.com/");
        databaseReference = firebaseDatabase.getReference("Users");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AddUser.class));
            }
        });

        showUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),UserList.class));

            }
        });

        sendSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar;
                String date;
                SimpleDateFormat dateFormat;
                calendar = Calendar.getInstance();
                dateFormat = new SimpleDateFormat("MM/dd");
                date = dateFormat.format(calendar.getTime());

                Log.d("SMS",date);

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            for (DataSnapshot npsnapshot : snapshot.getChildren()) {
                                User l = npsnapshot.getValue(User.class);
                                Log.d("DB", l.name);
                                Log.d("SMS", l.birthday);
                                if (l.getBirthday().equals(date)) {
                                    Log.d("SMS","i am here");
                                    sendAPI(l.phone);
                                }
                            }
                            Toast.makeText(HomeActivity.this, "SMS Sent Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




            }
        });
    }

    void sendAPI(String phone){
        OkHttpClient client = new OkHttpClient();
        String apiKey = "2LpD76zv7KCsRAGQak8AFy8XcHL";
        String secretKey = "z6wGEXRSjvpEAkmh7rvo3gZN4t32LIUMHwXwXIQg";

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "api_key="+apiKey+"&api_secret="+secretKey+"&from=MOVIDER&to=%2B"+phone+"&text=HappyBDAY%20it%20is%20me");
        Request request = new Request.Builder()
                .url("https://api.movider.co/v1/sms")
                .post(body)
                .addHeader("accept", "application/json")
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .build();

        try {
            client.newCall(request).execute();
            Log.d("SMS","Exceuted");
        }catch (Exception e ){
            Toast.makeText(getApplicationContext(), "Ex : "+ e, Toast.LENGTH_SHORT).show();
        }
    }

}
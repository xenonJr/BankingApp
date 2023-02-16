package com.example.bankingapp.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.bankingapp.Adapter.UserAdapter;
import com.example.bankingapp.Model.User;
import com.example.bankingapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserList extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        final UserAdapter[] lectureAdapter = new UserAdapter[1];
        List<User> lectureList;
        lectureList = new ArrayList<>();
        RecyclerView recyclerView;
        recyclerView = findViewById(R.id.fav_rv);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getApplicationContext())
        );

        firebaseDatabase = FirebaseDatabase.getInstance("https://banking-app-15a1e-default-rtdb.firebaseio.com/");
        databaseReference = firebaseDatabase.getReference("Users");


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot npsnapshot : snapshot.getChildren()){
                        User l= npsnapshot.getValue(User.class);
                        lectureList.add(l);
                    }
                    lectureAdapter[0] = new UserAdapter(lectureList,getApplicationContext());
                    recyclerView.setAdapter(lectureAdapter[0]);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    }

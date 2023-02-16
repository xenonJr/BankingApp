package com.example.bankingapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bankingapp.Model.User;
import com.example.bankingapp.R;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    List<User> UserList;
    List<DatabaseReference> databaseReferenceList;
    FragmentActivity fragmentActivity;
    Context context;
    boolean isFav = false;



    public UserAdapter(List<User> UserList, FragmentActivity fragmentActivity) {
        this.UserList = UserList;
        this.fragmentActivity = fragmentActivity;

    }

    public UserAdapter(List<User> UserList, Context context) {
        this.UserList = UserList;
        this.context = context;
    }

    public UserAdapter(List<User> UserList, Context context, List<DatabaseReference> databaseReference){
        this.UserList = UserList;
        this.context = context;
        this.databaseReferenceList = databaseReference;
    }




    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.user_card,parent,false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.Name.setText(UserList.get(position).name);
        holder.phone.setText("Phn: "+UserList.get(position).phone);
        holder.bday.setText("B.Day : "+UserList.get(position).birthday);
    }

    @Override
    public int getItemCount() {
        return UserList.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        TextView Name,phone,bday;


        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.user_name);
            phone = itemView.findViewById(R.id.user_phone);
            bday = itemView.findViewById(R.id.user_bday);

        }
    }
}


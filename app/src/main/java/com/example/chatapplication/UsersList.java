package com.example.chatapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class UsersList extends AppCompatActivity {

    private ListView usersList;
    private UserListAdapter adapter;
    private String user1;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);
        usersList = findViewById(R.id.userslist);
        adapter = new UserListAdapter(this);
        usersList.setAdapter(adapter);
        Intent intent = getIntent();
        user1 = intent.getStringExtra("User");
        getUsers();
        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(UsersList.this, MainActivity.class);
                intent.putExtra("User1", user1);
                User user2 = (User) adapter.getItem(position);
                intent.putExtra("User2", user2.getNamee());
                startActivity(intent);
            }
        });
    }

    private void getUsers() {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    adapter.addItem(new User(data.getKey()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

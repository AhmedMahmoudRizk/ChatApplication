package com.example.chatapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    private FirebaseDatabase database;
    private DatabaseReference myRef1, myRef2, userStatus;
    private MessageAdapter messageAdapter;
    private ListView messagesView;
    private EditText editText;
    private ImageView sendMsg;
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeUI();
        backEnd();
    }

    private void backEnd() {
        initalizeBackend();
        getFromDataBase();
        createAction();
    }

    private void initalizeBackend() {
        database = FirebaseDatabase.getInstance();
        myRef1 = database.getReference("message").child("tester-tester2");
        myRef2 = database.getReference("message").child("tester2-tester");
        Intent intent = getIntent();
        user = intent.getStringExtra("user");
        userStatus =
                database.getReference("users").child(user).child("status");

        userStatus.onDisconnect().setValue("offline");
        userStatus.setValue("online");
        userStatus.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("zzzzzzzz", dataSnapshot.getKey());
                Log.i("zzzzzz", dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void initializeUI() {
        editText = findViewById(R.id.editText);

        messageAdapter = new MessageAdapter(this);
        messagesView = findViewById(R.id.messages_view);
        sendMsg = findViewById(R.id.sendMsg);
        messagesView.setAdapter(messageAdapter);


    }

    private void getFromDataBase() {
        myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (data.exists()) {
                        Map<String, Object> map = (Map<String, Object>) data.getValue();
                        addMessage((String) map.get("user"), (String) map.get("message"), (String) map.get("date"));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void createAction() {

        myRef1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String u = "", m = "", d = "";
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (data.getKey().equals("message"))
                        m = data.getValue().toString();
                    else if (data.getKey().equals("date"))
                        d = data.getValue().toString();
                    else
                        u = data.getValue().toString();
                }
                addMessage(u, m, d);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

    }


    public void sendMessage() {
        String message = editText.getText().toString();
        if (message.length() > 0) {
            DatabaseReference r1, r2;
            r1 = myRef1;
            r2 = myRef2;
            r1 = r1.push();
            r2 = r2.push();
            r1.child("user").setValue(user);
            r1.child("message").setValue(message);
//            r1.child("date").setValue("zzzzz");
            r2.child("user").setValue(user);
            r2.child("message").setValue(message);
//            r2.child("date").setValue("zzzzz");
            //appear msg
            editText.getText().clear();
//            addMessage(user, message);
        }
    }


    /**
     * Functions Belong to frontend
     */
    private void addMessage(String u, String m, String d) {
        final MemberData data = new MemberData(u, getRandomColor());
        boolean belongsToCurrentUser = u.equals(user);
        final Message message = new Message(m, d, data, belongsToCurrentUser);
        messageAdapter.add(message);
        messagesView.setSelection(messagesView.getCount() - 1);
    }

    private String getRandomColor() {
        Random r = new Random();
        StringBuffer sb = new StringBuffer("#");
        while (sb.length() < 7) {
            sb.append(Integer.toHexString(r.nextInt()));
        }
        return sb.toString().substring(0, 7);
    }

}

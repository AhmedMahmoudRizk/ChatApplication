package com.example.chatapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
    private String user1, user2;
    private boolean user2Status, isTyping;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeUI();
        backEnd();
        detectWriting();

    }

    private void initializeUI() {
        editText = findViewById(R.id.editText);

        messageAdapter = new MessageAdapter(this);
        messagesView = findViewById(R.id.messages_view);
        sendMsg = findViewById(R.id.sendMsg);
        messagesView.setAdapter(messageAdapter);

    }

    private void detectWriting() {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString()) && s.toString().trim().length() == 1)
                    isTyping = true;
                else if (s.toString().trim().length() == 0 && isTyping)
                    isTyping = false;
                userStatus.child(user1).child("isTyping").setValue(isTyping);
            }
        });
    }

    private void backEnd() {
        initalizeBackend();
        getFromDatabase();
        createActionBackend();
    }

    private void initalizeBackend() {
        Intent intent = getIntent();
        user1 = intent.getStringExtra("user");
        if (user1.equals("tester"))
            user2 = "tester2";
        else
            user2 = "tester";
        database = FirebaseDatabase.getInstance();
        myRef1 = database.getReference("message").child(user1 + "-" + user2);
        myRef2 = database.getReference("message").child(user2 + "-" + user1);
        userStatus = database.getReference("users");
        // in application is terminated set the values to default
        userStatus.child(user1).child("status").onDisconnect().setValue("offline");
        userStatus.child(user1).child("status").setValue("online");
        userStatus.child(user1).child("isTyping").onDisconnect().setValue("false");
        user2Status = false;
        isTyping = false;
    }

    private void getFromDatabase() {

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

        userStatus.child(user2).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user2Status(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void user2Status(DataSnapshot dataSnapshot) {
        if (dataSnapshot.getKey().equals("status"))
            user2Status = dataSnapshot.getValue().toString().equals("online");
        else if (dataSnapshot.getKey().equals("isTyping")) {
            isTyping = dataSnapshot.getValue().toString().equals("true");
            if (isTyping)
                addMessage(user2, null, "");
            else
                messageAdapter.removeIfTyping();
        }
    }

    private void createActionBackend() {

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
                messageAdapter.removeIfTyping();
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

        userStatus.child(user2).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                user2Status(dataSnapshot);
                //update view status
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
            r1.child("user").setValue(user1);
            r1.child("message").setValue(message);
            r2.child("user").setValue(user1);
            r2.child("message").setValue(message);
            //appear msg
            editText.getText().clear();

//            addMessage(user1, message);
        }
    }


    /**
     * Functions Belong to frontend
     */
    private void addMessage(String u, String m, String d) {
        final MemberData data = new MemberData(u, getRandomColor());
        boolean belongsToCurrentUser = u.equals(user1);
        final Message message = new Message(m, d, data, belongsToCurrentUser, isTyping);
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

package com.example.chatapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    TextView registerUser;
    EditText username, password;
    Button loginButton;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeUI();

        createAction();

    }

    private void initializeUI() {
        registerUser = findViewById(R.id.register);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");

    }


    private void createAction() {
        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUserAccount();
            }
        });
    }

    private void loginUserAccount() {
//        progressBar.setVisibility(View.VISIBLE);

        final String user, pass;
        user = username.getText().toString();
        pass = password.getText().toString();

        if (TextUtils.isEmpty(user)) {
            Toast.makeText(getApplicationContext(), "Please enter User...", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(pass)) {
            Toast.makeText(getApplicationContext(), "Please enter password!", Toast.LENGTH_LONG).show();
            return;
        }
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (data.getKey().equals(user)) {
                        Map<String, Object> map = (Map<String, Object>) data.getValue();
                        if (map.get("password").toString().equals(pass)) {
                            Intent intent = new Intent(Login.this, UsersList.class);
                            intent.putExtra("User", user);
                            startActivity(intent);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
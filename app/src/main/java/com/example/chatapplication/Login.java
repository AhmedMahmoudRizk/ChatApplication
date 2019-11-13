package com.example.chatapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.Map;

import androidx.annotation.NonNull;
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
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {

                        if (!task.isSuccessful()) {
//                            Log.w("zzzzz", task.getResult().getToken());
                            Log.w("zzzzz", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        Log.d("zzzzz", token);
                        Toast.makeText(Login.this, token, Toast.LENGTH_SHORT).show();
                    }
                });
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
                            myRef.child(user).child("status").setValue("online");
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
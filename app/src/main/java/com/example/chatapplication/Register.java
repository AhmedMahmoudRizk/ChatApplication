package com.example.chatapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;


public class Register extends AppCompatActivity {

    EditText username, password;
    Button registerButton;
    TextView login;
    //    private ProgressBar progressBar;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initializeUI();
        createAction();

    }


    private void initializeUI() {
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        registerButton = findViewById(R.id.registerButton);
        login = findViewById(R.id.login);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");
    }

    private void createAction() {
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewUser();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });
    }

    private void registerNewUser() {

//        progressBar.setVisibility(View.VISIBLE);

        final String user, pass;
        user = username.getText().toString();
        pass = password.getText().toString();

        if (TextUtils.isEmpty(user)) {
            Toast.makeText(getApplicationContext(), "Please enter email...", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(pass)) {
            Toast.makeText(getApplicationContext(), "Please enter pass!", Toast.LENGTH_LONG).show();
            return;
        }
        myRef.child(user).child("password").setValue(pass);
        myRef.child(user).child("isTyping").setValue(false);
        myRef.child(user).child("status").setValue("online");
        Intent intent = new Intent(Register.this, UsersList.class);
        intent.putExtra("User", user);
        startActivity(intent);
//        else {
//            final ProgressDialog pd = new ProgressDialog(Register.this);
//            pd.setMessage("Loading...");
//            pd.show();
//
//            String url = "https://chat-application-7efb8.firebaseio.com/";
//
//            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
//                @Override
//                public void onResponse(String s) {
//                    Firebase reference = new Firebase("https://chat-application-7efb8.firebaseio.com/users");
//                    Log.e("taaag", "Enter here---");
//                    if(s.equals("null")) {
//                        reference.child(User).child("password").setValue(pass);
//                        Toast.makeText(Register.this, "registration successful", Toast.LENGTH_LONG).show();
//                    }
//                    else {
//                        try {
//                            JSONObject obj = new JSONObject(s);
//
//                            if (!obj.has(User)) {
//                                reference.child(User).child("password").setValue(pass);
//                                Toast.makeText(Register.this, "registration successful", Toast.LENGTH_LONG).show();
//                            } else {
//                                Toast.makeText(Register.this, "username already exists", Toast.LENGTH_LONG).show();
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    pd.dismiss();
//                }
//
//            },new Response.ErrorListener(){
//                @Override
//                public void onErrorResponse(VolleyError volleyError) {
//                    Log.e("taaag", "Enter here---");
//
//                    System.out.println("taaag--" + volleyError );
//                    pd.dismiss();
//                }
//            });
//
//            RequestQueue rQueue = Volley.newRequestQueue(Register.this);
//            rQueue.add(request);
//        }


//        mAuth.createUserWithEmailAndPassword(User, pass)
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_LONG).show();
////                            progressBar.setVisibility(View.GONE);
//
//                            Intent intent = new Intent(Register.this, Login.class);
//                            startActivity(intent);
//                        } else {
//                            Toast.makeText(getApplicationContext(), "Registration failed! Please try again later", Toast.LENGTH_LONG).show();
////                            progressBar.setVisibility(View.GONE);
//                        }
//                    }
//                });
    }


}

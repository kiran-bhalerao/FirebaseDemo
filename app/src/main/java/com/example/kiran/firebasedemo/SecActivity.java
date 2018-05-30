package com.example.kiran.firebasedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.Timer;

public class SecActivity extends AppCompatActivity {

    EditText from;
    EditText to;
    Button saveButton;
    EditText mesaageText;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sec);
        from = findViewById(R.id.fromText);
        to = findViewById(R.id.toText);
        mesaageText = findViewById(R.id.messageText);
        saveButton = findViewById(R.id.saveButton);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        Date date = new Date();
        String time = date.getHours() + "-" + date.getMinutes() + "-" + date.getSeconds();

        saveButton.setOnClickListener((v) -> {
            new Thread(() -> {
                DatabaseReference userRef = firebaseDatabase.getReference("Messages").push();
                userRef.child("from").setValue(from.getText().toString());
                userRef.child("to").setValue(to.getText().toString());
                userRef.child("message").setValue(mesaageText.getText().toString());
                userRef.child("time").setValue(time);
                userRef.child("email").setValue(firebaseAuth.getCurrentUser().getEmail());
            }).start();
        });
    }
}

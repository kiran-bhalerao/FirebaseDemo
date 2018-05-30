package com.example.kiran.firebasedemo;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class SecActivity extends AppCompatActivity {

    EditText to;
    Button saveButton;
    EditText mesaageText;
    Button logoutButton;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    FirebaseAuth firebaseAuth;
    Handler handler;
    DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sec);
        to = findViewById(R.id.toText);
        mesaageText = findViewById(R.id.messageText);
        saveButton = findViewById(R.id.saveButton);
        logoutButton = findViewById(R.id.logoutButton);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
//        reference = firebaseDatabase.getReference("Messages");
        reference = firebaseDatabase.getReference();


        logoutButton.setOnClickListener((v) -> {
            firebaseAuth.signOut();
            Toast.makeText(SecActivity.this, "Logout successfully", Toast.LENGTH_SHORT).show();

            SharedPreferences sharedPreferences = getSharedPreferences("LoginData", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLogin", false);
            editor.apply();
        });

        saveButton.setOnClickListener((v) -> {
            new Thread(() -> {

                long secs = (new Date().getTime()) / 1000;
                Date date = new Date();
                String currentDate = date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();
                Message message = handler.obtainMessage();
                Bundle bundle = new Bundle();

                userRef = reference.child(firebaseAuth.getCurrentUser().getUid()).push();
                userRef.child("from").setValue(firebaseAuth.getCurrentUser().getEmail());
                userRef.child("to").setValue(to.getText().toString());
                userRef.child("message").setValue(mesaageText.getText().toString());
                userRef.child("timeInSec").setValue(secs);
                userRef.child("time").setValue(currentDate)
                        .addOnFailureListener(e -> {
                            bundle.putString("msg", "Failed to Save cause " + e.getMessage());
                            message.setData(bundle);
                            handler.sendMessage(message);
                        })
                        .addOnSuccessListener(aVoid -> {
                            bundle.putString("msg", "Data Successfully Saved");
                            message.setData(bundle);
                            handler.sendMessage(message);
                        });
            }).start();


        });

        handler = new Handler(msg -> {
            Bundle bundle = msg.getData();
            Toast.makeText(SecActivity.this, bundle.getString("msg"), Toast.LENGTH_LONG).show();
            return true;
        });


        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getKey().equals(firebaseAuth.getCurrentUser().getUid())) {
                    ArrayList<Map.Entry> arrayList = getMapArrayList(dataSnapshot);
                    for (int i = 0; i < arrayList.size(); i++) {
                        Map.Entry me = arrayList.get(i);
                        Toast.makeText(SecActivity.this, me.getKey() + " " + me.getValue(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public ArrayList<Map.Entry> getMapArrayList(DataSnapshot dataSnapshot) {
        Map map = (Map) dataSnapshot.getValue();
        ArrayList<Map> arrayList = new ArrayList<>();
        ArrayList<Map.Entry> arrayListEntry = new ArrayList<>();
        Set mapSet = map.entrySet();
        Iterator iterator = mapSet.iterator();
        while (iterator.hasNext()) {
            Map.Entry me = (Map.Entry) iterator.next();
            Map mp = (Map) me.getValue();
            arrayList.add(mp);
        }
        for (int i = 0; i < arrayList.size(); i++) {
            Set mapSet2 = arrayList.get(i).entrySet();
            Iterator iterator2 = mapSet2.iterator();
            while (iterator2.hasNext()) {
                Map.Entry me = (Map.Entry) iterator2.next();
                arrayListEntry.add(me);
            }
        }
        return arrayListEntry;
    }
}

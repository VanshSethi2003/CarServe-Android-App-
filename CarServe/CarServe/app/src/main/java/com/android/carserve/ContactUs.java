package com.android.carserve;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class ContactUs extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference mainref;
    EditText emailet, queryet;
    String email,query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        firebaseDatabase = FirebaseDatabase.getInstance();
        mainref = firebaseDatabase.getReference("contactrecords");


        emailet = findViewById(R.id.emailet);
        queryet = findViewById(R.id.queryet);

    }



    public void addlogic(View view) {

        email=emailet.getText().toString();
        query =queryet.getText().toString();
        if(email.isEmpty() || query.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please enter values", Toast.LENGTH_SHORT).show();
        }
        else
        {

            String id = mainref.push().getKey();
            contact recorddata = new contact(id, email,query);
            mainref.child(id).setValue(recorddata);
            Toast.makeText(getApplicationContext(), "Query sent added", Toast.LENGTH_SHORT).show();
            emailet.setText("");
            queryet.setText("");
        }

    }
}
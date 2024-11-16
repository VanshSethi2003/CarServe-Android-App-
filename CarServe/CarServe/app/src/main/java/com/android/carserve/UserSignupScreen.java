package com.android.carserve;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserSignupScreen extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference userref;
    EditText usernameet, passwordet, emailet, phonenoet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_signup_screen);
        firebaseDatabase = FirebaseDatabase.getInstance();
        userref = firebaseDatabase.getReference("usersdata");
        usernameet = findViewById(R.id.usernameet);
        passwordet = findViewById(R.id.passwordet);
        emailet = findViewById(R.id.emailet);
        phonenoet = findViewById(R.id.phonenoet);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MYPREF", MODE_PRIVATE);
        if (pref.getString("username", null) != null) {
            Shared.username = pref.getString("username", null);
            finish();
            startActivity(new Intent(getApplicationContext(), UserDashboard.class));
        }
    }

    public void usersignup(View view) {
        final String username = usernameet.getText().toString();
        final String password = passwordet.getText().toString();
        final String email = emailet.getText().toString();
        final String phone = phonenoet.getText().toString();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please Enter vales", Toast.LENGTH_LONG).show();
        } else if (email.indexOf("@") == -1 || email.indexOf(".") == -1) {

            Toast.makeText(getApplicationContext(), "Invalid email", Toast.LENGTH_LONG).show();

        } else if (phone.length() != 10) {
            Toast.makeText(getApplicationContext(), "Invalid phoneno", Toast.LENGTH_LONG).show();

        } else {
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {
                        Toast.makeText(UserSignupScreen.this, "Username Already Exist", Toast.LENGTH_SHORT).show();
                    } else {
                        userdata userdata = new userdata(username, password, email, phone);
                        userref.child(username).setValue(userdata);

                        SharedPreferences pref = getApplicationContext().getSharedPreferences("MYPREF", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("username", username);
                        editor.putString("email", userdata.getEmail());
                        editor.putString("phone", userdata.getPhoneno());
                        editor.commit();
                        finish();

                        Toast.makeText(UserSignupScreen.this, "Signup success", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), UserDashboard.class));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            userref.child(username).addListenerForSingleValueEvent(valueEventListener);

        }
    }
}

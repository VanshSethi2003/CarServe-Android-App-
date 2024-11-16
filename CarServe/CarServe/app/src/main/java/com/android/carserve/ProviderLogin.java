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

public class ProviderLogin extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference providerref;
    EditText usernameet, passwordet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_login);
        firebaseDatabase = FirebaseDatabase.getInstance();
        providerref = firebaseDatabase.getReference("providers");
        usernameet = findViewById(R.id.usernameet);
        passwordet = findViewById(R.id.passwordet);
    }

    public void providerlogin(View view) {
        final String username = usernameet.getText().toString();
        final String password = passwordet.getText().toString();
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please Enter username and password", Toast.LENGTH_LONG).show();
        } else {

            ValueEventListener valueEventListener=new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {

                        providermodel providermodel = dataSnapshot.getValue(providermodel.class);

                        String pass=providermodel.getPassword();
                        if (pass.equals(password)) {


                            SharedPreferences pref = getApplicationContext().getSharedPreferences("MYPREF", MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("providerdminusername", username);
                            editor.commit();

                            finish();

                            startActivity(new Intent(getApplicationContext(), ProviderDash.class));

                        } else {
                            Toast.makeText(getApplicationContext(), "Wrong Password", Toast.LENGTH_LONG).show();

                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Wrong Username", Toast.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            providerref.child(username).addListenerForSingleValueEvent(valueEventListener);
        }
    }
}
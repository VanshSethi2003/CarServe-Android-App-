package com.android.carserve;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;


public class AddRecord extends Fragment {


    FirebaseDatabase firebaseDatabase;
    DatabaseReference mainref;
    EditText nameet, costet;
    TextView datetv;
    Button datebtn, addrecordbtn;
    String name, cost, date;

    public AddRecord() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View cv = inflater.inflate(R.layout.fragment_add_record, container, false);


        firebaseDatabase = FirebaseDatabase.getInstance();
        mainref = firebaseDatabase.getReference("servicerecords");


        nameet = cv.findViewById(R.id.snet);
        costet = cv.findViewById(R.id.costet);
        datetv = cv.findViewById(R.id.datetv);
        datebtn = cv.findViewById(R.id.datebtn);
        addrecordbtn = cv.findViewById(R.id.addrecordbtn);


        datebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // the instance of our calendar.
                final Calendar c = Calendar.getInstance();

                // on below line we are getting
                // our day, month and year.
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // on below line we are creating a variable for date picker dialog.
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our text view.
                                datetv.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        },
                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        year, month, day);
                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.show();
            }
        });

        addrecordbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addrecord();
            }
        });
        return cv;

    }

    public void addrecord() {

        name=nameet.getText().toString();
        cost =costet.getText().toString();
        date= datetv.getText().toString();
       if(name.isEmpty() || cost.isEmpty()||date.isEmpty())
       {
           Toast.makeText(getContext(), "Please enter values", Toast.LENGTH_SHORT).show();
       }
       else
       {
           SharedPreferences pref = getContext().getSharedPreferences("MYPREF", getContext().MODE_PRIVATE);
           String username=pref.getString("username",null);
           String id = mainref.push().getKey();
           record recorddata = new record(id, name,cost,date,username);
           mainref.child(id).setValue(recorddata);
           Toast.makeText(getContext(), "Record added", Toast.LENGTH_SHORT).show();
           nameet.setText("");
           costet.setText("");
           datetv.setText("");
       }

    }
}
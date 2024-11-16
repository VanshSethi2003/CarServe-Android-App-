package com.android.carserve;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;

public class ViewBookings extends AppCompatActivity {


    FirebaseDatabase firebaseDatabase;
    DatabaseReference bookref;

    ArrayList<booking> al;
    bookingadapter bookad;
    ListView bookinglv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders);
        al = new ArrayList<>();
        bookad = new bookingadapter();
        bookinglv = findViewById(R.id.orderslv);

        bookinglv.setAdapter(bookad);

        firebaseDatabase = FirebaseDatabase.getInstance();
        bookref = firebaseDatabase.getReference("bookings");


        fetchbookings();
    }


    public void fetchbookings() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MYPREF", MODE_PRIVATE);
        String username = pref.getString("username", null);
        bookref.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                al.clear();
                for (DataSnapshot single_data_snapshot : snapshot.getChildren()) {
                    booking obj = single_data_snapshot.getValue(booking.class);
                    al.add(obj);
                }

                al.sort(new Comparator<booking>() {
                    @Override
                    public int compare(booking b1, booking b2) {
                        return b2.getDate().compareTo(b1.getDate());
                    }
                });

                //send refresh to list
                bookad.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    /// Inner Class ///
    class bookingadapter extends BaseAdapter {

        @Override
        public int getCount() {
            return al.size();
        }

        @Override
        public Object getItem(int i) {
            return al.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i * 100;
        }

        @Override
        public View getView(final int i, View customView, ViewGroup parent) {
            // Load Single Row Design, Fill Data and return its View

            // STEP 1  , inflate single row design
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            customView = inflater.inflate(R.layout.layoutforbooking, parent, false);

            // STEP 2 ,  get ith object from ArrayList
            final booking obj = al.get(i);

            // STEP 3, fill data from obj to singlerow (customview)
            TextView bookingidtv = customView.findViewById(R.id.bookingid);
            TextView bookingdatetv = customView.findViewById(R.id.bookingdatetv);
            TextView bookingtimetv = customView.findViewById(R.id.bookingtimetv);
            TextView bookingcost = customView.findViewById(R.id.bookingcost);
            TextView busernametv = customView.findViewById(R.id.busernametv);
            TextView bemailtv = customView.findViewById(R.id.bemailtv);
            TextView bphonenotv = customView.findViewById(R.id.bphonenotv);
            Button cancelappointment = customView.findViewById(R.id.cancelappointment);



            TextView statustv = customView.findViewById(R.id.statustv);


            bookingidtv.setText("ID: "+obj.getBid() + "");
            bookingdatetv.setText("Date: "+obj.getDate() + "");
            bookingtimetv.setText("Time: "+obj.getTime() + "");
            bookingcost.setText("COST: \u20B9"+obj.getPackagecost() + "");
            busernametv.setText("Username: "+obj.getUsername());
            bemailtv.setText("Email: "+obj.getEmail());
            bphonenotv.setText("Phone no: "+obj.getPhoneno());
            statustv.setText("STATUS: "+obj.getStatus());


            cancelappointment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    AlertDialog.Builder builder = new AlertDialog.Builder(ViewBookings.this);

                    builder.setTitle("confirmation !");
                    builder.setMessage("Are you sure you want to cancel it?");
//                                                        builder.setIcon(R.drawable.icon1);
                    builder.setCancelable(false);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            bookref.child(obj.getBid() + "").child("status").setValue("canceled");
                            fetchbookings();
                            Toast.makeText(getApplicationContext(), "Appointment canceled", Toast.LENGTH_SHORT).show();
                        }
                    });

                    AlertDialog ad = builder.create();
                    ad.show();
                }
            });

            return customView;
        }

    }

}

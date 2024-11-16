package com.android.carserve.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.carserve.R;
import com.android.carserve.booking;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;


public class ViewBookings extends Fragment {


    FirebaseDatabase firebaseDatabase;
    DatabaseReference bookref;

    ArrayList<booking> al;
    bookingadapter bookad;
    ListView bookinglv;

    public ViewBookings() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View cv =  inflater.inflate(R.layout.fragment_add_package, container, false);
        al = new ArrayList<>();
        bookad = new bookingadapter();
        bookinglv = cv.findViewById(R.id.orderslv);

        bookinglv.setAdapter(bookad);

        firebaseDatabase = FirebaseDatabase.getInstance();
        bookref = firebaseDatabase.getReference("bookings");


        fetchbookings();
        return cv;
    }


    public void fetchbookings() {


        bookref.addListenerForSingleValueEvent(new ValueEventListener() {
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
            customView = inflater.inflate(R.layout.layoutforbooking1, parent, false);

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
            Button userlocation = customView.findViewById(R.id.userlocation);


            TextView packname = customView.findViewById(R.id.packname);

            TextView statustv = customView.findViewById(R.id.statustv);

            if(obj.isPickup() )
            {
                userlocation.setVisibility(View.VISIBLE);
            }
            else{
                userlocation.setVisibility(View.INVISIBLE);
            }

            bookingidtv.setText("ID: "+obj.getBid() + "");
            bookingdatetv.setText("Date: "+obj.getDate() + "");
            bookingtimetv.setText("Time: "+obj.getTime() + "");
            bookingcost.setText("COST: \u20B9"+obj.getPackagecost() + "");
            busernametv.setText("Username: "+obj.getUsername());
            bemailtv.setText("Email: "+obj.getEmail());
            bphonenotv.setText("Phone no: "+obj.getPhoneno());
            statustv.setText("STATUS: "+obj.getStatus());
            packname.setText("pack: "+obj.getPackname());

            userlocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String uri = String.format(Locale.ENGLISH, "geo:%f,%f", 	Float.parseFloat(obj.getLat()), Float.parseFloat(obj.getLng()));
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(intent);
                }
            });

            return customView;
        }

    }
}
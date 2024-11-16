package com.android.carserve;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ViewRecords extends Fragment {


    FirebaseDatabase firebaseDatabase;
    DatabaseReference mainref, mainref1;

    ArrayList<record> al;
    myadapter myad;
    ListView lvrecords;

    public ViewRecords() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View cv = inflater.inflate(R.layout.fragment_view_records, container, false);
        al = new ArrayList<>();

        myad = new myadapter();
        lvrecords = cv.findViewById(R.id.lvrecords);
        lvrecords.setAdapter(myad);
        firebaseDatabase = FirebaseDatabase.getInstance();
        mainref1 = firebaseDatabase.getReference("servicerecords");
        return cv;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            fetchrecords();
        }
        else {
        }
    }

    public void fetchrecords() {

        SharedPreferences pref = getContext().getSharedPreferences("MYPREF", getContext().MODE_PRIVATE);
        String username = pref.getString("username", null);
        mainref1.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                al.clear();


                for (DataSnapshot single_data_snapshot : snapshot.getChildren()) {
                    record obj = single_data_snapshot.getValue(record.class);
                    al.add(obj);
                }


                //send refresh to list
                myad.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    /// Inner Class ///
    class myadapter extends BaseAdapter {

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
        public View getView(int i, View customView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            customView = inflater.inflate(R.layout.layoutforrecord, parent, false);

            final record obj = al.get(i);
            TextView nametv = customView.findViewById(R.id.nametv);
            TextView costtv = customView.findViewById(R.id.costtv);
            TextView datetv = customView.findViewById(R.id.datetv);


            nametv.setText("Name: " + obj.getName() + "");
            costtv.setText("COST: $ " + obj.getCost() + "");
            datetv.setText("Date: " + obj.getDate() + "");

            ImageView btncd = customView.findViewById(R.id.btncd);

            btncd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                    builder.setTitle("confirmation !");
                    builder.setMessage("Are you sure you want to delete it?");
//                                                        builder.setIcon(R.drawable.icon1);
                    builder.setCancelable(false);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            mainref1.child(obj.getId() + "").removeValue();
                            fetchrecords();
                            Toast.makeText(getContext(), "record Deleted", Toast.LENGTH_SHORT).show();
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
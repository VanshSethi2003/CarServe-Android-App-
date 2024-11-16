package com.android.carserve.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.carserve.R;
import com.android.carserve.packagedata;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ViewPackages extends Fragment {


    FirebaseDatabase firebaseDatabase;
    DatabaseReference mainref, mainref1;

    Spinner packagetypesp;
    ArrayList<String> packtypelist;
    ArrayAdapter<String> adapter;
    ArrayList<packagedata> al;
    myadapter myad;
    ListView lbpackages;

    public ViewPackages() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View cv= inflater.inflate(R.layout.fragment_view_packages, container, false);

        packtypelist = new ArrayList<>();
        al = new ArrayList<>();

        packagetypesp = cv.findViewById(R.id.packagetypesp);

        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, packtypelist);
        packagetypesp.setAdapter(adapter);
        myad = new myadapter();
        lbpackages = cv.findViewById(R.id.lvpackages);

        lbpackages.setAdapter(myad);

        firebaseDatabase = FirebaseDatabase.getInstance();

        mainref1 = firebaseDatabase.getReference("packages");

        loadpackagetypes();

        packagetypesp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String pack = packtypelist.get(i);
                fetchpackages(pack);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        return cv;
    }


    public void loadpackagetypes() {

        packtypelist.clear();
        packtypelist.add("car service");
        packtypelist.add("car modification");
        packtypelist.add("car washing");
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onResume() {
        super.onResume();
        loadpackagetypes();
    }

    public void fetchpackages(String packagename) {

        Log.d("MYMSG", packagename + " -->");
        packagename = packagename.toLowerCase();

        mainref1.orderByChild("packagetype").equalTo(packagename).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                al.clear();


                for (DataSnapshot single_data_snapshot : snapshot.getChildren()) {
                    packagedata obj = single_data_snapshot.getValue(packagedata.class);
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
            // Load Single Row Design, Fill Data and return its View

            // STEP 1  , inflate single row design
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            customView = inflater.inflate(R.layout.viewpacklayout, parent, false);


            // STEP 2 ,  get ith object from ArrayList
            final packagedata obj = al.get(i);


            TextView pnametv = customView.findViewById(R.id.pnametv);
            TextView ppricetv = customView.findViewById(R.id.ppricetv);
            TextView pdestv = customView.findViewById(R.id.pdestv);

            ImageView pimv = customView.findViewById(R.id.pimv);

            pnametv.setText(""+obj.getName() + "");
            ppricetv.setText("COST: $ "+obj.getCost() + "");
            pdestv.setText(""+obj.getDes() + "");

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

                            mainref1.child(obj.getPid() + "").removeValue();
                            loadpackagetypes();
                            Toast.makeText(getContext(), "Package Deleted", Toast.LENGTH_SHORT).show();
                        }
                    });

                    AlertDialog ad = builder.create();
                    ad.show();

                }
            });

            Picasso.get().load(obj.getPhoto()).into(pimv);


            return customView;
        }

    }

}
package com.android.carserve.ui.notifications;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.android.carserve.PackageAdapter;
import com.android.carserve.PackageAdapter2;
import com.android.carserve.R;
import com.android.carserve.databinding.FragmentNotificationsBinding;
import com.android.carserve.packagedata;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment {

    ArrayList<packagedata> packagelist;
    ArrayList<String> aldata;
    RecyclerView rv;
    PackageAdapter2 packageAdapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference packref;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View cv = inflater.inflate(R.layout.fragment_notifications, container, false);
        firebaseDatabase = FirebaseDatabase.getInstance();
        packref = firebaseDatabase.getReference("packages");

        rv = cv.findViewById(R.id.rvpackage);
        packagelist = new ArrayList<>();
        aldata = new ArrayList<>();

        packageAdapter = new PackageAdapter2(packagelist, this);
        rv.setAdapter(packageAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(linearLayoutManager);

        return cv;
    }



    @Override
    public void onResume() {
        super.onResume();
        loadPackages();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    public void loadPackages() {

        packagelist.clear();
        ValueEventListener valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Log.d("MYMSG",snapshot.getValue()+"");
                for(DataSnapshot snapshot1:snapshot.getChildren())
                {
                    packagedata packdata = snapshot1.getValue(com.android.carserve.packagedata.class);
                    packagelist.add(packdata);
                }
                packageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        packref.orderByChild("packagetype").equalTo("car washing").addListenerForSingleValueEvent(valueEventListener);
    }
}
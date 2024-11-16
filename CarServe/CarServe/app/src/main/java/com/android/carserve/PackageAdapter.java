package com.android.carserve;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.android.carserve.ui.home.HomeFragment;


import java.util.ArrayList;

public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.MyViewHolder> {

    ArrayList<packagedata> al;
    HomeFragment context;

    public PackageAdapter(ArrayList<packagedata> al, HomeFragment context) {
        this.al = al;
        this.context =  context;

    }

    // Define ur own View Holder (Refers to Single Row)
    class MyViewHolder extends RecyclerView.ViewHolder {
        View singlecardview;

        // We have Changed View (which represent single row) to CardView in whole code
        public MyViewHolder(View itemView) {
            super(itemView);
            singlecardview = (itemView);
        }
    }

    @Override
    public PackageAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View viewthatcontainscardview = inflater.inflate(R.layout.layoutforpackes, parent, false);
        Log.d("MYMESSAGE", "On CreateView Holder Done");
        return new MyViewHolder(viewthatcontainscardview);
    }


    @Override
    public void onBindViewHolder(PackageAdapter.MyViewHolder holder, final int position) {

        View localcardview = holder.singlecardview;

        TextView packname,packprice;
        ImageView packimg;
        LinearLayout mainll;

        packname = (TextView) (localcardview.findViewById(R.id.packname));
        packprice = (TextView) (localcardview.findViewById(R.id.packprice));
        packimg = (ImageView) (localcardview.findViewById(R.id.packimg));
        mainll=localcardview.findViewById(R.id.mainll);

        packagedata packdata = al.get(position);
        packname.setText(packdata.getName());
        packprice.setText(packdata.getCost());
        Picasso.get().load(packdata.getPhoto()).into(packimg);

        mainll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(),ViewDetailAndBook.class);
                intent.putExtra("pid",packdata.getPid()+"");
                intent.putExtra("name",packdata.getName()+"");
                intent.putExtra("des",packdata.getDes()+"");
                intent.putExtra("photo",packdata.getPhoto()+"");
                intent.putExtra("cost",packdata.getCost()+"");
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {

        Log.d("MYMESSAGE", "get Item Count Called");
        return al.size();
    }
}


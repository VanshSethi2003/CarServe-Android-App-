package com.android.carserve;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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

public class AddNewPackage extends AppCompatActivity {

    Spinner packagetypesp;
    ArrayList<String> packtypelist;
    ArrayAdapter<String> adapter;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference mainref;
    StorageReference mainref_storage;
    FirebaseStorage firebaseStorage;


    Uri urifromgallery;
    EditText nameet, deset, costet;
    ImageView packphotoimv;
    String name, cost, des,packagetype;

    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_package);


        packagetypesp = findViewById(R.id.packagetypesp);
        packtypelist = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, packtypelist);
        packagetypesp.setAdapter(adapter);

        firebaseDatabase = FirebaseDatabase.getInstance();
        mainref = firebaseDatabase.getReference("packages");


        nameet = findViewById(R.id.nameet);
        costet = findViewById(R.id.costet);
        deset = findViewById(R.id.deset);

        packphotoimv = findViewById(R.id.packphotoimv);

        firebaseStorage = FirebaseStorage.getInstance();
        mainref_storage = firebaseStorage.getReference("products");


        loadpackagetype();


        pd = new ProgressDialog(AddNewPackage.this);
        pd.setMessage("Adding Package");
        pd.setCancelable(false);


    }


    public void loadpackagetype() {


        packtypelist.clear();
        packtypelist.add("car service");
        packtypelist.add("car modification");
        packtypelist.add("car washing");
        adapter.notifyDataSetChanged();

    }


    public void picphoto(View view) {

        Intent in = new Intent(Intent.ACTION_PICK);
        in.setType("image/*");
        startActivityForResult(in, 130);

    }

    public void addlogic(View view) {

        name = nameet.getText().toString();
        cost = costet.getText().toString();
        des = deset.getText().toString();


        packagetype = packagetypesp.getSelectedItem().toString();


        if (name.isEmpty() || des.isEmpty() || cost.isEmpty()  || packagetype.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please Enter Values", Toast.LENGTH_SHORT).show();
        } else if (urifromgallery == null) {
            Toast.makeText(getApplicationContext(), "Please Choose Photo", Toast.LENGTH_SHORT).show();
        } else {
            pd.show();
            uploadphototoserver();

        }


    }

    public void uploadphototoserver() {


        // upload photo
        File filetoupload = new File(getRealPathFromURI(urifromgallery));
        String nameofpic = "service" + (int) (Math.random() * 1000000000) + filetoupload.getName();
        Uri urioffile = Uri.fromFile(filetoupload);
        final StorageReference srfiletoupload = mainref_storage.child(nameofpic);
        final UploadTask uploadTask = srfiletoupload.putFile(urioffile);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = srfiletoupload.getDownloadUrl();
                uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String filepath = uri.toString();
                        String id = mainref.push().getKey();
                        packagedata packagedata=new packagedata(id,name,des,filepath,cost,packagetype);
                        mainref.child(id).setValue(packagedata);
                        Toast.makeText(getApplicationContext(), "packaged added", Toast.LENGTH_SHORT).show();
                        finish();
                        pd.hide();

                    }
                });
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {


                    }
                });

            }
        });

    }


    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent backdata) {
        super.onActivityResult(requestCode, resultCode, backdata);

        if (requestCode == 130) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = backdata.getData();
                urifromgallery = backdata.getData();
                packphotoimv.setImageURI(uri);
            }
        }
    }
}
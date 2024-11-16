package com.android.carserve;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;

public class ViewDetailAndBook extends AppCompatActivity implements LocationListener {

    String pid, name, des, cost, photo, username, email, phoneno;

    TextView namtv, destv, costtv, usernametv, emailtv, phonetv, datetv, timetv;
    ImageView photoimv;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference bookref;
    LocationManager locationManager;

    Double lat,lng;
    CheckBox arrangepickup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        setContentView(R.layout.activity_view_detail_and_book);
        Intent in = getIntent();
        namtv = findViewById(R.id.nametv);
        destv = findViewById(R.id.destv);
        costtv = findViewById(R.id.costtv);
        photoimv = findViewById(R.id.photoimv);
        emailtv = findViewById(R.id.emailtv);
        usernametv = findViewById(R.id.usernametv);
        phonetv = findViewById(R.id.phonetv);
        datetv = findViewById(R.id.datetv);
        timetv = findViewById(R.id.timetv);
        arrangepickup=findViewById(R.id.arrangepickup);

        firebaseDatabase = FirebaseDatabase.getInstance();
        bookref = firebaseDatabase.getReference("bookings");

        pid = getIntent().getStringExtra("pid");
        name = getIntent().getStringExtra("name");
        des = getIntent().getStringExtra("des");
        cost = getIntent().getStringExtra("cost");
        photo = getIntent().getStringExtra("photo");
        namtv.setText(name);
        destv.setText(des);
        costtv.setText("PRICE: $" + cost);

        Picasso.get().load(photo).into(photoimv);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MYPREF", MODE_PRIVATE);
        if (pref.getString("username", null) != null) {
            username = pref.getString("username", null);
            email = pref.getString("email", null);
            phoneno = pref.getString("phoneno", null);

            usernametv.setText("Username: " + username);
            emailtv.setText("Email: " + email);
            phonetv.setText("Phone no: " + phoneno);

        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        // getting GPS status
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        boolean isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if(isGPSEnabled && isNetworkEnabled) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        }
        if(isGPSEnabled && !isNetworkEnabled) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
        if(!isGPSEnabled && isNetworkEnabled) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        }

    }

    public void bookpackage(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ViewDetailAndBook.this);

        builder.setTitle("confirmation !");
        builder.setMessage("Are you sure you want to book?");
//                                                        builder.setIcon(R.drawable.icon1);
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (timetv.getText().toString().isEmpty() || datetv.getText().toString().isEmpty()) {
                    Toast.makeText(ViewDetailAndBook.this, "Please choose date and time", Toast.LENGTH_SHORT).show();
                } else {

                    String id = bookref.push().getKey();

                    boolean pickup=false;
                    if(arrangepickup.isChecked())
                    {
                        if(lat!=null && lng!=null) {
                            pickup = true;
                        }
                        else
                        {
                            pickup = false;
                            Toast.makeText(ViewDetailAndBook.this, "Could not arrange pick up", Toast.LENGTH_SHORT).show();
                        }
                    }



                    booking bookdata = new booking(id, pid, name, cost, username, datetv.getText().toString(), timetv.getText().toString(), new Date().toString(), "booked", email, phoneno,pickup,lat+"",lng+"");
                    bookref.child(id).setValue(bookdata);
                    Toast.makeText(getApplicationContext(), "Booking Successful", Toast.LENGTH_SHORT).show();
                    finish();


                    notifyuser();

                }
            }
        });

        AlertDialog ad = builder.create();
        ad.show();

    }

    @Override
    protected void onDestroy() {
        locationManager.removeUpdates(this);
        super.onDestroy();
    }

    private void notifyuser() {
        Intent intent = new Intent(getApplicationContext(), ViewDetailAndBook.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder b = new NotificationCompat.Builder(getApplicationContext());

        b.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_baseline_add_24)
                .setContentTitle("Booking Confirmation")
                .setContentText("Your appointment has been booked")
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent)
                .setContentInfo("Info");


        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, b.build());
    }

    public void choosedate(View view) {

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
                ViewDetailAndBook.this,
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

    public void choosertime(View view) {
        // on below line we are getting the
        // instance of our calendar.
        final Calendar c = Calendar.getInstance();

        // on below line we are getting our hour, minute.
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // on below line we are initializing our Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(ViewDetailAndBook.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        // on below line we are setting selected time
                        // in our text view.
                        timetv.setText(hourOfDay + ":" + minute);
                    }
                }, hour, minute, false);
        // at last we are calling show to
        // display our time picker dialog.
        timePickerDialog.show();
    }

    public void chooselocation(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
    }


    @Override
    public void onLocationChanged(Location location) {

        lat=location.getLatitude();
        lng=location.getLongitude();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }

    public void choosecurrentlocation(View view) {

    }
}
package com.example.busloconductor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity implements LocationListener {
    private Button scanqr;
    int BARCODE_READER = 1;
    TextView ar,  amount, busnum;
    LocationManager locationManager;
    Context context;
    FirebaseDatabase database;
    DatabaseReference mref;
    Bundle bundle;
    int REQUEST_LOCATION_PERMISSION = 1, xx=0, REQUEST_CHECK_SETTINGS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        database = FirebaseDatabase.getInstance();
        database = FirebaseDatabase.getInstance();
         bundle = getIntent().getExtras();
        String x = "Bus No.: " + bundle.getString("busno");
//        mref = database.getReference().child("Earnings").child(bundle.getString("busno"));
        scanqr = (Button) findViewById(R.id.scanqr);
        busnum = (TextView) findViewById(R.id.busnum);
        busnum.setText(x);
//        ar= (TextView) findViewById(R.id.amountrtillnow);
        amount = (TextView) findViewById(R.id.lastamount);
        scanqr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BarcodeCapture.class);
                startActivityForResult(intent, BARCODE_READER);
            }
        });
        mref = database.getReference().child("Bus").child(bundle.getString("busno"));
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(MainActivity.this, "Internet not granted", Toast.LENGTH_LONG).show();
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION);
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    xx);
        }
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 0, this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == BARCODE_READER){
            if(resultCode == CommonStatusCodes.SUCCESS){
                if(data!=null){
                     Barcode barcode = data.getParcelableExtra(BarcodeCapture.BarcodeObject);
                    Point[] p = barcode.cornerPoints;
                    amount.setText("Amount Last Received: " + barcode.displayValue);



                }else {
                    ar.setText("No barcode captured");
                }
            }else {
                Log.v("AAA", CommonStatusCodes.getStatusCodeString(resultCode));

            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onStart() {
        super.onStart();

//            startService(new Intent(MainActivity.this, NormalService.class).putExtra("Name", "NAMEE"));

//        Toast.makeText(this, "Service started", Toast.LENGTH_SHORT).show();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 0, this);
        Log.v("AAA", "STARTED");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 0, this);


    }

    @Override
    public void onLocationChanged(Location location) {
        double lon = location.getLongitude();
        double lat = location.getLatitude();
        mref.child("lat").setValue(lat+"");
        mref.child("lon").setValue(lon+"");
        Log.v("AAA", "onlocationchangedcalled " + lat+" "+lon);
        if(lat >28.713 && lat<28.714 && lon > 77.136 && lon < 137){
            notification(bundle.getString("busno") + " Bus is near Prashant Vihar");
            mref.child("status").setValue("near Prashant Vihar");
        }
        else if(lat >28.707 && lat<28.714 && lon > 77.126 && lon < 131){
            notification(bundle.getString("busno") + " Bus is near District Court Subway");
            mref.child("status").setValue("near District Court Subway");
        }
        else if(lat >28.698 && lat<28.703 && lon > 77.128 && lon < 133){
            notification(bundle.getString("busno") + " Bus is near Madhuban Chowk");
            mref.child("status").setValue("near Madhuban Chowk");
        }
        else if(lat >28.703 && lat<28.707 && lon > 77.126 && lon < 131){
            notification(bundle.getString("busno") + " Bus is near Kohat Enclave");
            mref.child("status").setValue("near Kohat Enclave");
        }
        else if(lat >28.703 && lat<28.706 && lon > 77.135 && lon < 139){
            notification(bundle.getString("busno") + " Bus is near Pitampura");
            mref.child("status").setValue("near Pitampura");
        }
        else if(lat >28.692 && lat<28.696 && lon > 77.141 && lon < 145){
            notification(bundle.getString("busno") + " Bus is near ND Block");
            mref.child("status").setValue("near ND Block");
        }
        else if(lat >28.691 && lat<28.695 && lon > 77.147 && lon < 151){
            notification(bundle.getString("busno") + " Bus is near Netaji Subhash Place");
            mref.child("status").setValue("near Netaji Subhash Place");
        }

    }
    public void notification(String bigtext){
        int nid = 0;
        Log.v("AAA", bigtext);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Bus update")
                .setContentText(bigtext)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);

        Uri path = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(path);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelId =  "YOUR_CHANNEL_ID";
            NotificationChannel channel = new NotificationChannel(channelId, "Channel human readabale titel",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(channelId);
        }

        notificationManager.notify(nid, builder.build());

    }
    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onProviderEnabled(String s) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onProviderDisabled(String s) {
        Toast.makeText(MainActivity.this, "Please Enable GPS ", Toast.LENGTH_SHORT).show();
        Log.d("Latitude","enable");
    }
}

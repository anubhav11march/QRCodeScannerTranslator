package com.example.busloconductor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;



public class MainActivity extends AppCompatActivity {
    private Button scanqr;
    int BARCODE_READER = 1;
    TextView ar,  amount, busnum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        database = FirebaseDatabase.getInstance();

        Bundle bundle = getIntent().getExtras();
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
}

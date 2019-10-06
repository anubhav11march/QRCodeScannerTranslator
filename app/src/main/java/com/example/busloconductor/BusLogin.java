package com.example.busloconductor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class BusLogin extends AppCompatActivity {
    Button busreg;
    EditText busno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_login);
        busno = (EditText) findViewById(R.id.busno);
        busreg = (Button) findViewById(R.id.busreg);

    }

    public void busreg(View view){
        Intent intent = new Intent(BusLogin.this, MainActivity.class);
        intent.putExtra("busno", busno.getText().toString().trim());
        Log.v("AAA", busno.getText().toString().trim());
        startActivity(intent);
    }
}

package com.example.scantext_gms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    int scanTextOverCamera_requestCode = 1, scanTextFromImage_requestCode = 2;
    String scannedText;
    private Button captureBillBtn, pickBillFromGallaryBtn;
    private ImageView imageView;
    private EditText editTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        captureBillBtn = findViewById(R.id.capture_bill_btn);
        pickBillFromGallaryBtn = findViewById(R.id.pickbillfromgallary_btn);
        imageView = findViewById(R.id.image_view);
        editTextView = findViewById(R.id.text_display);

        captureBillBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Test : MainActivity", "Invoking startCamera()");
                Toast.makeText(getApplicationContext(), "Invoking startCamera()", Toast.LENGTH_SHORT);
                //startActivity(new Intent(MainActivity.this, ScanTextOverCamera.class));// //
                startActivityForResult(new Intent(MainActivity.this, ScanTextOverCamera.class), scanTextOverCamera_requestCode);
            }
        });

        pickBillFromGallaryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Test : MainActivity", "invoking ScanTextFromImage()");
                //startActivity(new Intent(MainActivity.this, ScanTextFromImage.class) );
                startActivityForResult(new Intent(MainActivity.this, ScanTextFromImage.class), scanTextFromImage_requestCode);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("Test : MainActivity", "onActivityResult " + requestCode + " " + resultCode);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 1:
                    scannedText = data.getStringExtra("DetectedTextFromCamera");
                    break;
                case 2:
                    scannedText = data.getStringExtra("DetectedTextFromImage");
                    break;
                default:
                    Log.e("Test : MainActivity", "requestCode Not Found");
            }
        }
        Log.d("Test : MainActivity", "Result Text:" + scannedText);
        // TODO Update your TextView.
        editTextView.setText(scannedText);
    }
}
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

    int requestCode;
    String scannedText;
    private Button captureImageBtn, detectTextBtn;
    private ImageView imageView;
    private EditText editTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        captureImageBtn = findViewById(R.id.capture_image_btn);
        detectTextBtn = findViewById(R.id.detect_text_image_btn);
        imageView = findViewById(R.id.image_view);
        editTextView = findViewById(R.id.text_display);

        captureImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Test", "invoke startCamera()");
                Toast.makeText(getApplicationContext(), "Invoke startCamera()", Toast.LENGTH_SHORT);
                //startActivity(new Intent(MainActivity.this, ScanTextOverCamera.class));
                startActivityForResult(new Intent(MainActivity.this, ScanTextOverCamera.class), requestCode);


            }
        });


        detectTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        //   startActivity(new Intent(MainActivity.this, MainScreenActivity.class));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("Test", "Result Text:");

        if (resultCode == Activity.RESULT_OK) {
            scannedText = data.getStringExtra("DetectedText");
            Log.i("Test", "Result Text:" + scannedText);
            // TODO Update your TextView.
            editTextView.setText(scannedText);
        }
    }
}
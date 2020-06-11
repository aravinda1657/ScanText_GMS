package com.example.scantext_gms;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainScreenActivity extends AppCompatActivity{

    private Button captureImageBtn,detectTextBtn;
    private ImageView imageView;
    private TextView textView;

    public void activityMainScreen(){

        captureImageBtn = findViewById(R.id.capture_image_btn);
        detectTextBtn = findViewById(R.id.detect_text_image_btn);
        imageView = findViewById(R.id.image_view);

        textView = findViewById(R.id.text_display);


        captureImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Log.d("Test","invoke startCamera()");
                Toast.makeText(getApplicationContext(),"Invoke startCamera()",Toast.LENGTH_SHORT);
                startActivity(new Intent(MainScreenActivity.this, ScanTextOverCamera.class));
            }
        });
        detectTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }


}
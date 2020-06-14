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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    int scanTextOverCamera_requestCode = 1, scanTextFromImage_requestCode = 2;
    String scannedText;
    private Button captureBillBtn, pickBillFromGallaryBtn;
    private ImageView imageView;
    private EditText editTextView;

    private Button getItemsFromText_btn;
    private EditText editText_MyProducts;
    String myProducts;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        captureBillBtn = findViewById(R.id.capture_bill_btn);
        pickBillFromGallaryBtn = findViewById(R.id.pickbillfromgallary_btn);
        imageView = findViewById(R.id.image_view);
        editTextView = findViewById(R.id.text_display);
        getItemsFromText_btn=findViewById(R.id.getItemsFromText_btn);



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

        getItemsFromText_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initilizeBaseProductsJSON();
                jsonParseProducts();
                editTextView.setText(myProducts);
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

    public void initilizeBaseProductsJSON() {

    /*    String jsonFileInApp_String = Utils.getJsonFromAssets(getApplicationContext(), "BaseProducts.json");
       // Log.i("AppTest_Main: Productdata", jsonFileInApp_String);
     */
        try {
            InputStream is = getAssets().open("BaseProducts.json");
            File destination = new File(getApplicationContext().getFilesDir()+"/BaseProducts.json");
            Log.i("AppTest_Main : Paths", is.toString() + "  " + destination.toString());
            copyFileUsingStream(is, destination);

        } catch (Exception e) {
            Log.i("AppTest_Main : Productdata", "ErrorCopying file : "+ e.getMessage() );
        }
    }

    private static void copyFileUsingStream(InputStream source, File dest) throws IOException {
        InputStream is = source;
        OutputStream os = null;
        try {
            // is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }

    public void jsonParseProducts() {

        List<Product> newProducts = BillFilter.FilterBill();
        //Print Products
        myProducts="Product      Price      Category \n";
        for (int i = 0; i < newProducts.size(); i++) {
            Log.i("AppTest_Main : Productdata", "> initial_Items " + i + "\n" + newProducts.get(i));
            System.out.println("AppTest_Main :  > initial_Items " + i + "\n" + newProducts.get(i));
            myProducts=myProducts+newProducts.get(i).toString2();
        }



/*
        String jsonFileString = Utils.getJsonFromAssets(getApplicationContext(), "products.json");
        Log.i("AppTest_Main: Productdata", jsonFileString);

        Gson gson = new Gson();

        Type listProductType = new TypeToken<List<Product>>() {
        }.getType();
        //Convert JSON to Objects
        List<Product> products = gson.fromJson(jsonFileString, listProductType);

        for (int i = 0; i < products.size(); i++) {
            Log.i("AppTest_Main: data", "> Item " + i + "\n" + products.get(i));
        }

        Product myProduct = new Product();
        myProduct.name = "Snickers";
        myProduct.price = 61;
        myProduct.catogory = "Unknown";

        String json = new Gson().toJson(myProduct);
        System.out.println(json);
        Log.i("AppTest_Main: Productdata", json);

      /*  Gson gson2 = new Gson();
        Writer output = null;
        File file = new File("storage/sdcard/ExpensesCatogorization/" + "Products" + ".json");
        try {


           Log.i("AppTest_Main: Productdata", "1 Writing To Json    Path :" + getApplicationContext().getFilesDir());
            File mediaStorageDir = new File(getApplicationContext().getFilesDir(), "EC");

            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d("App", "1 failed to create directory");
                }
            }

            output = new BufferedWriter(new FileWriter(file));
            output.write(json.toString());
        } catch (IOException e) {
           Log.i("AppTest_Main: Productdata", "1 Error Writing To Json");
            e.printStackTrace();
        }

        try (Writer writer = Files.newBufferedWriter(Paths.get(getApplicationContext().getFilesDir() + "/EC/" + "Products.json"))) {
            Log.i("AppTest_Main: Productdata", "Writing To Json");
            gson.toJson(myProduct, writer);
            writer.append(myProduct.toString());
            writer.append(myProduct.toString());
            //  writeJsonFile()

        } catch (IOException e) {
            Log.i("AppTest_Main : Productdata", "Error Writing To Json : "+ e.getMessage() );
            e.printStackTrace();
        }
*/

    }



}
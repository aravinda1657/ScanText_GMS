package com.example.scantext_gms;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.lang.Appendable;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    int scanTextOverCamera_requestCode = 1, scanTextFromImage_requestCode = 2;
    String scannedText;
    private Button captureBillBtn, pickBillFromGallaryBtn, analyseSpends;
    private ImageView imageView;
    private EditText editTextView;

    private Button getItemsFromText_btn;
    List<Product> newProducts;
    Context context;


    SharedPreferences prefs = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        captureBillBtn = findViewById(R.id.capture_bill_btn);
        pickBillFromGallaryBtn = findViewById(R.id.pickbillfromgallary_btn);
        imageView = findViewById(R.id.image_view);
        editTextView = findViewById(R.id.text_display);
        analyseSpends = findViewById(R.id.spendAnalytics);
        getItemsFromText_btn = findViewById(R.id.getItemsFromText_btn);

        scannedText = editTextView.getText().toString();
      ////
        prefs = getSharedPreferences("com.example.scantext_gms", MODE_PRIVATE);

        if (prefs.getBoolean("firstrun", true)) {
            // Do first run stuff here then set 'firstrun' as false
            // using the following line to edit/commit prefs
            initilizeBaseProductsJSON();
            prefs.edit().putBoolean("firstrun", false).commit();
        }

        getItemsFromText_btn.callOnClick();

        captureBillBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("Test : MainActivity", "Invoking startCamera()");
                Toast.makeText(getApplicationContext(), "Invoking startCamera()", Toast.LENGTH_SHORT);
                //startActivity(new Intent(MainActivity.this, ScanTextOverCamera.class));// //
                startActivityForResult(new Intent(MainActivity.this, ScanTextOverCamera.class), scanTextOverCamera_requestCode);

                // startActivityForResult(new Intent(MainActivity.this, MainActivity_Json.class), scanTextOverCamera_requestCode);
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
                scannedText = editTextView.getText().toString();
                jsonParseProducts();
                editTextView.setText(scannedText);
            }
        });

        analyseSpends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                analyseSpends(newProducts);
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
            File destinationSystem = new File(getApplicationContext().getFilesDir() + "/BaseProducts.json");
            JsonReadWrite.copyFileUsingStream(is, destinationSystem);

            File destination = new File(context.getExternalFilesDir(null) + "/BaseProducts.json");
            Log.i("AppTest_Main : Paths", is.toString() + "  " + destination.toString());
            JsonReadWrite.copyFileUsingStream(is, destination);

        } catch (Exception e) {
            Log.i("AppTest_Main : Productdata", "ErrorCopying file : " + e.getMessage());
        }
    }

    public void jsonParseProducts() {

        Map<String, Product> baseProductsMapDictionary = new HashMap<String, Product>();
        List<Product> baseProducts = new ArrayList<Product>();
        newProducts = BillFilter.FilterBill(scannedText);

        try {
            //getObjectsFrombaseJSONFile - Converts base JSON String to Objects
            Reader reader = Files.newBufferedReader(Paths.get(getApplicationContext().getFilesDir() + "/BaseProducts.json"));
            baseProducts = JsonReadWrite.getObjectsFrombaseJSONFile(reader);
            if (baseProducts != null) {
                //convertObjectsoDict - Converts baseObjectsToDictionary
                baseProductsMapDictionary = JsonReadWrite.convertObjectsoDict(baseProducts);
            } else {
                Log.e("AppTest_Main : Json ", "Error : baseProducts is empty  ");
            }

            //getCatogoryFromBaseProductsJSONFile - Assigns catogory of known products to products Object.
            newProducts = JsonReadWrite.getCatogoryFromBaseProductsJSONFile(newProducts, baseProductsMapDictionary);

            //implement Update UserJson File with new products
            //impelemet Read updated user Json File

            //



            //Append the Newly Semi - Catogorized Products into UserProducts.Json
            //Design Would you like to save - Dialog in future
         /*   Writer writer = Files.newBufferedWriter(Paths.get(getApplicationContext().getFilesDir() + "/UserProducts.json"));
            Log.i("AppTest_JRW : Json ", "Executing : writeObjectsToUserJSONFile" +
                    Paths.get(getApplicationContext().getFilesDir() + "/UserProducts.json"));
            // create Gson instance with pretty-print
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Type listProductType = new TypeToken<List<Product>>() {}.getType();
            gson.toJson(newProducts,listProductType, writer);

             writer = Files.newBufferedWriter(Paths.get(getApplicationContext().getFilesDir() + "/UserProducts.json"));
            JsonReadWrite.writeObjectsToUserJSONFile(newProducts, writer);
*/
 /*           Gson gson = new Gson();
            Type listProductType = new TypeToken<List<Product>>() {}.getType();


            Writer writer = Files.newBufferedWriter(Paths.get(context.getExternalFilesDir(null) + "/UserProducts.json"));
            JsonReadWrite.writeObjectsToUserJSONFile(newProducts, writer);
*/
            /*File path = context.getExternalFilesDir(null);
            File file = new File(path, filename);
            try {

                om.writerWithDefaultPrettyPrinter().writeValue(file, carList);

            } catch (IOException e) {
                e.printStackTrace();
            }*/
/*
            JSONObject obj = new JSONObject();
            obj.put("Name", "Crunchify.com");
            obj.put("Author", "App Shah");
             FileWriter file;
            file = new FileWriter(String.valueOf(Paths.get(getApplicationContext().getFilesDir() + "/UserProducts.json")));
          //  file.write(obj.toJSONString());

            java.lang.Appendable Writer2 = new FileWriter(String.valueOf(Paths.get(getApplicationContext().getFilesDir() + "/UserProducts.json")));
            gson.toJson(newProducts,listProductType, Writer2);
*/

 /*           JSONObject main = new JSONObject();
            JSONParser jsonParser = new JSONParser();

            try {
                Object obj = jsonParser.parse(new FileReader("D:\\student.json"));
                JSONArray jsonArray = (JSONArray)obj;

                System.out.println(jsonArray);

                JSONObject student1 = new JSONObject();
                student1.put("name", "BROCK");
                student1.put("age", new Integer(3));

                JSONObject student2 = new JSONObject();
                student2.put("name", "Joe");
                student2.put("age", new Integer(4));

                jsonArray.add(student1);
                jsonArray.add(student2);

                System.out.println(jsonArray);

                FileWriter file = new FileWriter("D:\\student.json");
                file.write(jsonArray.toJSONString());
                file.flush();
                file.close();
*/

       /*     } catch (ParseException | IOException e) {
                e.printStackTrace();
            }
*/


        } catch (Exception e) {
            Log.e("AppTest_Main : Json ", "Error : " + e.getMessage());
        }

        //Print Products to String
        scannedText = "Product      Price      Category \n"; //Append this in Activity
        for (int i = 0; i < newProducts.size(); i++) {
            Log.i("AppTest_Main : Productdata", "> initial_Items " + i + "\n" + newProducts.get(i));
            System.out.println("AppTest_Main :  > initial_Items " + i + "\n" + newProducts.get(i));
            scannedText = scannedText + newProducts.get(i).toString2();
        }
    }

    public void analyseSpends( List<Product> products ){
        Map<String, Float> catogorySpendsMapDictionary = new HashMap<String, Float>();
        catogorySpendsMapDictionary = spendCatogorize(products);
        
        String spendsStr = "";
        Set<String> keySet = catogorySpendsMapDictionary.keySet();
        for (String key : keySet) {
            spendsStr= spendsStr + key.toString() + "   :   " + catogorySpendsMapDictionary.get(key).toString()+"\n";
        }
        Log.i("AppTest_Main : spendsStr : /n", spendsStr.toString());
        editTextView.setText(spendsStr);
    }

    public Map<String, Float> spendCatogorize(List<Product> products ){
        Map<String, Float>  catogorySpendsMapDictionary = new HashMap<String, Float>();

        for (int i=0;i<products.size();i++) {

          if(!catogorySpendsMapDictionary.containsKey((products.get(i).catogory.toLowerCase()))) {
              catogorySpendsMapDictionary.put(products.get(i).catogory.toLowerCase(), (float) products.get(i).price);
          }else{
              Float catogorySum=catogorySpendsMapDictionary.get(products.get(i).catogory.toLowerCase());
              catogorySum=catogorySum+products.get(i).price;
              catogorySpendsMapDictionary.put(products.get(i).catogory.toLowerCase(), catogorySum );
          }
            //????if(newProducts.contains(products.get(i)))
        }
        Log.i("AppTest_Main : catogorySpendsMapDictionary : /n", catogorySpendsMapDictionary.toString());
        return catogorySpendsMapDictionary;
    }

}
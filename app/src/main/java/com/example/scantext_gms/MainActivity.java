package com.example.scantext_gms;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;

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

        //TODO implement on First Run Method
        //initilizeBaseProductsJSON();
        prefs = getSharedPreferences("com.example.scantext_gms", MODE_PRIVATE);
        if (prefs.getBoolean("firstrun", true)) {
            // Do first run stuff here then set 'firstrun' as false
            // using the following line to edit/commit prefs
            requestPermissions();
            initilizeBaseProductsJSON();
            prefs.edit().putBoolean("firstrun", false).commit();
        }

        //getItemsFromText_btn.callOnClick();

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
            Log.i("AppTest_Main : Paths : System-Data", is.toString() + "  " + destinationSystem.toString());
            JsonReadWrite.copyFileUsingStream(is, destinationSystem);

//context.getExternalFilesDir()

            InputStream is2 = getAssets().open("BaseProducts.json");
            File directory_documents = Environment.getExternalStoragePublicDirectory("DIRECTORY_DOCUMENTS");//Creates a public directory for app.
            Log.i("AppTest_Main : Paths", is.toString() + "Executing getApplicationContext().getExternalFilesDir(null) ");
            File destination = new File(getApplicationContext().getExternalFilesDir(null) + "/BaseProducts.json");
            Log.i("AppTest_Main : Paths", is.toString() + "  " + destination.toString());
            JsonReadWrite.copyFileUsingStream(is2, destination);


            //UserProducts.json Initilize
            InputStream is3 = getAssets().open("UserProducts.json");
            destinationSystem = new File(getApplicationContext().getFilesDir() + "/UserProducts.json");
            Log.i("AppTest_Main : Paths : System-Data", is.toString() + "  " + destinationSystem.toString());
            JsonReadWrite.copyFileUsingStream(is3, destinationSystem);

            InputStream is4 = getAssets().open("UserProducts.json");
            directory_documents = Environment.getExternalStoragePublicDirectory("DIRECTORY_DOCUMENTS");//Creates a public directory for app.
            Log.i("AppTest_Main : Paths", is.toString() + "Executing getApplicationContext().getExternalFilesDir(null) ");
            destination = new File(getApplicationContext().getExternalFilesDir(null) + "/UserProducts.json");
            Log.i("AppTest_Main : Paths", is.toString() + "  " + destination.toString());
            JsonReadWrite.copyFileUsingStream(is4, destination);

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





      /*      Writer writer = Files.newBufferedWriter(Paths.get(getApplicationContext().getExternalFilesDir("DIRECTORY_DOCUMENTS") + "/UserProducts.json"));
            Log.i("AppTest_Main : " , writer.toString());
            JsonReadWrite.writeObjectsToUserJSONFile(newProducts, writer);
*/
            writeToJsonFile(newProducts);

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
        //Print Products Scanned along with their Categories
        //Print Products to String
        String newProductsJSONString = "";
        scannedText = "Product                         Price     Category \n"; //Append this in Activity
        for (int i = 0; i < newProducts.size(); i++) {
            //Log.i("AppTest_Main : Productdata", "> initial_Items " + i + "\n" + newProducts.get(i));
            // System.out.println("AppTest_Main :  > initial_Items " + i + "\n" + newProducts.get(i));
            scannedText = scannedText + newProducts.get(i).toString2();
            // System.out.format("%32s%10d%16s", newProducts);
            newProductsJSONString = newProductsJSONString + newProducts.get(i).toString() + ",\n";
        }

        Log.v("AppTest_Main : Json ", "Data :\n\n newProductsJSONString\n\n" + newProductsJSONString);
        Log.v("AppTest_Main : Json ", "Data : \n\n setText-Output \n\n" + scannedText);
    }

    public void analyseSpends(List<Product> products) {
        Map<String, Float> catogorySpendsMapDictionary = new HashMap<String, Float>();
        catogorySpendsMapDictionary = spendCatogorize(products);

        String spendsStr = "";
        Set<String> keySet = catogorySpendsMapDictionary.keySet();
        for (String key : keySet) {
            spendsStr = spendsStr + key.toString() + "   :   " + catogorySpendsMapDictionary.get(key).toString() + "\n";
        }
        Log.i("AppTest_Main : spendsStr : /n", spendsStr.toString());
        editTextView.setText(spendsStr);
    }

    public Map<String, Float> spendCatogorize(List<Product> products) {
        Map<String, Float> catogorySpendsMapDictionary = new HashMap<String, Float>();

        for (int i = 0; i < products.size(); i++) {

            if (!catogorySpendsMapDictionary.containsKey((products.get(i).catogory.toLowerCase()))) {
                catogorySpendsMapDictionary.put(products.get(i).catogory.toLowerCase(), (float) products.get(i).price);
            } else {
                Float catogorySum = catogorySpendsMapDictionary.get(products.get(i).catogory.toLowerCase());
                catogorySum = catogorySum + products.get(i).price;
                catogorySpendsMapDictionary.put(products.get(i).catogory.toLowerCase(), catogorySum);
            }
            //????if(newProducts.contains(products.get(i)))
        }
        Log.i("AppTest_Main : catogorySpendsMapDictionary : /n", catogorySpendsMapDictionary.toString());
        return catogorySpendsMapDictionary;
    }


    public void writeToJsonFile(List<Product> myProducts) throws IOException {
        // writeObjectsToUserJSONFile(newProducts);
        writemethod2(myProducts);
    }

    public void writeObjectsToUserJSONFile(List<Product> newProducts) {
        Log.i("AppTest_JRW : Json ", "\n\nExecuting : writeObjectsToUserJSONFile : : Start");
  /*      for (int i = 0; i < newProducts.size(); i++) {
            Log.v("AppTest_ : Json ", newProducts.get(i).name);
        }*/

        try {
            Writer myWriter = Files.newBufferedWriter(Paths.get(getApplicationContext().getExternalFilesDir("DIRECTORY_DOCUMENTS") + "/UserProducts.json"));
            Log.i("AppTest_Main : ", "\nWriter\n" + myWriter.toString() + "\n\n getApplicationContext().getExternalFilesDir(\"DIRECTORY_DOCUMENTS\")\n" + getApplicationContext().getExternalFilesDir("DIRECTORY_DOCUMENTS"));

            Gson gson = new Gson();
            Type listProductType = new TypeToken<List<Product>>() {
            }.getType();
            String json = new Gson().toJson(newProducts);
            Log.v("AppTest_JRW : Json ", "writeObjectsToUserJSONFile JsonFormat\n\n" + json);
            Log.v("AppTest_JRW : Json Type ", listProductType.toString());
            // Log.v("AppTest_ : Json Type ", newProducts.getType());
            gson.toJson(json, myWriter);
            //  gson.toJson(newProducts, listProductType, writer);
        } catch (Exception e) {
            Log.e("AppTest_JRW : Json ", "Error : writeObjectsToUserJSONFile" + e.getMessage());
        }
        Log.v("AppTest_JRW : Json ", "Executed : writeObjectsToUserJSONFile : : End");
    }

    public void writemethod2(List<Product> myProducts) throws IOException {

        Log.v("AppTest_JRW : Json ", "\n\n" + getApplicationContext().getExternalFilesDir("DIRECTORY_DOCUMENTS") + "/UserProducts.json");

        Writer writer = new FileWriter(getApplicationContext().getExternalFilesDir("DIRECTORY_DOCUMENTS") + "/UserProducts.json");
        new Gson().toJson(myProducts, writer);
        writer.close();
/*
        Writer writer2 = new FileWriter(getApplicationContext().getExternalFilesDir("DIRECTORY_DOCUMENTS") + "/UserProducts.json");
        new Gson().toJson(myProducts, writer2);
        writer2.close();

        Writer writer3 = Files.newBufferedWriter(Paths.get(getApplicationContext().getExternalFilesDir("DIRECTORY_DOCUMENTS") + "/UserProducts.json"));
        Gson gson = new Gson();
        gson.toJson(myProducts, writer3);
        writer3.close();

        Writer writer4 = new FileWriter(getApplicationContext().getExternalFilesDir("DIRECTORY_DOCUMENTS") + "/UserProducts.json");
        new Gson().toJson(myProducts, writer4);
        writer4.close();

        Writer writer5 = Files.newBufferedWriter(Paths.get("UserProducts.json"));
        // convert user object to JSON file
        Gson gson2 = new Gson();
        gson2.toJson(myProducts, writer5);

        // close writer
  //    writer.close();

        // close the writer
*/

    }

    public void requestPermissions() {

        checkPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
        checkPermission(Manifest.permission.CAMERA,
                CAMERA_PERMISSION_CODE);

    }

    public void checkPermission(String permission, int requestCode) {

        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(
                MainActivity.this,
                permission)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat
                    .requestPermissions(
                            MainActivity.this,
                            new String[]{permission},
                            requestCode);
        } else {
            Toast
                    .makeText(MainActivity.this,
                            "Permission already granted",
                            Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super
                .onRequestPermissionsResult(requestCode,
                        permissions,
                        grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this,
                        "Camera Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(MainActivity.this,
                        "Camera Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this,
                        "Storage Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(MainActivity.this,
                        "Storage Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
}

package com.example.scantext_gms;

import android.util.Log;
import android.os.Bundle;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
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
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.*;

public class JsonReadWrite {

    public static void copyFileUsingStream(InputStream source, File dest) throws IOException {
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

    public static List<Product> getCatogoryFromBaseProductsJSONFile(List<Product> products, Map<String, Product> mapDictionary) {
        //getCatogoryFromBaseProductsJSONFile - Assigns catogory of known products to products Object.
        for (int i = 0; i < products.size(); i++) {

            if (mapDictionary.containsKey(products.get(i).name.toLowerCase())) {
                products.get(i).catogory = (mapDictionary.get(products.get(i).name.toLowerCase())).catogory;
            }
        }
        return products;
    }

    public static Map<String, Product> convertObjectsoDict(List<Product> products) {
        //convertObjectsoDict - Converts baseObjectsToDictionary

        //List<Product> newProducts = BillFilter.FilterBill(scannedText);
        Map<String, Product> newMapDictionary = new HashMap<String, Product>();

        for (int i = 0; i < products.size(); i++) {
            Product temp = products.get(i);
            Log.i("AppTest_Main temp : ", temp.name);

            newMapDictionary.put(products.get(i).name.toLowerCase(), temp);
            Log.i("AppTest_Main : DictionaryValues", newMapDictionary.get(products.get(i).name.toLowerCase()).name);
        }
        return newMapDictionary;
    }

    //Read - Write Methods
    public static List<Product> getObjectsFrombaseJSONFile(Reader reader) {
        //getObjectsFrombaseJSONFile - Converts base JSON String to Objects
        List<Product> baseProducts = new ArrayList<Product>();
        try {
            Gson gson = new Gson();
            //  Log.i("AppTest_Main: Productdata", baseProductsjJsonFileString);
            Type listProductType = new TypeToken<List<Product>>() {
            }.getType();
            baseProducts = gson.fromJson(reader, listProductType);

        } catch (Exception e) {
            Log.e("AppTest_JRW : Json ", "Error : getObjectsFrombaseJSONFile" + e.getMessage());
        }
        return baseProducts;
    }

    public static List<Product> getObjectsFromUserJSONFile() {
        try {

        } catch (Exception e) {
        }
        return null;
    }

    public static void writeObjectsToBaseJSONFile() {
        try {

        } catch (Exception e) {
        }
    }

    public static void writeObjectsToUserJSONFile(List<Product> newProducts,Writer writer) {
        Log.i("AppTest_JRW : Json ", "Executing : writeObjectsToUserJSONFile");
        try {
            String json = new Gson().toJson(newProducts);
            Log.i("AppTest_JRW : Json ", "writeObjectsToUserJSONFile Json Data \n\n" + json);

 /*           for(int i=0; i<newProducts.size();i++)
            {
                Log.v("AppTest_ : Json ", newProducts.get(i).name);
            }
   */
            Gson gson = new Gson();
            Type listProductType = new TypeToken<List<Product>>() {}.getType();
            Log.v("AppTest_ : Json Type ", listProductType.toString());
          // Log.v("AppTest_ : Json Type ", newProducts.getType());


            gson.toJson(newProducts,listProductType, writer);
        } catch (Exception e) {
            Log.e("AppTest_JRW : Json ", "Error : writeObjectsToUserJSONFile" + e.getMessage());
        }
    }

    public static void appendAndWriteObjectsToUserJSONFile( ) {

        try {


        } catch (Exception e) {
        }
    }
}
//  String baseProductsjJsonFileString = getApplicationContext().getFilesDir() + "/BaseProducts.json";
// String baseProductsjJsonFileString = Utils.getJsonFromAssets(getApplicationContext(), "BaseProducts.json");
// String baseProductsjJsonFileString = Utils.getJsonFromAssets(getApplicationContext() , "/BaseProducts.json");
//  FileWriter file = new FileWriter("BaseProducts.json");

//baseProducts = gson.fromJson(baseProductsjJsonFileString, listProductType);
//Log.i("AppTest_Main: Productdata", baseProducts.get(0).toString2());


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

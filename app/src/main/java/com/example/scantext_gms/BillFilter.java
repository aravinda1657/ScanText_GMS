package com.example.scantext_gms;

import android.os.Environment;
import android.util.Log;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.jar.JarEntry;

public class BillFilter {
    public static List<Product> FilterBill(String scannedText) {
        System.out.println("Hi");

        String billString = scannedText;

/*        String billString = "BAWARCHI\n" +
                "    RTC\n" +
                "    HYDERABAD\n" +
                "    9944994499\n" +
                "    ORDER: 741\n" +
                "    DINE IN\n" +
                "    HOST: B0B\n" +
                "    06/07/20\n" +
                "    11:45 AM\n" +
                "    OTY\n" +
                "    ITEM\n" +
                "    PRICE\n" +
                "    1 CHICKEN BIRYANI\n" +
                "    2 CHICKEN MASALA\n" +
                "    4 BUTTER NAAN\n" +
                "    RS 250\n" +
                "    RS 620\n" +
                "    RS 160\n" +
                "    CASH\n" +
                "    SALE\n" +
                "    SUBTOTAL\n" +
                "    GST\n" +
                "    TOTAL\n" +
                "    RS 1030.00\n" +
                "    RS 41.20\n" +
                "    RS 1071.20\n" +
                "    SALE\n" +
                "    APPROVED\n" +
                "    TRANSACTION TYPE\n" +
                "    AUTHORIZATION:\n" +
                "    PAYMENT CODE:\n" +
                "    PAYMENT ID:\n" +
                "    CARD READER\n" +
                "    + TIP:\n" +
                "    = TOTAL\n" +
                "    CUSTOMER COPY\n" +
                "    THANKS FOR VISITING\n" +
                "    BAWARCHI";

 */

        String[] billStringArray = billString.split("\n");
        List<Product> products = new ArrayList<Product>();

        for (int i = 0; i < billStringArray.length; i++) {
            System.out.println(billStringArray[i]);
        }

        try {
            int itemsNamesIdentifier = 0, priceIdentifier = 0;
            List<String> items = new ArrayList<String>();
            List<Integer> prices = new ArrayList<Integer>();


            for (int i = 0; i < billStringArray.length; i++) {

                System.out.println("Test : Data:String :" + billStringArray[i] + "itemsIdentifier=" + itemsNamesIdentifier + "priceIdentifier =" + priceIdentifier);

                if (billStringArray[i].toLowerCase().contains("price")) {
                    itemsNamesIdentifier = 1;
                    priceIdentifier = 0;
                } else if (billStringArray[i].toLowerCase().contains("rs")) {
                    priceIdentifier = 1;
                    itemsNamesIdentifier = 0;
                } else if (priceIdentifier == 1) {
                    break;
                } else {
                    priceIdentifier = 0;
                }


                if ((itemsNamesIdentifier == 1) && !billStringArray[i].toLowerCase().contains("price")) {
                    items.add(billStringArray[i].substring(billStringArray[i].trim().indexOf(" ")));

                } else if (priceIdentifier == 1) {
                    billStringArray[i]=billStringArray[i].substring(0,billStringArray[i].indexOf("."));
                    prices.add(Integer.parseInt(billStringArray[i].toLowerCase().replace("rs", "").trim()));
                }

            }

            for (int i = 0; i < items.size(); i++) {
                Product temp = new Product();
                temp.name = items.get(i);
                temp.price = prices.get(i);
                temp.catogory = "Unknown";
                products.add(temp);
                System.out.println("AppTest_BF :  added " + i + "\n" + products.get(i));
            }
            System.out.println("AppTest_BF :  added all Products");


            for (int i = 0; i < products.size(); i++) {
                Log.i("AppTest_BF : Productdata", "> initial_Items " + i + "\n" + products.get(i));
                System.out.println("AppTest_BF :  > initial_Items " + i + "\n" + products.get(i));
            }


        } catch (Exception e) {
            Log.e("AppTest_BF : dataError", e.getMessage());
        }

        return products;
    }

}
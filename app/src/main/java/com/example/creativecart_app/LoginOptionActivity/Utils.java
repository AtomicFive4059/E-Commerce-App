package com.example.creativecart_app.LoginOptionActivity;

import android.content.Context;
import android.text.format.DateFormat;
import android.widget.Toast;

import androidx.annotation.NonNull;


import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

//A class that will contain static function,constant variable that we will be used in whole application
public class Utils {

    public static final String ADS_STATUS_AVAILABLE="AVAILABLE";
    public static final String ADS_STATUS_SOLD="SOLD";

    //Categories arrays of the Ads
    public static final String[] categories={

            "Mobiles",
            "Computer/Laptop",
            "Electronics & Home Appliances",
            "Vehicle",
            "Furniture & Home Decor",
            "Fashion And Beauty",
            "Books",
            "Sports",
            "Animals",
            "Business",
            "Agriculture"
    };

    //Condition arrays of the Ads
    public static final String[] condition={"New","Used","Refurbished"};

    /* A Function to show Toast
    * @param context the context of the activity/fragment from where this function will be called
    * @param message the message to show in the Toast.*/
   public   static void toast(Context context,String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }


    /* A Function to get current timestap
    * @return Return the current timestamp as long datatype  */
    public  static long getTimestap(){
        return System.currentTimeMillis();
    }


    /* A Function to show toast @param timestamp the timestamp of type Long that we need to format to dd/mm/yyyy @return timestamp formatted to  date dd/mm/yyyy */

    public static String formatTimestampDate(Long timestamp){
        Calendar calendar=Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(timestamp);

        String date= DateFormat.format("dd/mm/yyyy",calendar).toString();
        return date;
    }
}

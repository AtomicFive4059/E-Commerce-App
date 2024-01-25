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

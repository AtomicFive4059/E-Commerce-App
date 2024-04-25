package com.example.creativecart_app.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.creativecart_app.databinding.NoInternetConnectionLayoutBinding;

public class noInternet extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        //if connection is off
        if (!isNetworkConnted(context)){
           // Toast.makeText(context, "Please check network", Toast.LENGTH_LONG).show();

            //NoInternetConnectionLayoutBinding is like a ViewBinding for initiating AlertDialog controls
            NoInternetConnectionLayoutBinding binding = NoInternetConnectionLayoutBinding
                    .inflate(LayoutInflater.from(context));

            //setting AlertDialog to the Context
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            //set the layout view to the AlertDialog
            builder.setView(binding.getRoot());
            //if tryAgainBtn clicked, then dismissed the AlertDialog
            builder.setCancelable(false);
            Dialog dialog = builder.create();
            //to show AlertDialog
            dialog.show();

            //Handling tryAgainBtn clicked event and dismissed theAlertDialog
            binding.tryAgainBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //if connection is on
                    if (isNetworkConnted(context)){
                        dialog.dismiss();
                    }
                }
            });

        }
    }

    private boolean isNetworkConnted(Context context){
        try {
            ConnectivityManager connectivityManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}

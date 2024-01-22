package com.example.creativecart_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.creativecart_app.Fragment.AccountsFragment;
import com.example.creativecart_app.Fragment.ChatsFragment;
import com.example.creativecart_app.Fragment.HomeFragment;
import com.example.creativecart_app.Fragment.MyAdsFragment;
import com.example.creativecart_app.LoginOptionActivity.LoginActivity;
import com.example.creativecart_app.LoginOptionActivity.Utils;
import com.example.creativecart_app.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //activity_main.xml = ActivityMainBinding
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //get instance of the firebase auth for firebase auth related task
        firebaseAuth=FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser()==null){
            //user is not logged in, move to LoginActivity
           startLoginOption();
        }

        //by default (when app is open) show HomeFragment
        showHomeFragment();

        //handle bottomNv item clicks to navigiates between fragment
        binding.bottomNv.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                //get id of the menu iten clicked
                int itemId = item.getItemId();


                if(itemId==R.id.menu_home){
                    //home item clicke,show HomeFragment
                    showHomeFragment();
                    return true;

                } else if (itemId==R.id.menu_chats) {
                    //chat item clicked, show ChatFragment
                    if (firebaseAuth.getCurrentUser()==null){
                        Utils.toast(MainActivity.this,"Login Required...");
                        startLoginOption();
                        return false;
                    }else {
                        showChatsFragment();
                        return true;
                    }

                } else if (itemId==R.id.menu_my_ads) {
                    //myAds item clicked, show MyAdsFragment

                    if (firebaseAuth.getCurrentUser()==null){
                        Utils.toast(MainActivity.this,"Login Required...");
                        startLoginOption();
                        return false;
                    }else {
                        showMyAdsFragment();
                        return true;
                    }

                } else if (itemId==R.id.menu_account) {
                    //account item clicked, show AccountFragment

                    if (firebaseAuth.getCurrentUser()==null){
                        Utils.toast(MainActivity.this,"Login Required...");
                        startLoginOption();
                        return false;
                    }else {
                        showAccountFragment();
                        return true;
                    }

                }else {


                    return false;
                }
            }
        });
    }

    private void showHomeFragment() {
        //change toolbar textview text/title to Home
      //  binding.toolbarTitleTv.setText("Home");

        //show fragment
        HomeFragment homeFragment=new HomeFragment();
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(binding.fragmentsFl.getId(),homeFragment,"HomeFragment");
        fragmentTransaction.commit();
    }
    private void showChatsFragment(){
        //change toolbar textview text/title to Chats
       // binding.toolbarTitleTv.setText("Chats");
        ChatsFragment chatsFragment=new ChatsFragment();
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(binding.fragmentsFl.getId(),chatsFragment,"ChatsFragment");
        fragmentTransaction.commit();

    }
    private void showMyAdsFragment(){
        //change toolbar textview text/title to MyAds
     //   binding.toolbarTitleTv.setText("MyAds");

        //show fragment
        MyAdsFragment myAdsFragment=new MyAdsFragment();
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(binding.fragmentsFl.getId(),myAdsFragment,"MyAdsFragment");
        fragmentTransaction.commit();

    }
    private void showAccountFragment(){
        //change toolbar textview text/title to Account
      //  binding.toolbarTitleTv.setText("Account");

        //show fragment
        AccountsFragment accountsFragment=new AccountsFragment();
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(binding.fragmentsFl.getId(),accountsFragment,"AccountFragment");
        fragmentTransaction.commit();
    }
    private void startLoginOption(){
        //MainActivity to LoginActivity
        startActivity(new Intent(this, LoginActivity.class));
    }
}
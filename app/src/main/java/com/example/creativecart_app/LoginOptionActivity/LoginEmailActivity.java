package com.example.creativecart_app.LoginOptionActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import com.example.creativecart_app.MainActivity;
import com.example.creativecart_app.databinding.ActivityLoginEmailBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginEmailActivity extends AppCompatActivity {

    ActivityLoginEmailBinding activityLoginEmailBinding;

    private static final String TAG="LOGIN_TAG";
    private ProgressDialog progressDialog;

    //Firebase Auth for auth related task.
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //activity_login_email.xml = ActivityLoginEmailBinding
        activityLoginEmailBinding=ActivityLoginEmailBinding.inflate(getLayoutInflater());
        setContentView(activityLoginEmailBinding.getRoot());

        //get instance of firebase auth for auth related task
        firebaseAuth=FirebaseAuth.getInstance();

        //initiate setup ProgressDialog
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please wait...!!");
        progressDialog.setCanceledOnTouchOutside(false);

        //Handle toolbarBackBtn button to go back
        activityLoginEmailBinding.toolbarBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //handle noAccountTv click, open RegisterEmailActivity to register user with email and password
        activityLoginEmailBinding.noAccountTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginEmailActivity.this, RegisterEmailActivity.class));
            }
        });

        //handle loginBtn click, start login
        activityLoginEmailBinding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validateData();

            }
        });
    }

    private String email,password;

    private void validateData(){

        //input data
        email=activityLoginEmailBinding.emailEt.getText().toString().trim();
        password=activityLoginEmailBinding.passwordEt.getText().toString();

        Log.d(TAG,"ValidateData: email: "+email);
        Log.d(TAG,"ValidateData: Password: "+password);

        //validate data
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){

            //email pattern is Invalid, show error
            activityLoginEmailBinding.emailEt.setError("Invalid Email..");
            activityLoginEmailBinding.emailEt.requestFocus();
        } else if (password.isEmpty()) {

            //password is not entered, show error
            activityLoginEmailBinding.passwordEt.setError("Enter Password...");
            activityLoginEmailBinding.passwordEt.requestFocus();
        }else {
            //email pattern is valid and password is entered
            loginUser();
        }
    }

    private void loginUser(){
        //show progress..
        progressDialog.setMessage("Login In");
        progressDialog.show();

        //start user login
        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        //user login success
                        Log.d(TAG,"onSuccess: Logged In...");
                        progressDialog.dismiss();

                        //start MainActivity
                        startActivity(new Intent(LoginEmailActivity.this, MainActivity.class));
                        finishAffinity();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //user login failed
                        Log.e(TAG,"onFailure: ",e);
                        Utils.toast(LoginEmailActivity.this,"Failed due to "+e.getMessage());
                        progressDialog.dismiss();
                    }
                });
    }
}
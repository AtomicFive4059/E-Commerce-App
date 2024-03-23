package com.example.creativecart_app.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import com.example.creativecart_app.LoginOptionActivity.Utils;
import com.example.creativecart_app.databinding.ActivityForgotPasswordBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.sessions.LogEnvironment;

public class ForgotPasswordActivity extends AppCompatActivity {

    //ViewBing
    private ActivityForgotPasswordBinding binding;
    //TAG for logs in logcat
    private static final String TAG="FORGOT_PASS_TAG";
    //Firebase Auth for auth related task
    private FirebaseAuth firebaseAuth;
    //ProgressDialog to show while sending password recovery instruction
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //init View Binding --> activity_forgot_password.xml=ActivityForgotPasswordBinding
        binding=ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot() );

        //ProgressDialog to show while sending password recovery instruction
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCanceledOnTouchOutside(false);

        //get instance of FirebaseAuth to Auth related task
        firebaseAuth=FirebaseAuth.getInstance();

        //Handle toolbarBackBtn click, to go back
        binding.toolbarBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //Handle submitBtn click, validate data to start password recovery
        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });

    }

    private String email="";

    private void validateData() {
        Log.d(TAG, "validateData: ");

        //input data
        email=binding.emailEt.getText().toString().trim();

        Log.d(TAG, "validateData: Email: "+email);

        //Validate data
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //invalid email patterns, show error in emailEt
            binding.emailEt.setError("Invalid Email Patterns..");
            binding.emailEt.requestFocus();
        }else {
            //Email patterns is valid, send password recovery instruction
            sendPasswordRecoveryInstructions();
        }

    }

    private void sendPasswordRecoveryInstructions(){
        Log.d(TAG, "sendPasswordRecoveryInstructions: ");

        //show progressDialog
        progressDialog.setMessage("Sending Password Recovery Instruction To "+email);
        progressDialog.show();

        //send password recovery instruction, pass the input email as param
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        //instruction sent, check email. Sometimes it goes in the spam folder, so if not in inbox check the your spam folder
                        progressDialog.dismiss();
                        Utils.toast(ForgotPasswordActivity.this,"Instructoin To Reset Password Sent To "+email);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Failed to sent instruction
                        Log.e(TAG, "onFailure: ",e);
                        progressDialog.dismiss();

                        Utils.toast(ForgotPasswordActivity.this,"Failed To Send Due To "+e.getMessage());
                    }
                });
    }
}
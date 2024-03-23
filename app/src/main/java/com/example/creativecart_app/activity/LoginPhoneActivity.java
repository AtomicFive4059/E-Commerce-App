package com.example.creativecart_app.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.creativecart_app.LoginOptionActivity.Utils;
import com.example.creativecart_app.databinding.ActivityLoginPhoneBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class LoginPhoneActivity extends AppCompatActivity {

    private ActivityLoginPhoneBinding binding;

    //ProgressDialog to show, while phone login
    private ProgressDialog progressDialog;

    //Firebase Auth for auth related task
    private FirebaseAuth firebaseAuth;
    private PhoneAuthProvider.ForceResendingToken forceResendingToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    private static final String TAG= "LOGIN_PHONE_TAG";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //activity_login_phone.xml=ActivityLoginPhoneBinding
        binding=ActivityLoginPhoneBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //For the start show, phone input UI and hide OTP UI
        binding.phoneInputRl.setVisibility(View.VISIBLE);
        binding.optInputRl.setVisibility(View.GONE);

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);


        firebaseAuth=FirebaseAuth.getInstance();

        phoneLoginCallBack();

        //handle click, to goback
        binding.toolbarBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();

            }
        });


        binding.sendOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });

        binding.resendotpTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                resendVerificationCode(forceResendingToken);
            }
        });

        binding.verifyOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String otp=binding.otpEt.getText().toString().trim();

                Log.d(TAG, "onClick: OTP: "+otp);

                if (otp.isEmpty()){
                   binding.otpEt.setError("Enter OTP..");
                   binding.otpEt.requestFocus();
                } else if (otp.length()<6) {
                    binding.otpEt.setError("OTP must be 6 Digit..");
                    binding.otpEt.requestFocus();
                }else {

                    verifyPhoneNumberWithCode(mVerificationId,otp);
                }
            }
        });
    }

    private String phoneCode="",phoneNumber="",phoneNumberWithCode="";

    private void validateData(){

        //Input data
        phoneCode=binding.phoneCodeTil.getSelectedCountryCodeWithPlus();
        phoneNumber=binding.phoneNumberEt.getText().toString().trim();
        phoneNumberWithCode=phoneCode+phoneNumber;

        Log.d(TAG, "validateData: phoneCode  "+phoneCode);
        Log.d(TAG, "validateData: phoneNumber "+phoneNumber);
        Log.d(TAG, "validateData: getPhoneNumberWithCode"+phoneNumberWithCode);

        //Phone number is not enter, show this error
        if (phoneNumber.isEmpty()){
            Utils.toast(this,"Please Enter Phone Number..!!");
        }else {
            startPhoneNumberVerificaton();
        }
    }

    //show progress
   private void startPhoneNumberVerificaton(){
    progressDialog.setMessage("Sending OTP to "+phoneNumberWithCode);
    progressDialog.show();

       PhoneAuthOptions phoneAuthOptions=PhoneAuthOptions.newBuilder(firebaseAuth)  //Firebase instance
               .setPhoneNumber(phoneNumberWithCode)                                 //Phone Number with Country Code e.g:-+91 xxxxxxxxxx
               .setTimeout(120L, TimeUnit.SECONDS)                            //TimeOut and Unit
               .setActivity(this)                                                   //Activity (For callback binding)
               .setCallbacks(mCallbacks)
               .build();

       PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions);

   }

    private void phoneLoginCallBack() {

        Log.d(TAG, "phoneLoginCallBack: ");
        mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {

                // This callback will be invoked in two situation
                /*1- Instant verification. In some cases the phone number can be instantly verified, withought needing to send or enter a verification code */
                /*2- Auto retrieval. On some devices Google Play Service can automatically detect the upcoming Verification SMS and perform Verification Action, without User Action */
                Log.d(TAG, "onVerificationCompleted: ");

                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

                //This callback is involved in a invalid request for verification is made
                //For instance if the phone number format is not valid
                Log.e(TAG, "onVerificationFailed: ",e);
                progressDialog.dismiss();

                Utils.toast(LoginPhoneActivity.this,""+e.getMessage());
            }


            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {

                /*The SMS verification code has been sent to the provided phone number, we now need to ask user to enter the code and then construct the credential by combining the code with verification id */
                super.onCodeSent(verificationId, token);

                //save verification ID and resending taken,so we can use later them
                mVerificationId=verificationId;
                forceResendingToken=token;

                //OTP is sent, so hide progress for now
                progressDialog.dismiss();

                //OTP is sent, so hide PHONE UI and show OPT UI
                binding.phoneInputRl.setVisibility(View.INVISIBLE);
                binding.optInputRl.setVisibility(View.VISIBLE);

                //show Toast for success sending OTP
                Utils.toast(LoginPhoneActivity.this,"OTP Send to the "+phoneNumberWithCode);

                //show user a message that enter Verification Code sent to the phone number
                binding.loginLableTv.setText("Please enter the Verification Code sent to the "+phoneNumberWithCode);


            }
        };
    }

   private void verifyPhoneNumberWithCode(String verificationId,String opt){

       Log.d(TAG, "verifyPhoneNumberWithCode: Verification: "+verificationId);
       Log.d(TAG, "verifyPhoneNumberWithCode: OTP: "+opt);
        //show progress
        progressDialog.setMessage("Verifying OTP..");
        progressDialog.show();

        //PhoneAuthCredential with Verification Id and OTP to SignIn user with signInWithPhoneAuthCredential
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verificationId,opt);
        signInWithPhoneAuthCredential(credential);
   }


   private void resendVerificationCode(PhoneAuthProvider.ForceResendingToken forceResendingToken){

       Log.d(TAG, "resendVerificationCode: ForceResendingToken "+forceResendingToken);
       //show progress dialog
       progressDialog.setMessage("Resending OTP..");
       progressDialog.show();

       PhoneAuthOptions phoneAuthOptions=PhoneAuthOptions.newBuilder(firebaseAuth)  //Firebase instance
               .setPhoneNumber(phoneNumberWithCode)                                 //Phone Number with Country Code e.g:-+91 xxxxxxxxxx
               .setTimeout(120L, TimeUnit.SECONDS)                            //TimeOut and Unit
               .setActivity(this)                                                   //Activity (For callback binding)
               .setCallbacks(mCallbacks)
               .setForceResendingToken(forceResendingToken)
               .build();

       PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions);
   }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential){

        Log.d(TAG, "signInWithPhoneAuthCredential: ");
        progressDialog.setMessage("Logging In..");

        //SignIn into Firebade Auth using Phone Credential
        firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Log.d(TAG, "onSuccess: ");

                        //Sign success. Let's check if the user is new (New Account Register) or Existing (Existing Login)
                        if (authResult.getAdditionalUserInfo().isNewUser()){
                            
                            //New User,Account Created. Let's save User Info to Firebase realtime database
                            Log.d(TAG, "onSuccess: New User, Account Created..");
                            updateuserInfoDb();
                        }else {
                            Log.d(TAG, "onSuccess: Existing User, Logged In");

                            //Existing User,No need to Save User Info to Firebase realtime database. Start MainActivity directly
                            startActivity(new Intent(LoginPhoneActivity.this, MainActivity.class));
                            finishAffinity();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        //SignIn failed, show Exception message
                        Log.e(TAG, "onFailure: ", e);
                        progressDialog.dismiss();

                        Utils.toast(LoginPhoneActivity.this,"Failed Log In due to "+e.getMessage());
                    }
                });

    }

    private void updateuserInfoDb() {
        Log.d(TAG, "updateuserInfoDb: ");
        progressDialog.setMessage("Saving User Info..");
        progressDialog.show();

        //Let's save User Info to Firebase Realtime database key name should be same as we done in the Register via Email and Google

        long timestamp = Utils.getTimestap();
        String registerUserId = firebaseAuth.getUid();

        //setup data to save in firebase realtime db. most of the data will be empthy and will set in edit profile
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("name","");
        hashMap.put("phoneCode",""+phoneCode);
        hashMap.put("phoneNumber",""+phoneNumber);
        hashMap.put("profileImageUrl","");
        hashMap.put("dob","");
        hashMap.put("userType","Phone");   //possible value Email/Phome/Google
        hashMap.put("typingTo","");
        hashMap.put("timestamp",timestamp);
        hashMap.put("onlineStatus",true);
        hashMap.put("email","");
        hashMap.put("uid",registerUserId);

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
        reference.child(registerUserId)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        //User Info saved success
                        Log.d(TAG, "onSuccess: User Info Saved");
                        progressDialog.dismiss();

                        //start MainActivity
                        startActivity(new Intent(LoginPhoneActivity.this,MainActivity.class));
                        finishAffinity();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.e(TAG, "onFailure: ",e);
                        progressDialog.dismiss();
                        Utils.toast(LoginPhoneActivity.this,"Failed to save User Info due to "+e.getMessage());
                    }
                });

    }


}
package com.example.creativecart_app.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.example.creativecart_app.LoginOptionActivity.Utils;
import com.example.creativecart_app.R;
import com.example.creativecart_app.databinding.ActivityLoginBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    LottieAnimationView lottieAnimationView;

    ActivityLoginBinding binding;
    private static final String TAG = "LOGIN_OPTION_TAG";
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //activity_login.xml = ActivityLoginBinding
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait..");
        progressDialog.setCanceledOnTouchOutside(false);


        firebaseAuth = FirebaseAuth.getInstance();


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        googleSignInClient = GoogleSignIn.getClient(this, gso);


        //Handled close button click, and go back.
        binding.SkipCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //handle loginEmailBtn click,open LoginEmailActivity to login with email and password
        binding.loginEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, LoginEmailActivity.class));
            }
        });

        //handle loginGoogleBtn click,begin google sign-in
        binding.loginGoogleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                beginGoogleLogin();
            }
        });

        binding.loginPhoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(LoginActivity.this, LoginPhoneActivity.class));
            }
        });
    }

    private void beginGoogleLogin() {

        Log.d(TAG, "beginGoogleLogin: ");

        Intent googleSignInIntent = googleSignInClient.getSignInIntent();
        googleSignInARL.launch(googleSignInIntent);
    }

    private ActivityResultLauncher<Intent> googleSignInARL = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.d(TAG, "onActivityResult: ");


                    if (result.getResultCode() == Activity.RESULT_OK) {

                        Intent data = result.getData();
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

                        try {                                     //Google Sign In was successful, authentication with Firebase

                            GoogleSignInAccount account = task.getResult(ApiException.class);
                            Log.d(TAG, "onActivityResult: Account ID " + account.getId());
                            firebaseAuthWithGoogleAccount(account.getIdToken());

                        } catch (ApiException e) {

                            Log.e(TAG, "onActivityResult: ", e);
                        }
                    }
                    //cancel from google sign in options/confirmation dialog
                    else {
                        Log.d(TAG, "onActivityResult: Cancelled");
                        Utils.toast(LoginActivity.this, "Cancelled..");
                    }

                }
            }
    );

    private void firebaseAuthWithGoogleAccount(String idToken) {

        Log.d(TAG, "firebaseAuthWithGoogleAccount: idToken " + idToken);

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        //SignIn into firebase auth using Google Credentials
        firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        //SignIn success, let's check if the user is new (New Account Register) or existing (Existing Login)
                        if (authResult.getAdditionalUserInfo().isNewUser()) {

                            Log.d(TAG, "onSuccess: New User, Account created..");

                            //new user, Account created. Let's save user info to firebase realtime database
                            updateUserInfoDb();
                        } else {

                            Log.d(TAG, "onSuccess: Existing User, Logged In");

                            //new user, Account created. NO need to save user info to firebade realtime database, Start MainActivity
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finishAffinity();

                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.d(TAG, "onFailure: ", e);
                    }
                });
    }

    private void updateUserInfoDb() {

        Log.d(TAG, "updateUserInfoDb: ");

        //set message and shor progress dialog
        progressDialog.setMessage("Saving User Info..");
        progressDialog.show();


        //get current timestamp e.g show user registration date/time
        long timestamp = Utils.getTimestap();
        String registerUserEmail = firebaseAuth.getCurrentUser().getEmail();  //get email of register user
        String registerUserId = firebaseAuth.getUid();                        //get Uid of register user
        String name = firebaseAuth.getCurrentUser().getDisplayName();


        //setup data to save in firebase realtime db. most of the data will be empthy and will set in edit profile
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("name", "" + name);
        hashMap.put("phoneCode", "");
        hashMap.put("phoneNumber", "");
        hashMap.put("profileImageUrl", "");
        hashMap.put("dob", "");
        hashMap.put("userType", "Google");   //possible value Email/Phome/Google
        hashMap.put("typingTo", "");
        hashMap.put("timestamp", timestamp);
        hashMap.put("onlineStatus", true);
        hashMap.put("email", registerUserEmail);
        hashMap.put("uid", registerUserId);


        //set data to firebase db
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(registerUserId)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Log.d(TAG, "onSuccess: User info Save..");
                        progressDialog.dismiss();

                        startActivity(new Intent(LoginActivity.this, MainActivity.class));

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.e(TAG, "onFailure: ", e);
                        progressDialog.dismiss();

                        Utils.toast(LoginActivity.this, "Failed to show user info due to " + e.getMessage());
                    }
                });

    }
}
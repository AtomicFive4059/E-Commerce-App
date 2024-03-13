package com.example.creativecart_app.LoginOptionActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.creativecart_app.R;
import com.example.creativecart_app.databinding.ActivityChangePasswordBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {

    //ViewBing
    private ActivityChangePasswordBinding binding;

    //TAG for logs in logcat
    private static final String TAG="CHANGE_PASS_TAG";

    //ProgressDialog to show while sending password recovery instruction
    private ProgressDialog progressDialog;

    //Firebase Auth for auth related task
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //init View Binding --> activity_change_password.xml=ActivityChangePasswordBinding
        binding=ActivityChangePasswordBinding.inflate(getLayoutInflater());
        //geting Rool of xml file
        setContentView(binding.getRoot());

        //get instance of the firebase auth for Auth related task
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();

        //initiate ProgressDialog to show while changing password
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please Wait..");
        progressDialog.setCanceledOnTouchOutside(false);

        //handle toolbarBackBtn to go back
        binding.toolbarBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //readyBtn click, start validate(); method
        binding.readyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });


    }

   private String currentPassword="";
    private String newPassword="";
    private String confirmNewPassword="";
    private void validateData() {
        Log.d(TAG, "validateData: Current Password "+currentPassword);
        Log.d(TAG, "validateData: New Password "+newPassword);
        Log.d(TAG, "validateData: Confirm New Password "+confirmNewPassword);

        //input data
        currentPassword=binding.currentPasswordEt.getText().toString();
        newPassword=binding.newPasswordEt.getText().toString();
        confirmNewPassword=binding.confirmNewPasswordEt.getText().toString();

        //validate data
        if (currentPassword.isEmpty()){
            //Current Password Filed (currentPasswordEt) is empty, show error in currentPasswordEt
            binding.currentPasswordEt.setError("Please Enter Current Password");
            binding.currentPasswordEt.requestFocus();
        } else if (newPassword.isEmpty()) {
            //New Password Filed (newPasswordEt) is empty, show error in newPasswordEt
            binding.newPasswordEt.setError("Please Enter New Password");
            binding.newPasswordEt.requestFocus();
        } else if (confirmNewPassword.isEmpty()) {
            //Confirm New Password Filed (confirmNewPasswordEt) is empty, show error in confirmNewPasswordEt
            binding.confirmNewPasswordEt.setError("Please Enter Confirmation Password");
            binding.confirmNewPasswordEt.requestFocus();
        } else if (!newPassword.equals(confirmNewPassword)) {
            //password in newPasswordEt and confirmNewPasswordEt does not match, show error in confirmNewPasswordEt
            binding.confirmNewPasswordEt.setError("Password doesn't match");
            binding.confirmNewPasswordEt.requestFocus();
        }else {
            //all data is validated, verify current password is correct first before updating password
            authenticateUserForUpdatePassword();
        }
    }

    private void authenticateUserForUpdatePassword() {
        Log.d(TAG, "authenticateUserForUpdatePassword: ");

        //show progress
        progressDialog.setMessage("Authenticating User");
        progressDialog.show();

        //before changing password, re-authenticate the user to check if the has entered correct current password
        AuthCredential authCredential= EmailAuthProvider.getCredential(firebaseUser.getEmail(),currentPassword);
        firebaseUser.reauthenticate(authCredential)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //Successful Authentication, being update
                       updatePassword();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Failed to Authenticate user, may be wrong current password entered
                        Log.e(TAG, "onFailure: ",e);
                        progressDialog.dismiss();
                        Utils.toast(ChangePasswordActivity.this,"Failed To Authenticate Due To "+e.getMessage());
                    }
                });
    }

    private void updatePassword() {
        Log.d(TAG, "updatePassword: ");

        //show progress
        progressDialog.setMessage("Updating Password ");
        progressDialog.show();

        //being update password, pass the password as parameter
        firebaseUser.updatePassword(newPassword)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //password update success, you may do logout and move to login activity, if you want
                        progressDialog.dismiss();
                        Utils.toast(ChangePasswordActivity.this,"Password Updated Successfully..");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //password update failure,show error message
                        Log.e(TAG, "onFailure: ",e);
                        progressDialog.dismiss();
                        Utils.toast(ChangePasswordActivity.this,"Failed To Update Password Due to "+e.getMessage());
                    }
                });
    }
}
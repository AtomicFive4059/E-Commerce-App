package com.example.creativecart_app.LoginOptionActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.creativecart_app.MainActivity;
import com.example.creativecart_app.R;
import com.example.creativecart_app.databinding.ActivityDeleteAccountBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.units.qual.A;

public class DeleteAccountActivity extends AppCompatActivity {

    //ViewBing
    private ActivityDeleteAccountBinding binding;

    //ProgressDialog to show while deleting user and app data and info
    private ProgressDialog progressDialog;

    //TAG for logs in logcat
    private static final String TAG="DELETE_ACCOUNT_TAG";

    //Firebase Auth for Auth related task
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //activity_delete_account.xml=ActivityDeleteAccountBinding
        binding=ActivityDeleteAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot()); //get root of xml file

        //init/setup progressDialog to while deleting account
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please Wait..");
        progressDialog.setCanceledOnTouchOutside(false);

        //Get Instance of the firebase auth for Auth related task
        firebaseAuth=FirebaseAuth.getInstance();

        //get instance of firebaseUser to get current user and delete
        firebaseUser=firebaseAuth.getCurrentUser();

        //Handle toolbarBackBtn click, to go back
        binding.toolbarBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            onBackPressed();
            }
        });

        //Handle confirmDeleteBtn click, to Delete User Account and App data as well
        binding.confirmDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAccount();
            }
        });
    }

    private void deleteAccount() {
        Log.d(TAG, "deleteAccount: ");

        String myUid=firebaseAuth.getUid();

        //show progress
        progressDialog.setMessage("Deleting User Account..");
        progressDialog.show();

        //step:1 Delete User Account
        firebaseUser.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                         //User Account Deleted
                        Log.d(TAG, "onSuccess: Account Deleted Successfully ");

                        progressDialog.setMessage("Deleting User Ads ");

                        //step:2 Remove User Ads, currently we have not worked Ads, Ads will be saved in DB >Ads>AdID. Each Ads contain Uid of owner
                        DatabaseReference refUserAds= FirebaseDatabase.getInstance().getReference("Ads");
                        refUserAds.orderByChild("uid").equalTo(myUid)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        //thier may be multiple Ads by user need to loop
                                        for (DataSnapshot ds : snapshot.getChildren()){
                                            //delete Ads
                                            ds.getRef().removeValue();
                                        }


                                        progressDialog.setMessage("Deleting User Data..");

                                        //step:2 Remove user data, DB>Users>UserId
                                        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Users");
                                        reference.child(myUid)
                                                .removeValue()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        //Account data deleted
                                                        Log.d(TAG, "onSuccess: User data deleted successfully..");
                                                        startMainActivity();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        //Failed to delete user data, may due to firebase DB rules, we have to make it public since we delete data after account deleted
                                                        Log.e(TAG, "onFailure: ",e);
                                                        progressDialog.dismiss();
                                                        Utils.toast(DeleteAccountActivity.this,"Failed To Delete User Data Due To "+e.getMessage());
                                                        startMainActivity();
                                                    }
                                                });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed to delete user account, may be user need re-login for authentication purpose for deleting
                        Log.e(TAG, "onFailure: ",e);
                        progressDialog.dismiss();
                        Utils.toast(DeleteAccountActivity.this,"Failed To Delete Account Due To "+e.getMessage());
                    }
                });
    }

    private void startMainActivity(){
        Log.d(TAG, "startMainActivity: ");
        startActivity(new Intent(this,MainActivity.class));
        finishAffinity();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finishAffinity();
    }
}
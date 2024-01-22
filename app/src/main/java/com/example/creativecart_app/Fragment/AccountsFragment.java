package com.example.creativecart_app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.creativecart_app.LoginOptionActivity.Utils;
import com.example.creativecart_app.MainActivity;
import com.example.creativecart_app.R;
import com.example.creativecart_app.databinding.FragmentAccountsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountsFragment extends Fragment {

    private FragmentAccountsBinding binding;
    private FirebaseAuth firebaseAuth;
    private Context mContext;

    @Override
    public void onAttach(@NonNull Context context) {
        mContext=context;
        super.onAttach(context);
    }

    public AccountsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentAccountsBinding.inflate(LayoutInflater.from(mContext ),container,false);

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //get instance of the firebase auth for Auth related task
        firebaseAuth=FirebaseAuth.getInstance();

        loadMyInfo();

        //handle logoutBtn click, logout user and start MainActivity
        binding.logoutCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //logout user
                firebaseAuth.signOut();

                //start MainActivity
                startActivity(new Intent(mContext, MainActivity.class));
                getActivity().finishAffinity();
            }
        });
    }

    private void loadMyInfo() {
        //Reference of the current User Info in Firebase Realtime Database to get the User Info
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //get User Info, spelling shoud be as in Firebase realtime database
                        String dob=""+snapshot.child("dob").getValue();
                        String email=""+snapshot.child("email").getValue();
                        String name=""+snapshot.child("name").getValue();
                        String phoneNumber=""+snapshot.child("phoneNumber").getValue();
                        String profileImageUrl=""+snapshot.child("profileImageUrl").getValue();
                        String phoneCode=""+snapshot.child("phoneCode").getValue();
                        String timestap=""+snapshot.child("timestap").getValue();
                        String userType=""+snapshot.child("userType").getValue();

                        //Concatenate phone code and phone number to make full phone number
                        String phone=phoneCode+phoneNumber;

                        //to avoid null or format exception
                        if (timestap.equals("null")){
                            timestap="0";
                        }
                        //format timestamp to dd/mm/yyyy
                        String formatedDate= Utils.formatTimestampDate(Long.parseLong(timestap));

                        //set data to UI
                        binding.emailTV.setText(email);
                        binding.nameTv.setText(name );
                        binding.dobTv.setText(dob);
                        binding.phoneTv.setText(phone);
                        binding.memberSinceTv.setText(formatedDate);

                        //check user type i.e Email/Phone/Google. In case of Phone and Google Account is already Verified, but in case of Email account user have to verify
                        if (userType.equals("Email")){

                            //User type is Email, have to check if verified or not
                            boolean isVerified=firebaseAuth.getCurrentUser().isEmailVerified();

                            if (isVerified){
                                //verified
                                binding.verificationTv.setText("Verified");
                            }else {
                                //not verified
                                binding.verificationTv.setText("Not Verified");
                            }
                        }else {
                            //User type is Google or Phone, no need to check if verified or not as it's already verified
                            binding.verificationTv.setText("Verified");
                        }

                        //set profile image to profileIv
                        Glide.with(mContext)
                                .load(profileImageUrl)
                                .placeholder(R.drawable.account_img)
                                .into(binding.profileIv);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}
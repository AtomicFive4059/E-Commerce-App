package com.example.creativecart_app.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.example.creativecart_app.R;
import com.example.creativecart_app.databinding.FragmentChatsBinding;

public class ChatsFragment extends Fragment {

    String TAG="Cahts";
    FragmentChatsBinding binding;
    private Context context;

    ImageView imageView;

    public ChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding=FragmentChatsBinding.inflate(getLayoutInflater().from(getContext()),container,false);
        return binding.getRoot();

    }
}
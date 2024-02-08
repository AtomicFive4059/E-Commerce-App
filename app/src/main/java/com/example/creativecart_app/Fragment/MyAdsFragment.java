package com.example.creativecart_app.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.creativecart_app.databinding.FragmentMyAdsBinding;
import com.google.android.material.tabs.TabLayout;

public class MyAdsFragment extends Fragment {

    //TAG to show logs in Logcat
    private static final String TAG="MY_ADS_TAG";

    //View Binding
    private FragmentMyAdsBinding binding;

    //Context of this fragment class
    private Context mContext;
    private MyTabsViewPagerAdapter myTabsViewPagerAdapter;

    public MyAdsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        //get and init Context for this fragment class
        mContext=context;
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentMyAdsBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //add the tabs to TabLayout
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("ADS"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Favorites"));

        //FragmentManager. Initializing using getChildFragmentManager() because we are using Tabsn in fragment not activity (in activityn we used getFragmentManager())
        FragmentManager fragmentManager = getChildFragmentManager();
        myTabsViewPagerAdapter = new MyTabsViewPagerAdapter(fragmentManager,getLifecycle());
        binding.viewPager.setAdapter(myTabsViewPagerAdapter);

        //tab selected listener to set current item on view page
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
               //set current item on view page
                binding.viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //change Tab when swiping
        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position));
            }
        });
    }

    public class MyTabsViewPagerAdapter extends FragmentStateAdapter {
        public MyTabsViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            //Tab position start from 0. If 0 set/show MyAds_AdsFragment otherwise it is definitely 1 so show MyAds_FavFragment
            if (position==0) {
                return new MyAds_AdsFragment();
            }else {
                return new MyAds_FavFragment();
            }
        }

        @Override
        public int getItemCount() {
            //return list of the items/tabs
            return 2;
        }
    }
}
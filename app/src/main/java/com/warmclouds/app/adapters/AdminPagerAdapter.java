package com.warmclouds.app.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.warmclouds.app.fragments.BookingsFragment;
import com.warmclouds.app.fragments.NurseriesFragment;

public class AdminPagerAdapter extends FragmentStateAdapter {

    public AdminPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new NurseriesFragment();
            case 1:
                return new BookingsFragment();
            default:
                return new NurseriesFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}



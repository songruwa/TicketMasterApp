package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myapplication.fragments.artist_card;
import com.example.myapplication.fragments.details_card;
import com.example.myapplication.fragments.venue_card;

import java.util.ArrayList;

public class vpAdapter_detail extends FragmentStateAdapter {


    public vpAdapter_detail(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new details_card();
            case 1:
                return new artist_card();
            case 2:
                return new venue_card();
            default:
                return new details_card();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}

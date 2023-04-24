package com.example.myapplication;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myapplication.fragments.Favorite;
import com.example.myapplication.fragments.Search;

import java.util.ArrayList;


//https://www.youtube.com/watch?v=pIKdHeOjYNw
public class MyViewPagerAdapter extends FragmentStateAdapter{
    public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position) {
            case 0:
                return new parent(); // return parent fragment instead
            case 1:
                return new Favorite();
            default:
                return new Search();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

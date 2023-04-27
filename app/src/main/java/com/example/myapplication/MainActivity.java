package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;


public class MainActivity extends AppCompatActivity implements selectListener{

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    MyViewPagerAdapter myViewPagerAdapter;
//    how to use Navigation
//    https://www.youtube.com/watch?v=Ii_BDxYHvuA&t=301s
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;

//    https://www.youtube.com/watch?v=pIKdHeOjYNw
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager2 = findViewById(R.id.view_pager);
        myViewPagerAdapter = new MyViewPagerAdapter(this);
        viewPager2.setAdapter(myViewPagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int potision) {
                super.onPageSelected(potision);
                tabLayout.getTabAt(potision).select();
            }
        });


    }


    @Override
    public void onItemClicked(Context context, Event event) {
// Create an Intent to start the new activity
        Intent intent = new Intent(context, DetailsOfEvent.class);
        // Pass the necessary data using putExtra() methods
        intent.putExtra("eventName", event.getName());
        intent.putExtra("eventVenue", event.getVenue().getName());
        intent.putExtra("eventDate", event.getLocalDate());
        intent.putExtra("eventTime", event.getLocalTime());
        intent.putExtra("eventId", event.getId());
        // Start the new activity
        context.startActivity(intent);
    }
}

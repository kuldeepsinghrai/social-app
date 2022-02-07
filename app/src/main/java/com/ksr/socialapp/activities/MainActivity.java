package com.ksr.socialapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ksr.socialapp.R;
import com.ksr.socialapp.fragments.AddFragment;
import com.ksr.socialapp.fragments.NotificationsFragment;
import com.ksr.socialapp.fragments.HomeFragment;
import com.ksr.socialapp.fragments.ProfileFragment;
import com.ksr.socialapp.fragments.SearchFragment;
import com.ksr.socialapp.tools.FragmentManager;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);


        fragmentManager = new FragmentManager(getSupportFragmentManager());
        fragmentManager.openHomeFragment(false,false);


    }


    // changing fragments according to bottom navigation bar
    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.nav_home:
                            fragmentManager.openHomeFragment(false,false);
                            break;
                        case R.id.nav_search:
                            fragmentManager.openSearchFragment(false,false);
                            break;
                        case R.id.nav_add:
                            fragmentManager.openAddFragment(false,false);
                            break;
                        case R.id.nav_notification:
                            fragmentManager.openNotificationsFragment(false,false);
                            break;
                        case R.id.nav_profile:
                            fragmentManager.openProfileFragment(false,false);
                            break;
                        default:
                            fragmentManager.openHomeFragment(false,false);
                            break;
                    }
                    return true;
                }
            };


}
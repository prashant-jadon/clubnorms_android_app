package com.chandra.clubnorms;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.chandra.clubnorms.fragments.AddPostFragment;
import com.chandra.clubnorms.fragments.HomeFragment;
import com.chandra.clubnorms.fragments.NotificationsFragment;
import com.chandra.clubnorms.fragments.ProfileFragment;
import com.chandra.clubnorms.fragments.SavedFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        // Set default fragment to HomeFragment
        loadFragment(new HomeFragment());

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                int itemId = item.getItemId();

                if (itemId == R.id.home) {
                    fragment = new HomeFragment();
                    overridePendingTransition(0, 0);
                } else if (itemId == R.id.savedcourses) {
                    fragment = new SavedFragment();
                    overridePendingTransition(0, 0);
                } else if (itemId == R.id.addpost) {
                    fragment = new AddPostFragment();
                    overridePendingTransition(0, 0);
                } else if (itemId == R.id.notifications) {
                    fragment = new NotificationsFragment();
                    overridePendingTransition(0, 0);
                } else if (itemId == R.id.profile) {
                    fragment = new ProfileFragment();
                    overridePendingTransition(0, 0);
                }

                // Load the selected fragment if it's not null
                if (fragment != null) {
                    loadFragment(fragment);
                    return true;
                }

                return false;
            }
        });
    }

    // Function to load the selected fragment into the container
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.commit();
    }
}

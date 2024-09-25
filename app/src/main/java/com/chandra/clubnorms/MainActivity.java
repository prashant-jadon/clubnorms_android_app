package com.chandra.clubnorms;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.chandra.clubnorms.fragments.AddCourseFragment;
import com.chandra.clubnorms.fragments.AddPostFragment;
import com.chandra.clubnorms.fragments.HomeFragment;
import com.chandra.clubnorms.fragments.NotificationsFragment;
import com.chandra.clubnorms.fragments.ProfileFragment;
import com.chandra.clubnorms.fragments.SavedFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private HashMap<Integer, Fragment> fragmentMap = new HashMap<>();
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        // Load the default fragment
        currentFragment = new HomeFragment();
        loadFragment(currentFragment);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                int itemId = item.getItemId();

                // Check if the fragment is already loaded
                if (fragmentMap.containsKey(itemId)) {
                    fragment = fragmentMap.get(itemId);
                } else {
                    if (itemId == R.id.home) {
                        fragment = new HomeFragment();
                    } else if (itemId == R.id.savedcourses) {
                        fragment = new SavedFragment();
                    } else if (itemId == R.id.addpost) {
                        showAddDialog(); // Show dialog for Add Post / Add Course
                        return true; // Do not proceed with loading fragment
                    } else if (itemId == R.id.notifications) {
                        fragment = new NotificationsFragment();
                    } else if (itemId == R.id.profile) {
                        fragment = new ProfileFragment();
                    }

                    // Store the new fragment in the map
                    if (fragment != null) {
                        fragmentMap.put(itemId, fragment);
                    }
                }

                // Load the selected fragment if it's not null and different from current
                if (fragment != null && fragment != currentFragment) {
                    loadFragment(fragment);
                    currentFragment = fragment; // Update current fragment
                    return true;
                }

                return false;
            }
        });
    }

    // Show dialog for Add Post / Add Course
    private void showAddDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Choose Action")
                .setMessage("What would you like to do?")
                .setPositiveButton("Add Post", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        loadFragment(new AddPostFragment()); // Load the Add Post fragment
                    }
                })
                .setNegativeButton("Add Course", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        loadFragment(new AddCourseFragment()); // Load the Add Course fragment
                    }
                })
                .show(); // Show the alert dialog
    }

    // Function to load the selected fragment into the container
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.commit();
    }
}

package com.chandra.clubnorms.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chandra.clubnorms.CoursePublicFragment;
import com.chandra.clubnorms.MeetsPublicFragment;
import com.chandra.clubnorms.R;
import com.chandra.clubnorms.adapters.MeetAdapter;
import com.chandra.clubnorms.fragmentsInProfile.CoursesFragment;
import com.chandra.clubnorms.fragmentsInProfile.HostedFragment;
import com.chandra.clubnorms.modals.MeetDataModal;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private MeetAdapter adapter;
    private List<MeetDataModal> dataList;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottomNavigationForHome);
        loadFragment(new MeetsPublicFragment());

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                int itemId = item.getItemId();

                if(itemId==R.id.courses){
                    fragment = new CoursePublicFragment();
                    getEnterTransition();
                }else  if(itemId==R.id.meets){
                    fragment = new MeetsPublicFragment();
                }

                if(fragment!=null){
                    loadFragment(fragment);
                }
                return true;
            }
        });


        return  view;
    }


    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayoutForHome, fragment);
        transaction.commit();
    }



}
package com.chandra.clubnorms.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chandra.clubnorms.R;
import com.chandra.clubnorms.fragmentsInProfile.CoursesFragment;
import com.chandra.clubnorms.fragmentsInProfile.HostedFragment;
import com.chandra.clubnorms.fragmentsInProfile.UpcomingFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    TextView fullname,interest,bio,followersCountText,follwingsCountText;
    ImageView profile;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottomNavigationForProfile);
        
        loadFragment(new CoursesFragment());

        fullname = view.findViewById(R.id.fullname);
        interest = view.findViewById(R.id.interest);
        bio = view.findViewById(R.id.bio);
        profile = view.findViewById(R.id.profilePic);
        followersCountText = view.findViewById(R.id.followers_count);
        follwingsCountText = view.findViewById(R.id.following_count);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                int itemId = item.getItemId();

                if(itemId==R.id.courses){
                    fragment = new CoursesFragment();
                    getEnterTransition();
                }else  if(itemId==R.id.hosted){
                    fragment = new HostedFragment();
                }else if(itemId==R.id.upcoming){
                    fragment=new UpcomingFragment();
                }

                if(fragment!=null){
                    loadFragment(fragment);
                }
                return true;
            }
        });

        if(user!=null){

            db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("usersCollection").document(user.getUid());

            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()){
                        String fname = documentSnapshot.getString("fullname");
                        String intset = documentSnapshot.getString("interest");
                        String biodata = documentSnapshot.getString("bio");
                        String profilePic = documentSnapshot.getString("profilePicture");
                        List<String> followersList = (List<String>) documentSnapshot.get("Followers");
                        List<String> followingList = (List<String>) documentSnapshot.get("Followings");

                        String followersCount = followersList != null ? followersList.size()+"" : "0";
                        String followingsCount = followingList != null ? followingList.size()+"" : "0";

                        followersCountText.setText(followersCount);
                        follwingsCountText.setText(followingsCount);


                        fullname.setText(fname);
                        interest.setText(intset);
                        bio.setText(biodata);

                        Glide.with(getContext())
                                .load(profilePic) // image URL
                                .apply(new RequestOptions()
                                        .circleCrop() // Apply circle crop
                                        .placeholder(R.mipmap.ic_launcher_round) // Placeholder image
                                        .error(R.drawable.usericon) // Error image
                                        .override(200, 200)) // Resizing
                                .into(profile); // ImageView object// imageview object
                    }else{
                        Toast.makeText(getContext(), "Check your internet and refresh", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }


        return  view;
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayoutOnProfile, fragment);
        transaction.commit();
    }



}
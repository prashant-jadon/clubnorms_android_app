package com.chandra.clubnorms.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chandra.clubnorms.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.type.DateTime;


import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class AddPostFragment extends Fragment {

    EditText titleEdit,descriptionEdit;
    Button addMeet;
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_post, container, false);

        titleEdit = view.findViewById(R.id.title);
        descriptionEdit = view.findViewById(R.id.description);
        addMeet = view.findViewById(R.id.addmeet);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();

        try {
            URL serverUrl = new URL("https://meet.jit.si");
            JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                    .setServerURL(serverUrl)
                    .build();
            JitsiMeet.setDefaultConferenceOptions(options);
        } catch (MalformedURLException e) {
            Log.e("MeetAdapter", "Error initializing Jitsi: ", e);
        }


        addMeet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";
                StringBuilder s = new StringBuilder(10);
                int y;
                for ( y = 0; y < 10; y++) {
                    int index = (int)(AlphaNumericString.length() * Math.random());
                    s.append(AlphaNumericString.charAt(index));
                }
                String title = titleEdit.getText().toString();
                String description = descriptionEdit.getText().toString();

                if(!userId.isEmpty()){
                    DocumentReference docRef = db.collection("usersCollection").document(user.getUid());
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if(documentSnapshot.exists()){
                                String fname = documentSnapshot.getString("fullname");
                                String profilePic = documentSnapshot.getString("profilePicture");
                                String code = s.toString();
                                Map<String,Object> dataForMeet = new HashMap<>();
                                dataForMeet.put("title",title);
                                dataForMeet.put("description",description);
                                dataForMeet.put("createdAt", FieldValue.serverTimestamp());
                                dataForMeet.put("meetLink",code);
                                dataForMeet.put("fullname",fname);
                                dataForMeet.put("profilePicture",profilePic);
                                db.collection("usersCollection").document(userId).collection("meets")
                                        .add(dataForMeet).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Toast.makeText(getContext(), "Meet added successfuly", Toast.LENGTH_SHORT).show();

                                                try {
                                                    JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                                                            .setRoom(code)
                                                            .build();
                                                    JitsiMeetActivity.launch(getContext(), options);
                                                } catch (Exception e) {
                                                    Toast.makeText(getContext(), "Error launching Jitsi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    Log.e("MeetAdapter", "Error launching Jitsi activity: ", e);
                                                }
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getContext(), "Failed to add meet", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                db.collection("publicMeets").document().set(dataForMeet)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(getContext(), "Meet are available in public", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
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
            }
        });

        return  view;
    }
}
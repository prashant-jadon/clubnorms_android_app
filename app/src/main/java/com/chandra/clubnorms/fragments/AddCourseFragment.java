package com.chandra.clubnorms.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddCourseFragment extends Fragment {

    EditText titleCourseEdit,descriptionCourseEdit;
    Button addCourseButton;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_course, container, false);

        titleCourseEdit = view.findViewById(R.id.titleForCourse);
        descriptionCourseEdit = view.findViewById(R.id.descriptionForCourse);
        addCourseButton = view.findViewById(R.id.addCourse);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();
        
        
        addCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = titleCourseEdit.getText().toString();
                String description = descriptionCourseEdit.getText().toString();
                if(!userId.isEmpty()){
                    DocumentReference docRef = db.collection("usersCollection").document(user.getUid());
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if(documentSnapshot.exists()){
                                String fname = documentSnapshot.getString("fullname");
                                String profilePic = documentSnapshot.getString("profilePicture");
                                
                                Map<String,Object> dataForCourse = new HashMap<>();
                                dataForCourse.put("titleForCourse",title);
                                dataForCourse.put("descriptionForCourse",description);
                                dataForCourse.put("createdAt", FieldValue.serverTimestamp());
                                dataForCourse.put("fullname",fname);
                                dataForCourse.put("profilePicture",profilePic);
                                dataForCourse.put("ownerUserId",userId);
                                dataForCourse.put("joinedUsers",new ArrayList<>());

                                String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";
                                StringBuilder s = new StringBuilder(10);
                                int y;
                                for ( y = 0; y < 10; y++) {
                                    int index = (int)(AlphaNumericString.length() * Math.random());
                                    s.append(AlphaNumericString.charAt(index));
                                }

                                String code = s.toString();
                                dataForCourse.put("uniqueCode",code);

                                db.collection("usersCollection").document(userId).collection("courses")
                                        .add(dataForCourse).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Toast.makeText(getContext(), "Course added successfuly", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getContext(), "Course failed successfuly", Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                db.collection("publicCourses").document(code)
                                        .set(dataForCourse).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(getContext(), "Course added successfuly", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getContext(), "Course failed successfuly", Toast.LENGTH_SHORT).show();

                                            }
                                        });



                            }
                        }
                    });
                }
            }
        });
        
        return view;
    }
}
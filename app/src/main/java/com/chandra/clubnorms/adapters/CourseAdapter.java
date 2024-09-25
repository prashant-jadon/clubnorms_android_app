package com.chandra.clubnorms.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chandra.clubnorms.R;
import com.chandra.clubnorms.modals.CourseModal;
import com.chandra.clubnorms.modals.MeetDataModal;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {

    private List<CourseModal> courseData;
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    public CourseAdapter(List<CourseModal> courseData) {
        this.courseData = courseData;
    }

    @NonNull
    @Override
    public CourseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_course_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.ViewHolder holder, int position) {
        CourseModal courseModal = courseData.get(position);

        holder.title.setText(courseModal.gettitleForCourse());
        holder.description.setText(courseModal.getdescriptionForCourse());
        holder.time.setText(courseModal.getCreatedAt() != null ? getTimeAgo(courseModal.getCreatedAt()) : "Unknown Time");
        holder.username.setText(courseModal.getFullname());

        String uniqueCode = courseModal.getuniqueCode();
        String ownerId = courseModal.getOwnerUserId();
        Picasso.get()
                .load(courseModal.getprofilePicture()) // URL of the image
                .into(holder.profilePic);

        holder.courseAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = FirebaseFirestore.getInstance();
                mAuth = FirebaseAuth.getInstance();

                FirebaseUser user = mAuth.getCurrentUser();
                String userId = user.getUid();
                if(!userId.isEmpty()){
                  db.collection("usersCollection").document(userId).update("courses", FieldValue.arrayUnion(uniqueCode));
                    db.collection("publicCourses").document(uniqueCode).update("joinedUsers", FieldValue.arrayUnion(userId));
                    Toast.makeText(holder.itemView.getContext(), "Course Added to Your saved", Toast.LENGTH_SHORT).show();

                    CollectionReference coursesCollection = db.collection("usersCollection").document(userId).collection("courses");

                    // Query to find the document with the matching unique code
                    coursesCollection.whereEqualTo("uniqueCode", uniqueCode).get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                        // If a document is found, update the desired field
                                        for (DocumentSnapshot document : task.getResult()) {
                                            // Assuming you want to update a field named "joined" to true
                                            document.getReference().update("joinedUsers", FieldValue.arrayUnion(userId))
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Toast.makeText(holder.itemView.getContext(), "Course Joined", Toast.LENGTH_SHORT).show();

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(holder.itemView.getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    } else {
                                        Toast.makeText(holder.itemView.getContext(), "No matching course found", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(holder.itemView.getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

            }
            }
        });
    }
    public static String getTimeAgo(Timestamp timestamp) {
        long timeDifference = System.currentTimeMillis() - timestamp.toDate().getTime();

        long minutes = TimeUnit.MILLISECONDS.toMinutes(timeDifference);
        long hours = TimeUnit.MILLISECONDS.toHours(timeDifference);
        long days = TimeUnit.MILLISECONDS.toDays(timeDifference);

        if (minutes < 1) {
            return "Just now";
        } else if (minutes < 60) {
            return minutes + " minutes ago";
        } else if (hours < 24) {
            return hours + " hours ago";
        } else {
            return days + " days ago";
        }
    }

    @Override
    public int getItemCount() {
        return courseData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,description,time,username;
        ImageView profilePic;
        Button courseAdd;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.titleCourse); // Change this
            description = itemView.findViewById(R.id.descriptionCourse); // Change this
            time = itemView.findViewById(R.id.timeCourse); // Change this
            username = itemView.findViewById(R.id.fullnameCourse); // Change this
            profilePic = itemView.findViewById(R.id.profileImageCourse);
            courseAdd = itemView.findViewById(R.id.joincoursebutton); //
        }
    }
}

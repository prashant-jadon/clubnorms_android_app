package com.chandra.clubnorms;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chandra.clubnorms.adapters.CourseAdapter;
import com.chandra.clubnorms.adapters.MeetAdapter;
import com.chandra.clubnorms.modals.CourseModal;
import com.chandra.clubnorms.modals.MeetDataModal;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class CoursePublicFragment extends Fragment {

    private RecyclerView recyclerView;
    private CourseAdapter adapter;
    private List<CourseModal> dataList;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_course_public, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewForPublicCourses);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dataList = new ArrayList<>();
        adapter = new CourseAdapter(dataList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        fetchDataFromFirebase();

        return  view;
    }

    private void fetchDataFromFirebase() {
        CollectionReference collectionRef = db.collection("publicCourses");
        collectionRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    CourseModal data = document.toObject(CourseModal.class);
                    dataList.add(data);
                }
                adapter.notifyDataSetChanged(); // Notify adapter about data changes
            } else {
                Toast.makeText(getContext(), "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
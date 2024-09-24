package com.chandra.clubnorms.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chandra.clubnorms.R;
import com.chandra.clubnorms.adapters.MeetAdapter;
import com.chandra.clubnorms.modals.MeetDataModal;
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

        recyclerView = view.findViewById(R.id.recyclerViewForPublic);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dataList = new ArrayList<>();
        adapter = new MeetAdapter(dataList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        fetchDataFromFirebase();
        
        return  view;
    }

    private void fetchDataFromFirebase() {
        CollectionReference collectionRef = db.collection("publicMeets");
        collectionRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    MeetDataModal data = document.toObject(MeetDataModal.class);
                    dataList.add(data);
                    Log.d("HomeFragment", "Data: " + data.getTitle() + ", " + data.getCreatedAt());
                }
                adapter.notifyDataSetChanged(); // Notify adapter about data changes
            } else {
                Toast.makeText(getContext(), "Error fetching data", Toast.LENGTH_SHORT).show();
                Log.e("HomeFragment", "Error getting documents: ", task.getException());
            }
        });
    }

}
package com.chandra.clubnorms.fragmentsInProfile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chandra.clubnorms.R;
import com.chandra.clubnorms.adapters.MeetAdapter;
import com.chandra.clubnorms.modals.MeetDataModal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HostedFragment extends Fragment {

    private RecyclerView recyclerView;
    private MeetAdapter adapter;
    private List<MeetDataModal> dataList;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    TextView noContent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hosted, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewForPrivate);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dataList = new ArrayList<>();
        adapter = new MeetAdapter(dataList);
        recyclerView.setAdapter(adapter);
        noContent = view.findViewById(R.id.nocontent);

        db = FirebaseFirestore.getInstance();
        fetchDataFromFirebasePrivate();
        
        return view;
    }

    private void fetchDataFromFirebasePrivate() {

        CollectionReference collectionRef = db.collection("usersCollection").document(user.getUid().toString()).collection("meets");
        collectionRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    MeetDataModal data = document.toObject(MeetDataModal.class);
                    dataList.add(data);
                }
                adapter.notifyDataSetChanged(); // Notify adapter about data changes
            } else {
                noContent.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
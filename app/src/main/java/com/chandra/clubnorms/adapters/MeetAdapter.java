package com.chandra.clubnorms.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chandra.clubnorms.R;
import com.chandra.clubnorms.modals.MeetDataModal;
import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MeetAdapter extends RecyclerView.Adapter<MeetAdapter.ViewHolder> {

    private List<MeetDataModal> meetData;
    private static boolean isJitsiInitialized = false;

    public MeetAdapter(List<MeetDataModal> meetData) {
        this.meetData = meetData;
        initializeJitsiMeet(); // Initialize Jitsi once during adapter construction
    }

    private void initializeJitsiMeet() {
        if (!isJitsiInitialized) {
            try {
                URL serverUrl = new URL("https://meet.jit.si");
                JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                        .setServerURL(serverUrl)
                        .build();
                JitsiMeet.setDefaultConferenceOptions(options);
                isJitsiInitialized = true; // Prevent re-initialization
            } catch (MalformedURLException e) {
                Log.e("MeetAdapter", "Error initializing Jitsi: ", e);
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_meet_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MeetDataModal data = meetData.get(position);
        holder.time.setText(data.getCreatedAt() != null ? data.getCreatedAt().toDate().toString() : "Unknown Time");
        holder.title.setText(data.getTitle());
        holder.description.setText(data.getDescription());
        holder.fullname.setText(data.getFullname());

        holder.codeForMeet.setOnClickListener(view -> {
            String meetLink = data.getMeetLink();
            if (!meetLink.isEmpty()) {
                launchJitsiMeet(holder.itemView.getContext(), meetLink);
            } else {
                Toast.makeText(holder.itemView.getContext(), "Meet link is empty", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void launchJitsiMeet(Context context, String meetLink) {
        try {
            JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                    .setRoom(meetLink)
                    .build();
            JitsiMeetActivity.launch(context, options);
        } catch (Exception e) {
            Toast.makeText(context, "Error launching Jitsi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("MeetAdapter", "Error launching Jitsi activity: ", e);
        }
    }

    @Override
    public int getItemCount() {
        return meetData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView time, title, description, fullname;
        Button codeForMeet;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            fullname = itemView.findViewById(R.id.fullname);
            codeForMeet = itemView.findViewById(R.id.joinmeetbutton);
        }
    }
}

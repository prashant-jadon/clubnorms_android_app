package com.chandra.clubnorms.adapters;

import static java.security.AccessController.getContext;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestOptions;
import com.chandra.clubnorms.R;
import com.chandra.clubnorms.modals.MeetDataModal;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
public class MeetAdapter extends RecyclerView.Adapter<MeetAdapter.ViewHolder> {

    private List<MeetDataModal> meetData;

    public MeetAdapter(List<MeetDataModal> meetData) {
        this.meetData = meetData;
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

        // Format the createdAt timestamp to a readable string
        String formattedTime = data.getCreatedAt() != null ? data.getCreatedAt().toDate().toString() : "Unknown Time";
        holder.time.setText(formattedTime);
        holder.title.setText(data.getTitle());
        holder.description.setText(data.getDescription());
        holder.fullname.setText(data.getFullname());


        holder.codeForMeet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String meetLink = data.getMeetLink();
                Toast.makeText(holder.itemView.getContext(), meetLink, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return meetData.size(); // Return the actual size of the list
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView time, title, description, fullname;
        ImageView profilepic;
        Button codeForMeet;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            fullname = itemView.findViewById(R.id.fullname);
            profilepic = itemView.findViewById(R.id.profileImage);
            codeForMeet = itemView.findViewById(R.id.joinmeetbutton);
        }
    }
}

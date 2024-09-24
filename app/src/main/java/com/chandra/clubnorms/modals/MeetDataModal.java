package com.chandra.clubnorms.modals;

import com.google.firebase.Timestamp;

public class MeetDataModal {


    String title;
    Timestamp createdAt;


    public MeetDataModal(String title, Timestamp createdAt, String description, String meetLink, String fullname, String profilepic) {
        this.title = title;
        this.createdAt = createdAt;
        this.description = description;
        this.meetLink = meetLink;
        this.fullname = fullname;
        this.profilepic = profilepic;
    }

    String description;
    String meetLink;
    String fullname;

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getMeetLink() {
        return meetLink;
    }

    public void setMeetLink(String meetLink) {
        this.meetLink = meetLink;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    String profilepic;

    public MeetDataModal() {

    }


}

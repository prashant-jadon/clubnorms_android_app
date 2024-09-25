package com.chandra.clubnorms.modals;

import com.google.firebase.Timestamp;

public class CourseModal {
    public CourseModal() {
    }

    public String getuniqueCode() {
        return uniqueCode;
    }

    public void setuniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    String uniqueCode;
    String ownerUserId;

    public String getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(String ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    String titleForCourse;
    Timestamp createdAt;

    public CourseModal(String titleForCourse, Timestamp createdAt, String descriptionForCourse, String fullname, String profilePictureture,String ownerUserId) {
        this.titleForCourse = titleForCourse;
        this.createdAt = createdAt;
        this.descriptionForCourse = descriptionForCourse;
        this.fullname = fullname;
        this.profilePicture = profilePicture;
        this.uniqueCode = uniqueCode;
        this.ownerUserId = ownerUserId;
    }

    String descriptionForCourse;

    public String getprofilePicture() {
        return profilePicture;
    }

    public void setprofilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getdescriptionForCourse() {
        return descriptionForCourse;
    }

    public void setdescriptionForCourse(String descriptionForCourse) {
        this.descriptionForCourse = descriptionForCourse;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String gettitleForCourse() {
        return titleForCourse;
    }

    public void settitleForCourse(String titleForCourse) {
        this.titleForCourse = titleForCourse;
    }

    String fullname;
    String profilePicture;
}

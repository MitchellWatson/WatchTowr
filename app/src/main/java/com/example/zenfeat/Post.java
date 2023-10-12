package com.example.zenfeat;

import java.util.List;

public class Post {
    private String company;
    private String description;
    private String position;
    private String location;
    private String url;
    private int reputation;
    private String poster;
    private String occupation;
    private String uid;
    private String date;
    private List<String> usersLiked; // Added usersLiked field

    public Post() {
        // Required default constructor for Firestore
    }

    public Post(String company, String description, String position, String location, String url, int reputation, String poster, String occupation, String uid, String date, List<String> usersLiked) {
        this.company = company;
        this.description = description;
        this.position = position;
        this.location = location;
        this.url = url;
        this.reputation = reputation;
        this.poster = poster;
        this.occupation = occupation;
        this.uid = uid;
        this.date = date;
        this.usersLiked = usersLiked;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getReputation() {
        return reputation;
    }

    public void setReputation(int reputation) {
        this.reputation = reputation;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getUsersLiked() {
        return usersLiked;
    }

    public void setUsersLiked(List<String> usersLiked) {
        this.usersLiked = usersLiked;
    }
}

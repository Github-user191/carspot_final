package com.carspot.app.payload.response;

import com.carspot.app.entity.ContactForm;
import com.carspot.app.entity.Post;

import java.util.ArrayList;
import java.util.List;

public class UserInfoResponse {
    private Long id;
    private String fullName;
    private String emailAddress;
    private String mobileNumber;
    private String password;
    private int totalAds;
    private List<Post> posts = new ArrayList<>();
    private List<ContactForm> contactForms = new ArrayList<>();

    public UserInfoResponse() {

    }

    public UserInfoResponse(Long id, String fullName, String emailAddress, String mobileNumber, String password, int totalAds, List<Post> posts, List<ContactForm> contactForms) {
        this.id = id;
        this.fullName = fullName;
        this.emailAddress = emailAddress;
        this.mobileNumber = mobileNumber;
        this.password = password;
        this.totalAds = totalAds;
        this.posts = posts;
        this.contactForms = contactForms;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getTotalAds() {
        return totalAds;
    }

    public void setTotalAds(int totalAds) {
        this.totalAds = totalAds;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<ContactForm> getContactForms() {
        return contactForms;
    }

    public void setContactForms(List<ContactForm> contactForms) {
        this.contactForms = contactForms;
    }
}

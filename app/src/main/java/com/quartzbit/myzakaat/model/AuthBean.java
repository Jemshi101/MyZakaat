package com.quartzbit.myzakaat.model;

/**
 * Created by Jemsheer K D on 30 November, 2016.
 * Package com.quartzbit.myzakaat.model
 * Project Dearest
 */

public class AuthBean extends BaseBean {

    private String authToken;
    private String userID;
    private String name;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private String gender;
    private long DOB;
    private String profilePhoto;
    private String coverPhoto;
    private String country;
    private String address;
    private String state;
    private String city;
    private boolean isAllDocumentsUploaded;
    private boolean isAllDocumentsVerified;
    private boolean isPhoneVerified;
    private boolean isEmailVerified;

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public long getDOB() {
        return DOB;
    }

    public void setDOB(long DOB) {
        this.DOB = DOB;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(String coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean isAllDocumentsUploaded() {
        return isAllDocumentsUploaded;
    }

    public void setAllDocumentsUploaded(boolean allDocumentsUploaded) {
        isAllDocumentsUploaded = allDocumentsUploaded;
    }

    public boolean isAllDocumentsVerified() {
        return isAllDocumentsVerified;
    }

    public void setAllDocumentsVerified(boolean allDocumentsVerified) {
        isAllDocumentsVerified = allDocumentsVerified;
    }

    public boolean isPhoneVerified() {
        return isPhoneVerified;
    }

    public void setPhoneVerified(boolean phoneVerified) {
        isPhoneVerified = phoneVerified;
    }

    public boolean isEmailVerified() {
        return isEmailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        isEmailVerified = emailVerified;
    }
}

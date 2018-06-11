package com.quartzbit.myzakaat.config;


import java.util.ArrayList;
import java.util.Locale;

import com.quartzbit.myzakaat.model.BasicDataBean;
import com.quartzbit.myzakaat.model.ContactBean;
import com.quartzbit.myzakaat.model.PhoneContactsBean;
import okhttp3.OkHttpClient;

public class Config {

    private static Config instance;

    private OkHttpClient okHttpClient;

    private String authToken;
    private String fcmID;
    private String password;
    private String userID;
    private String name;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private String profilePhoto;
    private String coverPhoto;
    private String profilePhotoLocal;
    private long DOB;
    private String gender;
    private boolean isPhoneVerified;
    private boolean isEmailVerified;
    private boolean isOnline;

    private String currentLocation;
    private String currentLatitude;
    private String currentLongitude;

    private ArrayList<String> availableStickerKeyList = new ArrayList<>();

    private String locale = Locale.getDefault().getLanguage();
    private BasicDataBean basicDataBean;

    private PhoneContactsBean phoneContactsBean = new PhoneContactsBean();

    public static Config getInstance() {
        if (instance == null)
            instance = new Config();

        return instance;
    }

    public static void clear() {
        instance = null;
        instance = new Config();
    }

    private Config() {
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public void setOkHttpClient(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getFcmID() {
        return fcmID;
    }

    public void setFcmID(String fcmID) {
        this.fcmID = fcmID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getProfilePhotoLocal() {
        return profilePhotoLocal;
    }

    public void setProfilePhotoLocal(String profilePhotoLocal) {
        this.profilePhotoLocal = profilePhotoLocal;
    }

    public long getDOB() {
        return DOB;
    }

    public void setDOB(long DOB) {
        this.DOB = DOB;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getCurrentLatitude() {
        return currentLatitude;
    }

    public void setCurrentLatitude(String currentLatitude) {
        this.currentLatitude = currentLatitude;
    }

    public String getCurrentLongitude() {
        return currentLongitude;
    }

    public void setCurrentLongitude(String currentLongitude) {
        this.currentLongitude = currentLongitude;
    }

    public double getDCurrentLatitude() {
        try {
            return Double.parseDouble(currentLatitude);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0.0;
        }
    }
    public double getDCurrentLongitude() {
        try {
            return Double.parseDouble(currentLongitude);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    public ArrayList<String> getAvailableStickerKeyList() {
        return availableStickerKeyList;
    }

    public void setAvailableStickerKeyList(ArrayList<String> availableStickerKeyList) {
        this.availableStickerKeyList = availableStickerKeyList;
    }

    public BasicDataBean getBasicDataBean() {
        return basicDataBean;
    }

    public void setBasicDataBean(BasicDataBean basicDataBean) {
        this.basicDataBean = basicDataBean;
    }

    public PhoneContactsBean getPhoneContactsBean() {
        return phoneContactsBean;
    }

    public void setPhoneContactsBean(PhoneContactsBean phoneContactsBean) {
        this.phoneContactsBean = phoneContactsBean;
    }

    public PhoneContactsBean getPhoneContactsBean(String searchParam) {

        PhoneContactsBean result = new PhoneContactsBean();

        for (ContactBean contactBean : phoneContactsBean.getContacts()) {

            if (contactBean.getName().toLowerCase().contains(searchParam.toLowerCase())) {
                result.getContacts().add(contactBean);
                continue;
            }
            for (String email : contactBean.getEmails()) {
                if (email.toLowerCase().contains(searchParam.toLowerCase())) {
                    result.getContacts().add(contactBean);
                    break;
                }
            }
            if (!result.getContacts().contains(contactBean)) {
                for (String phone : contactBean.getPhoneNumbers()) {
                    if (phone.toLowerCase().contains(searchParam.toLowerCase())) {
                        result.getContacts().add(contactBean);
                        break;
                    }
                }
            }
        }

        return result;
    }
}

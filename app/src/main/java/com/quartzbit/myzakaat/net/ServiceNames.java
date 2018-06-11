package com.quartzbit.myzakaat.net;


public class ServiceNames {

    /*Set BASE URL here*/
    private static final String PRODUCTION_API = "http://144.217.204.89:7001";
    public static final String TESTING_API = "http://demo2894133.mockable.io";
    //    public static final String STAGING_API = "http://182.74.149.58";
    public static final String STAGING_API = "http://144.217.204.89:7001";
    public static final String DEVELOPMENT_API = "http://192.168.138.158";

    /*Set CURRENT BASE URL here*/
//    private static final String BASE_URL = PRODUCTION_API;
    private static final String BASE_URL = STAGING_API;
//    private static final String BASE_URL = DEVELOPMENT_API;
//    private static final String BASE_URL = TESTING_API;

    /* Set API VERSION here*/
    public static final String API_VERSION_PRODUCTION = "/api";
    //    public static final String API_VERSION_STAGING = "/lataxi/Webservices_driver";
    public static final String API_VERSION_STAGING = "/api";
    public static final String API_VERSION_DEVELOPMENT = "/api";
//        public static final String API_VERSION = "/API/v1";


    /* Set CURRENT API VERSION here*/
//    public static final String API_VERSION = API_VERSION_PRODUCTION;
    public static final String API_VERSION = API_VERSION_STAGING;
//    public static final String API_VERSION = API_VERSION_DEVELOPMENT;

    /*Set UPLOAD PATH. DO NOT CHANGE THIS UNLESS YOU KNOW WHAT YOU ARE DOING.*/
    public static final String PATH_UPLOADS_PRODUCTION = "/";
    //    public static final String PATH_UPLOADS_STAGING = "/lataxi";
    public static final String PATH_UPLOADS_STAGING = "/";
    public static final String PATH_UPLOADS_DEVELOPMENT = "/";


    /*SetCURRENT  UPLOAD PATH*/
//    public static final String PATH_UPLOADS = PATH_UPLOADS_PRODUCTION;
    public static final String PATH_UPLOADS = PATH_UPLOADS_STAGING;
//    public static final String PATH_UPLOADS = PATH_UPLOADS_DEVELOPMENT;

    /*Set API URL here*/
    private static final String API = BASE_URL + API_VERSION;

    /*Set IMAGE UPLOAD URL here.  DO NOT CHANGE THIS UNLESS YOU KNOW WHAT YOU ARE DOING.*/
    public static final String API_UPLOADS = BASE_URL + PATH_UPLOADS;


    /* API END POINTS*/


    public static final String DUMMY = API + "/dummy?";

    public static final String POLY_POINTS = "https://maps.googleapis.com/maps/api/directions/json?";
    public static final String LOCATION_NAME = "https://maps.googleapis.com/maps/api/geocode/json?";

    public static final String REGISTRATION = API + "/registration?";
    public static final String AUTH_EMAIL = API + "/login?";
    public static final String FORGOT_PASSWORD = API + "/forgot_password?";

    public static final String FCM_UPDATE = API + "/update_fcm_token?";

    public static final String PHONE_REGISTRATION = API + "/register_mobile?";
    public static final String MOBILE_NUMBER_AVAILABILITY = API + "/mobile_number_availability?";
    public static final String MOBILE_NUMBER_CHANGE = API + "/change_phone_number?";
    public static final String OTP_VERIFICATION = API + "/verify_otp?";
    public static final String OTP_SEND = API + "/login?";
    public static final String OTP_RESEND_CODE = API + "/resend_otp?";

    public static final String APP_STATUS = API + "/app_status?";

    public static final String UPLOAD_PROFILE_OR_COVER_PHOTO = API + "/profile_or_cover_photo_upload?";

    public static final String UPLOAD_FAMILY_TREE_PHOTO = API + "/family_tree_photo_upload?";
    public static final String UPLOAD_FAMILY_GROUP_PHOTO = API + "/family_group_photo_upload?";
    public static final String UPLOAD_CORPORATE_TREE_PHOTO = API + "/corporate_tree_photo_upload?";
    public static final String UPLOAD_CORPORATE_GROUP_PHOTO = API + "/corporate_group_photo_upload?";
    public static final String UPLOAD_MINISTRY_TREE_PHOTO = API + "/ministry_tree_photo_upload?";
    public static final String UPLOAD_MINISTRY_GROUP_PHOTO = API + "/ministry_group_photo_upload?";

    public static final String UPLOAD_FRIENDS_GROUP_PHOTO = API + "/friend_group_photo_upload?";

    public static final String UPLOAD_CHAT_FILE = API + "/chat_file_upload?";

    public static final String UPLOAD_EVENT_PHOTO = API + "/event_photo_upload?";

    public static final String UPLOAD_TIMELINE_PHOTOS = API + "/timeline_photo_upload?";
    public static final String UPLOAD_TIMELINE_VIDEOS = API + "/timeline_video_upload?";

    public static final String PROFILE = API + "/get_profile?";
    public static final String PROFILE_UPDATE = API + "/update_profile?";


}

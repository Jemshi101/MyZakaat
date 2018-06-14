package com.quartzbit.myzakaat.util;

import com.quartzbit.myzakaat.R;
import com.quartzbit.myzakaat.app.App;
import com.quartzbit.myzakaat.model.BankBean;
import com.quartzbit.myzakaat.model.BankListBean;
import com.quartzbit.myzakaat.net.ServiceNames;

public class AppConstants {


    public static final String UTF_8 = "UTF-8";
    public static final String MIME_TYPE_HTML = "text/html";

    public static final String SPACE = " ";
    public static final String COMMA = App.getInstance().getString(R.string.comma) + SPACE;
    public static final String AND = SPACE + App.getInstance().getString(R.string.and) + SPACE;
    public static final String SEPARATOR = "/";
    public static final String BASE_URL = ServiceNames.API_UPLOADS;

    private static final int COUNTRY_TYPE_ID = 0;
    private static final int COUNTRY_TYPE_DIALCODE = 1;
    private static final int COUNTRY_TYPE_NAME = 2;
    private static final int COUNTRY_TYPE_COUNTRY_CODE = 3;

    private static final int LANGUAGE_TYPE_ID = 0;
    private static final int LANGUAGE_TYPE_SHORTCODE = 1;
    private static final int LANGUAGE_TYPE_NAME = 2;


    public static String WEB_ERROR_MSG = App.getInstance().getResources().getString(R.string.message_web_error);
    public static String NO_NETWORK_AVAILABLE = App.getInstance().getResources().getString(R.string.message_no_network_available);

    public static String EXTRA_PROFILE_TO_IMAGE_MEDIA_LIST = "imageList";
    public static String EXTRA_PROFILE_TO_IMAGE_SELECTED_POSITION = "imagePosition";
    public static String EXTRA_PROFILE_TO_IMAGE_LIST_SIZE = "imageListSize";


    public static final String PREFERENCE_KEY_SESSION_FCM_ID = "fcm_id";
    public static final String PREFERENCE_KEY_SESSION_TOKEN = "auth_token";
    public static final String PREFERENCE_KEY_SESSION_DEVICE_ID = "device_id";
    public static final String PREFERENCE_KEY_SESSION_DEVICE_SECRET = "device_secret";
    public static final String PREFERENCE_KEY_SESSION_ACCESSTOKEN = "access_token";
    public static final String PREFERENCE_KEY_SESSION_REFRESHTOKEN = "refresh_token";
    public static final String PREFERENCE_KEY_SESSION_USERNAME = "username";
    public static final String PREFERENCE_KEY_SESSION_NAME = "name";
    public static final String PREFERENCE_KEY_SESSION_FIRSTNAME = "firstname";
    public static final String PREFERENCE_KEY_SESSION_LASTNAME = "lastname";
    public static final String PREFERENCE_KEY_SESSION_EMAIL = "email";
    public static final String PREFERENCE_KEY_SESSION_PHONE = "phone";
    public static final String PREFERENCE_KEY_SESSION_ADDRESS = "address";
    public static final String PREFERENCE_KEY_SESSION_PROFILE_PHOTO = "profile_photo";
    public static final String PREFERENCE_KEY_SESSION_COVER_PHOTO = "cover_photo";
    public static final String PREFERENCE_KEY_SESSION_USERID = "userid";
    public static final String PREFERENCE_KEY_SESSION_PASSWORD = "password";
    public static final String PREFERENCE_KEY_SESSION_GENDER = "gender";
    public static final String PREFERENCE_KEY_SESSION_IS_FIRST_TIME = "is_first_time";
    public static final String PREFERENCE_KEY_SESSION_IS_PHONE_VERIFIED = "is_phone_verified";
    public static final String PREFERENCE_KEY_SESSION_IS_EMAIL_VERIFIED = "is_email_verified";
    public static final String PREFERENCE_KEY_SESSION_DOB = "dob";
    public static final String PREFERENCE_KEY_SESSION_LOCALE = "locale";
    public static final String PREFERENCE_NAME_SESSION = "session";


    public static String ACTION_CHOOSE_PREFIX = "com.quartzbit.myzakaat.action.CHOOSE_PREFIX";
    public static String ACTION_CHOOSE_SUFFIX = "com.quartzbit.myzakaat.action.CHOOSE_SUFFIX";
    public static String ACTION_CHOOSE_MONTH = "com.quartzbit.myzakaat.action.CHOOSE_MONTH";
    public static String ACTION_CHOOSE_DAY = "com.quartzbit.myzakaat.action.CHOOSE_DAY";
    public static String ACTION_CHOOSE_YEAR = "com.quartzbit.myzakaat.action.CHOOSE_YEAR";
    public static String ACTION_CHOOSE_RELIGION = "com.quartzbit.myzakaat.action.CHOOSE_RELIGION";
    public static String ACTION_CHOOSE_COUNTRY = "com.quartzbit.myzakaat.action.CHOOSE_COUNTRY";
    public static String ACTION_CHOOSE_STATE = "com.quartzbit.myzakaat.action.CHOOSE_STATE";
    public static String ACTION_CHOOSE_DISTRICT = "com.quartzbit.myzakaat.action.CHOOSE_CITY";
    public static String ACTION_CHOOSE_AGE = "com.quartzbit.myzakaat.action.CHOOSE_AGE";
    public static String ACTION_CHOOSE_CAUSE_OF_DEATH = "com.quartzbit.myzakaat.action.CHOOSE_CAUSE_OF_DEATH";

    public static final String INR = "₹";
    public static final String CURRENCY = "₹";

    public static int YEAR_START = 1950;

    public static String MALE = "male";
    public static String FEMALE = "female";

    /* Video Editor Constants*/
    public static final String EXTRA_KEY_VIDEO_SOURCE = "extra_key_video_source";
    public static final String EXTRA_KEY_VIDEO_URI = "extra_key_video_uri";

    /* Image Filter Library Constants*/
    public static final String EXTRA_KEY_IMAGE_SOURCE = "extra_key_image_source";
    public static final String EXTRA_KEY_IMAGE_URI = "extra_key_image_uri";
    public static final String EXTRA_KEY_EFFECT = "e2";

    public static final String BANK_MY_SBI = "1ziNJFrjbc_UoaZLQqS2Gd2jPCjLc9ugPcE4C7qgg3f4";
    public static final String BANK_MY_CURRENT = "1aNCTQRSKiowYTRliPE5PcC8ByPoUF22FeiK7Tn8nnn4";
    public static final String BANK_SALARY_SBI = "e2";
    public static final String BANK_SALARY_AXIS = "e2";

    public static final String RANGE_MY_SBI = "Class Data!A22:J";
    public static final String RANGE_MY_CURRENT = "Class Data!A19:J";
    public static final String RANGE_SALARY_SBI = "Class Data!A22:J";
    public static final String RANGE_SALARY_AXIS = "Class Data!A22:J";

    public static final int MEDIA_TYPE_PHOTO = 0;
    public static final int MEDIA_TYPE_VIDEO = 1;


    public static BankListBean getBankListBean(){

        BankListBean bankListBean =new BankListBean();
        BankBean bankBean =new BankBean();
        bankBean.setId(BANK_MY_SBI);
        bankBean.setAccountName("MY SBI");
        bankBean.setAccountNumber("31150513805");
        bankBean.setBankName("State Bank Of India");
        bankBean.setRange(RANGE_MY_SBI);

        bankListBean.getBanks().add(bankBean);

        bankBean =new BankBean();
        bankBean.setId(BANK_MY_CURRENT);
        bankBean.setAccountName("MY CURRENT");
        bankBean.setAccountNumber("36500638051");
        bankBean.setBankName("State Bank Of India");
        bankBean.setRange(RANGE_MY_CURRENT);

        bankListBean.getBanks().add(bankBean);
/*
        bankBean =new BankBean();
        bankBean.setId(BANK_MY_SBI);
        bankBean.setAccountName("MY SBI");
        bankBean.setAccountNumber("31150513805");
        bankBean.setBankName("State Bank Of India");
        bankBean.setRange(RANGE_MY_SBI);

        bankListBean.getBanks().add(bankBean);

        bankBean =new BankBean();
        bankBean.setId(BANK_MY_SBI);
        bankBean.setAccountName("MY SBI");
        bankBean.setAccountNumber("31150513805");
        bankBean.setBankName("State Bank Of India");
        bankBean.setRange(RANGE_MY_SBI);

        bankListBean.getBanks().add(bankBean);*/

        return  bankListBean;

    }


}

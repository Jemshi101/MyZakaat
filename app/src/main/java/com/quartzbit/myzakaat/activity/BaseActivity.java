package com.quartzbit.myzakaat.activity;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.HapticFeedbackConstants;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.crashlytics.android.Crashlytics;
import com.quartzbit.myzakaat.R;
import com.quartzbit.myzakaat.app.App;
import com.quartzbit.myzakaat.config.TypefaceCache;
import com.quartzbit.myzakaat.dialogs.PopupMessage;
import com.quartzbit.myzakaat.listeners.PermissionListener;
import com.quartzbit.myzakaat.util.FileOp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import io.fabric.sdk.android.Fabric;

//import com.digits.sdk.android.Digits;
//import com.twitter.sdk.android.core.TwitterAuthConfig;
//import com.twitter.sdk.android.core.TwitterCore;

public abstract class BaseActivity extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "tuEpvrhOVzYu6veSjOJecaRvr";
    private static final String TWITTER_SECRET = "1x9JfDhohDBGi2W4PZLcAVdhYBXy35H9OqQbtAea9AmQ6MiaEu";

    protected static final int REQUEST_ENABLE_BT = 0;
    protected static final int REQUEST_PERMISSIONS = 1;
    protected static final int REQUEST_PERMISSIONS_LOCATION = 2;
    protected static final int REQUEST_PERMISSIONS_READ_CONTACTS = 3;
    protected static final int REQUEST_PERMISSIONS_READ_WRITE = 4;
    protected static final int REQUEST_PERMISSIONS_GET_ACCOUNTS = 5;
    protected static final int REQUEST_PERMISSIONS_READ_PHONE_STATE = 6;
    protected static final int REQUEST_PERMISSIONS_CALL = 7;
    protected static final int REQUEST_PERMISSIONS_SMS = 8;
    private static final String TAG = "BaseActivity";
    private int CURRENT_ACTIVITY = -1;
    protected static final int HOME_ACTIVITY = 0;
    protected static final int CHAT_ACTIVITY = 1;
    protected static final int SEARCH_ACTIVITY = 2;
    protected static final int TREES_ACTIVITY = 3;
    protected static final int NOTIFICATIONS_ACTIVITY = 4;
    protected static final int PROFILE_ACTIVITY = 5;
    protected static final int DEAREST_COUNTRY_ACTIVITY = 6;

    protected FileOp fop;

    protected boolean isBluetoothEnableRequestShown;
    protected boolean isLocationServiceEnableRequestShown;

    protected View.OnClickListener snackBarDismissOnClickListener;

    protected Resources r;
    protected static float px;
    protected int width;
    protected int height;

    protected Animation disappear;
    protected Animation slideLeftIn;
    protected Animation slideLeftOut;
    protected Animation slideRightIn;
    protected Animation slideRightOut;
    protected Animation slideUpIn;
    protected Animation slideDownOut;
    protected Animation slideDownIn;
    protected Animation slideUpOut;
    protected Animation fadeIn;
    protected Animation fadeOut;
    protected Animation fadeFastIn;
    protected Animation fadeFastOut;
    protected Animation growBottom;
    protected Animation shrinkBottom;
    protected Animation growFromTopLeftToBottomRight;
    protected Animation growFromBottomRightToTopLeft;
    protected Animation growFromBottomLeftToTopRight;
    protected Animation shrinkFromTopLeftToBottomRight;
    protected Animation shrinkFromBottomRightToTopLeft;
    protected Animation shrinkFromTopRightToBottomLeft;

    protected Typeface typeface;
    protected Typeface typefaceBold;
    protected Typeface typefaceItalic;
    protected Typeface typefaceBoldItalic;

    protected Vibrator mVibrator;
    protected float mActionBarHeight;

    protected int selectableItemBackground;


    protected boolean hasAllPermissions;
    protected boolean hasLocationPermissions;
    protected boolean hasReadWritePermissions;
    protected boolean hasReadContactsPermissions;
    protected boolean hasGetAccountsPermissions;
    protected boolean hasReadPhoneStatePermissions;
    protected boolean hasCallPermissions;
    protected boolean hasSMSPermissions;


    private ArrayList<PermissionListener> permissionListeners = new ArrayList<>();


    protected void initBase() {

        App.checkForToken();



        /*Remove this to remove Crashlytics and Fabric*/
//        Fabric.with(this, new Crashlytics());
//        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Crashlytics()/*, new TwitterCore(authConfig), new Digits.Builder().build()*/);

        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        //	getActionBar().setHomeButtonEnabled(true);

        fop = new FileOp(this.getApplicationContext());

        final TypedArray mstyled = getTheme().obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
        mActionBarHeight = mstyled.getDimension(0, 0);
        mstyled.recycle();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
/*        if (android.os.Build.VERSION.SDK_INT >= 21) {
            toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
        }*/

        //	llBottomBarActionPopup=(LinearLayout)findViewById(R.id.ll_bottombar_popmenu);

        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.selectableItemBackground, typedValue, true);
        selectableItemBackground = typedValue.resourceId;


        disappear = AnimationUtils.loadAnimation(this, R.anim.disappear);
        slideLeftIn = AnimationUtils.loadAnimation(this, R.anim.slide_left_in);
        slideLeftOut = AnimationUtils.loadAnimation(this, R.anim.slide_left_out);
        slideRightIn = AnimationUtils.loadAnimation(this, R.anim.slide_right_in);
        slideRightOut = AnimationUtils.loadAnimation(this, R.anim.slide_right_out);
        slideUpIn = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
        slideDownOut = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
        slideDownIn = AnimationUtils.loadAnimation(this, R.anim.slide_down_in);
        slideUpOut = AnimationUtils.loadAnimation(this, R.anim.slide_up_out);
        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        fadeFastIn = AnimationUtils.loadAnimation(this, R.anim.fast_fade_in);
        fadeFastOut = AnimationUtils.loadAnimation(this, R.anim.fast_fade_out);
        growBottom = AnimationUtils.loadAnimation(this, R.anim.grow_from_bottom);
        shrinkBottom = AnimationUtils.loadAnimation(this, R.anim.shrink_from_top);
        growFromBottomRightToTopLeft = AnimationUtils.loadAnimation(this, R.anim.grow_from_bottomright_to_topleft);
        growFromBottomLeftToTopRight = AnimationUtils.loadAnimation(this, R.anim.grow_from_bottomleft_to_topright);
        growFromTopLeftToBottomRight = AnimationUtils.loadAnimation(this, R.anim.grow_from_topleft_to_bottomright);
        shrinkFromTopLeftToBottomRight = AnimationUtils.loadAnimation(this, R.anim.shrink_from_topleft_to_bottomright);
        shrinkFromBottomRightToTopLeft = AnimationUtils.loadAnimation(this, R.anim.shrink_from_bottomright_to_topleft);
        shrinkFromTopRightToBottomLeft = AnimationUtils.loadAnimation(this, R.anim.shrink_from_topright_to_bottomleft);


        try {
            typeface = TypefaceCache.getInstance().getTypeface(getApplicationContext(), "Roboto-Regular.ttf");
            typefaceBold = TypefaceCache.getInstance().getTypeface(getApplicationContext(), "Roboto-Bold.ttf");
            typefaceItalic = TypefaceCache.getInstance().getTypeface(getApplicationContext(), "Roboto-Italic.ttf");
            typefaceBoldItalic = TypefaceCache.getInstance().getTypeface(getApplicationContext(), "Roboto-BoldItalic.ttf");
        } catch (Exception e) {
            e.printStackTrace();
        }

        r = getResources();
        px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, r.getDisplayMetrics());
        width = r.getDisplayMetrics().widthPixels;
        height = r.getDisplayMetrics().heightPixels;

        snackBarDismissOnClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
//                mVibrator.vibrate(25);
                v.setVisibility(View.GONE);

            }
        };

    }

    protected boolean isDarkToolbar() {
        switch (getCurrentActivity()) {
            case HOME_ACTIVITY:
            case NOTIFICATIONS_ACTIVITY:
            case SEARCH_ACTIVITY:
                return false;
            case DEAREST_COUNTRY_ACTIVITY:
            case PROFILE_ACTIVITY:
            case TREES_ACTIVITY:
            case CHAT_ACTIVITY:
                return true;
            default:
                return false;
        }
    }

    protected void setMenuIconColor(Menu menu, int color) {
        for (int i = 0; i < menu.size(); i++) {
            Drawable drawable = menu.getItem(i).getIcon();
            if (drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(ContextCompat.getColor(getApplicationContext(), color), PorterDuff.Mode.SRC_ATOP);
            }
        }
    }

    int getCurrentActivity() {
        return CURRENT_ACTIVITY;
    }

    void setCurrentActivity(int currentActivity) {
        CURRENT_ACTIVITY = currentActivity;
    }

    protected static void restart(Context context, int delay) {
        if (delay == 0) {
            delay = 1;
        }
        Intent restartIntent = context.getPackageManager()
                .getLaunchIntentForPackage(context.getPackageName());
        PendingIntent intent = PendingIntent.getActivity(
                context, 0,
                restartIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.set(AlarmManager.RTC, System.currentTimeMillis() + delay, intent);
        System.exit(2);
    }

/*    @Override
    protected void onResume() {
        super.onResume();

        if (Config.getInstance().getAuthToken() != null && !Config.getInstance().getAuthToken().equalsIgnoreCase("")
                && Config.getInstance().getMatrimonyID() != null && !Config.getInstance().getMatrimonyID().equalsIgnoreCase("")) {
            mDatabase.child("users").child(Config.getInstance().getMatrimonyID()).child("is_online").setValue(true);

            if (Config.getInstance().getPhotoRequestGrantedProfiles() == null)
                Config.getInstance().setPhotoRequestGrantedProfiles(new ArrayList<String>());

            initFirebase();
        }
    }*/

/*    @Override
    protected void onStop() {
        super.onStop();
        if (Config.getInstance().getAuthToken() != null && !Config.getInstance().getAuthToken().equalsIgnoreCase("")
                && Config.getInstance().getMatrimonyID() != null && !Config.getInstance().getMatrimonyID().equalsIgnoreCase("")) {
            mDatabase.child("users").child(Config.getInstance().getMatrimonyID()).child("is_online").setValue(false);

            if (photoRequestListener != null)
                photoRequestGrantedRef.removeEventListener(photoRequestListener);
        }
    }*/
/*
    private void initFirebase() {

        photoRequestGrantedRef = mDatabase.child("users").child(Config.getInstance().getMatrimonyID()).child("photo_request_granted");
        photoRequestGrantedRef.keepSynced(true);

        photoRequestListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("Count ", "" + dataSnapshot.getChildrenCount());
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    if (Config.getInstance().getPhotoRequestGrantedProfiles() == null)
                        Config.getInstance().setPhotoRequestGrantedProfiles(new ArrayList<String>());

                    if (!Config.getInstance().getPhotoRequestGrantedProfiles().contains(childSnapshot.getKey()))
                        Config.getInstance().getPhotoRequestGrantedProfiles().add(childSnapshot.getKey());

                    Log.i(TAG, "onDataChange: "+childSnapshot.getKey());

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        photoRequestGrantedRef.addValueEventListener(photoRequestListener);

    }*/

    public void performCall(String phone) {
        String url = "tel:" + phone;
        Log.i(TAG, "performCall:  PHONE : " + phone);
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(url));
        try {
            startActivity(intent);
        } catch (Exception ignored) {
        }
    }

    protected boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    // A method to find height of the status bar
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


    String getUserTimeFromUnix(String GMTTime) {
        if (GMTTime.equalsIgnoreCase("-62169984000") || GMTTime.equalsIgnoreCase("false") || GMTTime.equalsIgnoreCase("true"))
            return "";
        try {
            Calendar calTemp = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calTemp.setTimeInMillis(Long.valueOf(GMTTime) * 1000);
            calTemp.setTimeZone(Calendar.getInstance().getTimeZone());
            GMTTime = new SimpleDateFormat("MMM dd yyyy hh:mma", Locale.US)
                    .format(new Date(calTemp.getTimeInMillis()));
            return GMTTime;
        } catch (Exception e) {
            //	e.printStackTrace();
            return GMTTime;
        }
    }


    protected String getDeviceID() {
        String DEVICEID = "";
        String IMEI = "";

        try {
            TelephonyManager mngr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            IMEI = mngr.getDeviceId();

            System.out.println("IMEI : " + IMEI);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            DEVICEID = Build.SERIAL + IMEI;
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("DEVICE ID : " + DEVICEID);
        return DEVICEID;
    }

    void addPermissionListener(PermissionListener permissionListener) {
        if (permissionListeners == null)
            permissionListeners = new ArrayList<>();
        permissionListeners.add(permissionListener);
    }

    protected boolean checkForPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.CLEAR_APP_CACHE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

                /*String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.GET_ACCOUNTS,
                        Manifest.permission.CLEAR_APP_CACHE,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.INTERNET,
                        Manifest.permission.READ_PHONE_STATE};
                ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS);*/
                return hasAllPermissions = false;
            } else {
                return hasAllPermissions = true;
            }
        } else {
            return hasAllPermissions = true;
        }
    }

    protected void getAllPermssions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.CLEAR_APP_CACHE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.GET_ACCOUNTS,
                        Manifest.permission.CLEAR_APP_CACHE,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.INTERNET,
                        Manifest.permission.READ_PHONE_STATE};

                ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS);
            }
        }
    }

    protected boolean checkForContactsPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

               /* String[] permissions = new String[]{
                        Manifest.permission.READ_CONTACTS};
                ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS_READ_CONTACTS);*/
                return hasReadContactsPermissions = false;
            } else {
                return hasReadContactsPermissions = true;
            }
        } else {
            return hasReadContactsPermissions = true;
        }
    }

    protected void getContactsPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

                String[] permissions = new String[]{
                        Manifest.permission.READ_CONTACTS};
                ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS_READ_CONTACTS);
            }
        }
    }

    protected boolean checkForLocationPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                /*String[] permissions = new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,};
                ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS_LOCATION);*/
                return hasLocationPermissions = false;
            } else {
                return hasLocationPermissions = true;
            }
        } else {
            return hasLocationPermissions = true;
        }
    }

    protected void getLocationPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                String[] permissions = new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,};
                ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS_LOCATION);
            }
        }
    }

    protected boolean checkForReadWritePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                /*String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE};
                ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS_READ_WRITE);*/
                return hasReadWritePermissions = false;
            } else {
                return hasReadWritePermissions = true;
            }
        } else {
            return hasReadWritePermissions = true;
        }
    }

    protected void getReadWritePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE};
                ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS_READ_WRITE);
            }
        }
    }

    protected boolean checkForGetAccountsPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
              /*  String[] permissions = new String[]{Manifest.permission.GET_ACCOUNTS};
                ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS_GET_ACCOUNTS);*/
                return hasGetAccountsPermissions = false;
            } else {
                return hasGetAccountsPermissions = true;
            }
        } else {
            return hasGetAccountsPermissions = true;
        }
    }

    protected void getGetAccountsPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
                String[] permissions = new String[]{Manifest.permission.GET_ACCOUNTS};
                ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS_GET_ACCOUNTS);
            }
        }
    }


    protected boolean checkForReadPhoneStatePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//                String[] permissions = new String[]{Manifest.permission.READ_PHONE_STATE};
//                ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS_READ_PHONE_STATE);
                return hasReadPhoneStatePermissions = false;
            } else {
                return hasReadPhoneStatePermissions = true;
            }
        } else {
            return hasReadPhoneStatePermissions = true;
        }
    }

    protected void getReadPhoneStatePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                String[] permissions = new String[]{Manifest.permission.READ_PHONE_STATE};
                ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS_READ_PHONE_STATE);
            }
        }
    }

    protected boolean checkForCallPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                String[] permissions = new String[]{Manifest.permission.READ_PHONE_STATE};
//                ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS_READ_PHONE_STATE);
                return hasCallPermissions = false;
            } else {
                return hasCallPermissions = true;
            }
        } else {
            return hasCallPermissions = true;
        }
    }

    protected void getCallPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                String[] permissions = new String[]{Manifest.permission.CALL_PHONE};
                ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS_CALL);
            }
        }
    }

    protected boolean checkForSMSPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
//                String[] permissions = new String[]{Manifest.permission.READ_PHONE_STATE};
//                ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS_READ_PHONE_STATE);
                return hasSMSPermissions = false;
            } else {
                return hasSMSPermissions = true;
            }
        } else {
            return hasSMSPermissions = true;
        }
    }

    protected void getSMSPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                String[] permissions = new String[]{Manifest.permission.SEND_SMS};
                ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS_SMS);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSIONS) {
            hasAllPermissions = grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED
                    && grantResults[3] == PackageManager.PERMISSION_GRANTED
                    && grantResults[4] == PackageManager.PERMISSION_GRANTED
                    && grantResults[5] == PackageManager.PERMISSION_GRANTED
                    && grantResults[6] == PackageManager.PERMISSION_GRANTED
                    && grantResults[7] == PackageManager.PERMISSION_GRANTED
                    && grantResults[8] == PackageManager.PERMISSION_GRANTED
                    && grantResults[9] == PackageManager.PERMISSION_GRANTED;
            try {
                setPermissionCheckStatus(requestCode, hasAllPermissions);
            } catch (Exception ignored) {
            }
        }
        if (requestCode == REQUEST_PERMISSIONS_LOCATION) {
            if (grantResults.length == 2) {
                hasLocationPermissions = grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED;
                try {
                    setPermissionCheckStatus(requestCode, hasLocationPermissions);
                } catch (Exception ignored) {
                }
            }
        }
        if (requestCode == REQUEST_PERMISSIONS_READ_WRITE) {
            if (grantResults.length == 2) {
                hasReadWritePermissions = grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED;
                try {
                    setPermissionCheckStatus(requestCode, hasReadWritePermissions);
                } catch (Exception ignored) {
                }
            }
        }
        if (requestCode == REQUEST_PERMISSIONS_READ_CONTACTS) {
            if (grantResults.length == 1) {
                hasReadContactsPermissions = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                try {
                    setPermissionCheckStatus(requestCode, hasReadContactsPermissions);
                } catch (Exception ignored) {
                }
            }
        }
        if (requestCode == REQUEST_PERMISSIONS_GET_ACCOUNTS) {
            if (grantResults.length == 1) {
                hasGetAccountsPermissions = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                try {
                    setPermissionCheckStatus(requestCode, hasGetAccountsPermissions);
                } catch (Exception ignored) {
                }
            }
        }


        if (requestCode == REQUEST_PERMISSIONS_READ_PHONE_STATE) {
            if (grantResults.length == 1) {
                hasReadPhoneStatePermissions = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                try {
                    setPermissionCheckStatus(requestCode, hasReadPhoneStatePermissions);
                } catch (Exception ignored) {
                }
            }
        }

        if (requestCode == REQUEST_PERMISSIONS_CALL) {
            if (grantResults.length == 1) {
                hasCallPermissions = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                try {
                    setPermissionCheckStatus(requestCode, hasCallPermissions);
                } catch (Exception ignored) {
                }
            }
        }
        if (requestCode == REQUEST_PERMISSIONS_SMS) {
            if (grantResults.length == 1) {
                hasSMSPermissions = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                try {
                    setPermissionCheckStatus(requestCode, hasSMSPermissions);
                } catch (Exception ignored) {
                }
            }
        }
    }

    private void setPermissionCheckStatus(int requestCode, boolean hasPermission) {
        for (PermissionListener permissionListener : permissionListeners) {
            permissionListener.onPermissionCheckCompleted(requestCode, hasPermission);
        }
    }

    protected boolean checkLocationSettingsStatus() {

        int locationMode = 0;
        String locationProviders;
        boolean isLocationServiceEnabled = false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
            isLocationServiceEnabled = (locationMode != Settings.Secure.LOCATION_MODE_OFF);
        } else {
            locationProviders = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            isLocationServiceEnabled = !TextUtils.isEmpty(locationProviders);
        }

        if (!isLocationServiceEnabled) {
            // notify user
            if (!isFinishing()) {
                if (!isLocationServiceEnableRequestShown) {
                    Log.i(TAG, "checkLocationSettingsStatus: Popup Init :");
                    isLocationServiceEnableRequestShown = true;
                    PopupMessage popupMessage = new PopupMessage(this);
                    popupMessage.show(getString(R.string.message_please_enable_location_service_from_the_settings),
                            0, getString(R.string.btn_open_settings));
                    popupMessage.setPopupActionListener(new PopupMessage.PopupActionListener() {
                        @Override
                        public void actionCompletedSuccessfully(boolean result) {
                            Log.d(TAG, "actionCompletedSuccessfully: Settings Button clicked : ");
                            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(myIntent);
                            isLocationServiceEnableRequestShown = false;
                        }

                        @Override
                        public void actionFailed() {
                            isLocationServiceEnableRequestShown = false;
                        }
                    });
                }
            }
        }
        return isLocationServiceEnabled;
    }


}

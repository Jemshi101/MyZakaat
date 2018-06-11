package com.quartzbit.myzakaat.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.navigation.NavigationView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.HapticFeedbackConstants;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;

import com.quartzbit.myzakaat.R;
import com.quartzbit.myzakaat.app.App;
import com.quartzbit.myzakaat.config.Config;
import com.quartzbit.myzakaat.firebase.FirebaseDataManager;
import com.quartzbit.myzakaat.listeners.LocationUpdateListener;
import com.quartzbit.myzakaat.listeners.PermissionListener;
import com.quartzbit.myzakaat.listeners.ProfileListener;
import com.quartzbit.myzakaat.model.BasicBean;
import com.quartzbit.myzakaat.model.ProfileBean;
import com.quartzbit.myzakaat.net.WSAsyncTasks.LocationNameTask;
import com.quartzbit.myzakaat.widgets.CustomTextView;
import com.quartzbit.myzakaat.widgets.TextViewWithImage;

public class BaseAppCompatActivity extends BaseActivity {
    private static final String TAG = "BaseAppCompatActivity";

    FrameLayout lytContent;
    //protected FrameLayout leftDrawer;
    NavigationView leftDrawer;
    BottomNavigationView bottomBar;
    //	protected FrameLayout rightDrawer;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    //protected  RelativeLayout lytContents;

    final Handler mHandler = new Handler();

    protected SwipeRefreshLayout swipeView;
    protected CoordinatorLayout coordinatorLayout;
    protected Toolbar toolbar;

    private MenuItem menuProgress;
    private View lytDrawer;
    private View lytDrawerHeader;
    private View lytProgress;
    private ProgressBar progressBase;
    private View lytMessage;
    private TextViewWithImage txtMessage;
    private ImageView ivMessage;

    private ImageView ivUserDP;
    private ProfileBean profileBean;
    private CustomTextView txtName;
    private CustomTextView txtEmail;


    private TextView txtTitle;


    /*	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.layout_base);

        getActionBar().setDisplayShowHomeEnabled(false);
            getActionBar().setDisplayShowTitleEnabled(false);

    }
     */
    private void initViewBase() {

        initBase();

        //	getActionBar().setHomeButtonEnabled(true);

//        FacebookSdk.sdkInitialize(this.getApplicationContext());

        LocationUpdateListener locationUpdateListener = new LocationUpdateListener() {
            @Override
            public void onLocationUpdated(Location location) {
                if (Config.getInstance().isOnline()
                        && (Config.getInstance().getDCurrentLatitude() == 0.0
                        || Config.getInstance().getDCurrentLatitude() == location.getLatitude())
                        && (Config.getInstance().getDCurrentLongitude() == 0.0
                        || Config.getInstance().getDCurrentLongitude() == location.getLongitude())) {

//            performDriverLocationUpdate(location);
                }
            }
        };
        addLocationUpdateListener(locationUpdateListener);

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionCheckCompleted(int requestCode, boolean isPermissionGranted) {
                if (requestCode == REQUEST_PERMISSIONS_LOCATION) {
                    if (isPermissionGranted) {
                        if (checkLocationSettingsStatus()) {
                            getCurrentLocation();
                        }
                    }
                }
            }
        };
        addPermissionListener(permissionListener);


        lytContent = (FrameLayout) findViewById(R.id.lyt_contents_appcompat);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorlayout_base_appcompat);

        if (isDarkToolbar()) {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            View lytToolbarDark = getLayoutInflater().inflate(R.layout.toolbar_dark, coordinatorLayout);
            Toolbar toolbarDark = lytToolbarDark.findViewById(R.id.toolbar_dark);
            coordinatorLayout.removeView(toolbar);
            setSupportActionBar(toolbarDark);
            toolbar = toolbarDark;
            txtTitle = (TextView) lytToolbarDark.findViewById(R.id.txt_toolbar_dark_title);
        } else {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            txtTitle = (TextView) toolbar.findViewById(R.id.txt_toolbar_title);
        }

        //leftDrawer = (FrameLayout) findViewById(R.id.leftDrawer);
        leftDrawer = (NavigationView) findViewById(R.id.navigation_view_base_appcompat);
        bottomBar = (BottomNavigationView) findViewById(R.id.bottom_navigation_base_appcompat);
        //	rightDrawer = (FrameLayout)findViewById(R.id.rightDrawer);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_appcompat);


//        drawerLayout.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            toolbar.setPadding(0, App.getStatusBarHeight(getApplicationContext()), 0, 0);
            leftDrawer.setPadding(0, 0, 0, 0);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }

        lytProgress = findViewById(R.id.lyt_progress_base_appcompat);
        progressBase = (ProgressBar) findViewById(R.id.progress_base_appcompat);

        lytMessage = findViewById(R.id.lyt_default_message_base_appcompat);
        txtMessage = (TextViewWithImage) findViewById(R.id.txt_default_message_base_appcompat);
        ivMessage = (ImageView) findViewById(R.id.iv_default_message_base_appcompat);

        swipeView = (SwipeRefreshLayout) findViewById(R.id.swipe_base_appcompat);
        swipeView.setColorSchemeResources(R.color.holo_blue_dark, R.color.holo_blue_light,
                R.color.holo_green_light, R.color.holo_orange_light);
        swipeView.setEnabled(false);
        swipeView.setProgressViewOffset(false, 0,
                (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24,
                        getResources().getDisplayMetrics()) + mActionBarHeight));

        //	llBottomBarActionPopup=(LinearLayout)findViewById(R.id.ll_bottombar_popmenu);

        FrameLayout.LayoutParams param1 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        FrameLayout.LayoutParams param2 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        param1.bottomMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 65, r.getDisplayMetrics());
        param2.bottomMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 155, r.getDisplayMetrics());

        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.btn_open, R.string.btn_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                /*				if(view == rightDrawer) {
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, leftDrawer);
				} else if(view == leftDrawer) {
					drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, rightDrawer);
				}*/
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                /*				if(drawerView == rightDrawer) {
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, leftDrawer);
				} else if(drawerView == leftDrawer) {
					drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, rightDrawer);
				}*/
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        // Set the drawer toggle as the DrawerListener
        drawerLayout.addDrawerListener(mDrawerToggle);
        drawerLayout.setDrawerShadow(R.drawable.shadow,
                GravityCompat.START);

//        App.checkForToken(getApplicationContext());
        /*		Thread.setDefaultUncaughtExceptionHandler(this);
         */
        //	setupRightDrawer();
        setLeftDrawer();
        setBottomBar();
        setUser();
        disableShiftMode(bottomBar);
        /*		mHandler.postDelayed(notiNotiTask, 1000);
        mHandler.postDelayed(notiReqTask, 2000);*/

        setMessageScreenVisibility(false, false, false, R.drawable.logo_splash, getString(R.string.label_nothing_to_show));
        setProgressScreenVisibility(false, false);

    }

    public void onToolbarHomeClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        if (!drawerLayout.isDrawerOpen(leftDrawer)) {
            drawerLayout.openDrawer(leftDrawer);
        } else {
            drawerLayout.closeDrawer(leftDrawer);
        }
    }

    public void onToolbarSearchClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

/*        startActivity(new Intent(this, SearchActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT));*/

    }

/*    @Override
    protected void onRestart() {
//        getWindow().setBackgroundDrawableResource(getWindowBackgroudResourse());
        super.onRestart();
    }*/


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    protected void onResume() {
        super.onResume();

        drawerLayout.closeDrawers();

        //if(Config.getInstance().getAuthToken()==null||Config.getInstance().getAuthToken().equals(""))
//        leftDrawer.setCheckedItem(getSelectedNavigationDrawerItem(CURRENT_ACTIVITY));

/*
        if (!Config.getInstance().isLoggedIn() || Config.getInstance().getAuthToken() == null
                || Config.getInstance().getAuthToken().equals("")) {
            removeLoginDrawerFromNavigationView();
            addLoginDrawerToNavigationView();
        } else {
            removeLoginDrawerFromNavigationView();
        }

       */
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            if (App.isNetworkAvailable()) {
                fetchProfile(FirebaseAuth.getInstance().getCurrentUser().getUid());
            }
        }

        setBottomBarSelection();

    }

    protected void setTitle(int title, int color) {
        if (title != 0)
            txtTitle.setText(getString(title));
        if (color != 0)
            txtTitle.setTextColor(ContextCompat.getColor(getApplicationContext(), color));
    }

    protected void setTitle(String title, int color) {
        txtTitle.setText(title);
        if (color != 0)
            txtTitle.setTextColor(ContextCompat.getColor(getApplicationContext(), color));
    }

    @Override
    public void setContentView(final int layoutResID) {
        DrawerLayout lytBase = (DrawerLayout) getLayoutInflater().inflate(R.layout.layout_base_appcompat, null);
        lytContent = (FrameLayout) lytBase.findViewById(R.id.lyt_contents_appcompat);
        getLayoutInflater().inflate(layoutResID, lytContent, true);
        super.setContentView(lytBase);
        initViewBase();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_base, menu);
/*        menuProgress = menu.findItem(R.id.action_progress);
        // Extract the action-view from the menu item
        ProgressBar progressActionBar = (ProgressBar) MenuItemCompat.getActionView(menuProgress);
        progressActionBar.setIndeterminate(true);*/
        /*		menuNotiItem = menu.findItem(R.id.action_notifications);
        MenuItemCompat.setActionView(menuNotiItem, R.layout.actionbar_notification_icon);
		menuNoti = (View) MenuItemCompat.getActionView(menuNotiItem);
		txtNoti = (TextView) menuNoti.findViewById(R.id.txt_action_notification);
		 *//*		menuNoti.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				mVibrator.vibrate(25);
				if(isNotificationVisible){
					isNotificationVisible=false;
					notificationDrawer.setVisibility(View.GONE);
				}else{
					isNotificationVisible=true;
					notificationDrawer.setVisibility(View.VISIBLE);
				}
			}
		});*/

        setMenuIconColor(menu, R.color.white);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
//                mVibrator.vibrate(25);
                if (drawerLayout.isDrawerOpen(leftDrawer)) {
                    drawerLayout.closeDrawer(leftDrawer);
                } else {
                    drawerLayout.openDrawer(leftDrawer);
                }
                return true;
           /* case R.id.action_search:
                drawerLayout.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
//                mVibrator.vibrate(25);

                startActivity(new Intent(this, SearchActivity.class));
//                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                return true;*/

        }
        return super.onOptionsItemSelected(item);
    }

    private void setLeftDrawer() {

        LayoutInflater inflater = getLayoutInflater();
        lytDrawer = inflater.inflate(R.layout.layout_drawer, null);
//        leftDrawer.addView(lytDrawer);

        ivUserDP = (ImageView) lytDrawer.findViewById(R.id.iv_drawer_profile_photo);
        txtName = (CustomTextView) lytDrawer.findViewById(R.id.txt_drawer_name);
        txtEmail = (CustomTextView) lytDrawer.findViewById(R.id.txt_drawer_email);

//        txtName.setTextStyle(LatoTextStyle.BOLD);

       /* etxtPassword.setTypeface(typeface);
        etxtPassword.setTransformationMethod(new PasswordTransformationMethod());
*/
        addLoginDrawerToNavigationView();

    }

    private void addLoginDrawerToNavigationView() {
        leftDrawer.addView(lytDrawer);
    }

    private void removeLoginDrawerFromNavigationView() {
        leftDrawer.removeView(lytDrawer);
    }

    void setUser() {

        txtName.setText(Config.getInstance().getName());
        txtEmail.setText(Config.getInstance().getEmail());

        Log.i(TAG, "setUser: Profile Photo : " + Config.getInstance().getProfilePhoto());

        Glide.with(getApplicationContext())
                .load(Config.getInstance().getProfilePhoto())
                .apply(new RequestOptions()
                        .error(R.drawable.ic_profile_photo_default)
                        .fallback(R.drawable.ic_profile_photo_default)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .circleCrop())
                .into(ivUserDP);

    }

    private void setBottomBar() {

        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                bottomBar.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                //mVibrator.vibrate(25);
//                disableLabel(bottomBar);
                switch (item.getItemId()) {
                    case R.id.action_home:
                        if (getCurrentActivity() != HOME_ACTIVITY)
                            startActivity(new Intent(BaseAppCompatActivity.this, HomeActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                        break;
                    case R.id.action_chat:
                        if (getCurrentActivity() != CHAT_ACTIVITY)
                            startActivity(new Intent(BaseAppCompatActivity.this, RecentChatListActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                        break;
                    case R.id.action_trees:
                        if (getCurrentActivity() != TREES_ACTIVITY)
                            startActivity(new Intent(BaseAppCompatActivity.this, TreeListActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                        break;
                    case R.id.action_notifications:
                        if (getCurrentActivity() != NOTIFICATIONS_ACTIVITY)
                            startActivity(new Intent(BaseAppCompatActivity.this, NotificationsActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                        break;
                    case R.id.action_profile:
                        if (getCurrentActivity() != PROFILE_ACTIVITY)
                            startActivity(new Intent(BaseAppCompatActivity.this, ProfileViewActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                        break;
                }

                overridePendingTransition(R.anim.fade_in, R.anim.disappear);
                return true;
            }
        });

    }

    private void setBottomBarSelection() {

        switch (getCurrentActivity()) {
            case HOME_ACTIVITY:
                bottomBar.setSelectedItemId(R.id.action_home);
                break;
            case CHAT_ACTIVITY:
                bottomBar.setSelectedItemId(R.id.action_chat);
                break;
            case TREES_ACTIVITY:
                bottomBar.setSelectedItemId(R.id.action_trees);
                break;
            case NOTIFICATIONS_ACTIVITY:
                bottomBar.setSelectedItemId(R.id.action_notifications);
                break;
            case PROFILE_ACTIVITY:
                bottomBar.setSelectedItemId(R.id.action_profile);
                break;
            default:
                bottomBar.setSelectedItemId(R.id.action_home);
                break;
        }

    }


    public void onLogoutClick(View v) {
        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
//        mVibrator.vibrate(25);

        App.logout();
        startActivity(new Intent(this, SplashActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();

        /*SharedPreferences preferences = getSharedPreferences(AppConstants.PREFERENCE_NAME_SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        //editor.remove(AppConstants.PREFERENCE_KEY_SESSION_TOKEN);
        editor.commit();

        Config.clear();

        new DBHandler(getApplicationContext()).clearDatabase();
        clearApplicationData();

//        restart(this, 500);

*//*        startActivity(new Intent(this, HomeActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));*//*
        finish();*/
    }


    public void onDrawerProfileEditClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
//        mVibrator.vibrate(25);

//        drawerLayout.closeDrawers();
        startActivity(new Intent(BaseAppCompatActivity.this, ProfileViewActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
//        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

    }

    public void onDrawerAboutUsClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
//        mVibrator.vibrate(25);

//        drawerLayout.closeDrawers();
/*        startActivity(new Intent(BaseAppCompatActivity.this, HomeActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));*/
//        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

    }

    public void onDrawerHelpClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
//        mVibrator.vibrate(25);

//        drawerLayout.closeDrawers();
/*        startActivity(new Intent(BaseAppCompatActivity.this, HomeActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));*/
//        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);


    }

    public void onDrawerShareAppClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        //mVibrator.vibrate(25);

    }

    public void onDrawerFeedbackClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
//        mVibrator.vibrate(25);

//        drawerLayout.closeDrawers();
/*        startActivity(new Intent(BaseAppCompatActivity.this, HomeActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));*/
//        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

    }

    public void onDrawerSettingsClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
//        mVibrator.vibrate(25);

//        drawerLayout.closeDrawers();
        /*startActivity(new Intent(BaseAppCompatActivity.this, SettingsActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));*/
//        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

    }

    public static void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                //noinspection RestrictedApi
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                //noinspection RestrictedApi
                item.setChecked(item.getItemData().isChecked());
//                removeTextLabel(view, item);
            }
        } catch (NoSuchFieldException e) {
            Log.e("BNVHelper", "Unable to get shift mode field", e);
        } catch (IllegalAccessException e) {
            Log.e("BNVHelper", "Unable to change value of shift mode", e);
        }
    }

/*    private static void removeTextLabel(@NonNull BottomNavigationView bottomNavigationView, View view) {
        if (view == null) return;
        if (view instanceof MenuView.ItemView) {
            ViewGroup viewGroup = (ViewGroup) view;
            int padding = 0;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View v = viewGroup.getChildAt(i);
                if (v instanceof ViewGroup) {
                    padding = v.getHeight();
                    viewGroup.removeViewAt(i);
                }
            }
            viewGroup.setPadding(view.getPaddingLeft(), (viewGroup.getPaddingTop() + padding) / 2, view.getPaddingRight(),
                    view.getPaddingBottom());
        }
    }

    public static void disableLabel(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
            removeTextLabel(view, item);
            ImageView icon = (ImageView) item.findViewById(R.id.icon);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) icon.getLayoutParams();
            params.gravity = Gravity.CENTER;
            icon.setLayoutParams(params);
        }
    }*/


    public void onBaseAppCompatDummyClick(View view) {

    }

    private void fetchProfile(String uid) {

        Log.i(TAG, "fetchProfile: USER : " + uid);


//        setProgressScreenVisibility(true, true);
//        swipeView.setRefreshing(true);

        JSONObject postData = getProfileJSObj(uid);

        FirebaseDataManager.fetchProfileFromUID(postData, new ProfileListener() {

            @Override
            public void onLoadCompleted(boolean isExist, ProfileBean profileBeanWS) {

                if (isExist) {
                    Log.i(TAG, "onLoadCompleted:  PROFILE BEAN : " + new Gson().toJson(profileBeanWS));
                    profileBean = profileBeanWS;
                    populateProfile(profileBean);
                    App.saveToken();
                    /*startActivity(new Intent(BaseAppCompatActivity.this, CreateProfileActivity.class)
                            .putExtra("profileBean", profileBean));
                    finish();*/
                }
            }

            @Override
            public void onLoadFailed(String error) {
            }
        });

    }

    private JSONObject getProfileJSObj(String uid) {

        JSONObject postData = new JSONObject();
        try {
            postData.put("uid", uid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return postData;
    }

    private void populateProfile(ProfileBean profileBean) {

        try {
//            Config.getInstance().setAuthToken(profileBean.getAuthToken());
            Config.getInstance().setName(profileBean.getName());
            Config.getInstance().setFirstName(profileBean.getFirstName());
            Config.getInstance().setLastName(profileBean.getLastName());
            Config.getInstance().setProfilePhoto(profileBean.getProfilePhoto());
//            Config.getInstance().setCoverPic(profileBean.getCoverPhoto());
            Config.getInstance().setUserID(profileBean.getId());
            Config.getInstance().setPhone(profileBean.getPhone());
            Config.getInstance().setAddress(profileBean.getAddress());
            Config.getInstance().setEmail(profileBean.getEmail());
            //saveCoverPhotoToFile(profileBean.getCoverPhoto());
            setUser();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void clearApplicationData() {
        File cache = getApplication().getFilesDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                //		if (!s.equals("lib")) {
                (new File(appDir, s)).delete();
                //		}
            }
        }
    }

    /*
    private void saveToken() {
        System.out.println("SAVE STARTED");
        SharedPreferences preferences = getSharedPreferences(AppConstants.PREFERENCE_NAME_SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_TOKEN, Config.getInstance().getAuthToken());
        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_USERID, Config.getInstance().getUserID());
        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_EMAIL, Config.getInstance().getEmail());
        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_PASSWORD, Config.getInstance().getPassword());
        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_GENDER, Config.getInstance().getGender());
        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_DOB, Config.getInstance().getDob());
        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_FBACCESSTOKEN, Config.getInstance().getFacebookAccessToken());
        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_GOOGLEPLUSACCESSTOKEN, Config.getInstance().getGoogleAccessToken());
        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_GCM_ID, Config.getInstance().getGCMID());
        editor.commit();
        fop.writeHash();
        System.out.println("SAVE COMPLETE");
    }
*/

/*

    int getServerConnectionAvailableStatus(boolean isSnackbarEnabled) {
        if (Config.getInstance().getAuthToken() == null || Config.getInstance().getAuthToken().equals("")) {
            if (App.checkForToken(getApplicationContext()) && !Config.getInstance().getAuthToken().equals("")) {
                if (Config.getInstance().isPhoneVerified()) {
                    if (App.isNetworkAvailable(getApplicationContext())) {
                        return SERVER_CONNECTION_AVAILABLE;
                    } else {
                        if (isSnackbarEnabled)
                            Snackbar.make(coordinatorLayout, AppConstants.NO_NETWORK_AVAILABLE, Snackbar.LENGTH_LONG)
                                    .setAction(R.string.btn_dismiss, snackBarDismissOnClickListener).show();
                        return NETWORK_NOT_AVAILABLE;
                    }
                } else {
                    startActivity(new Intent(this, VerificationActivity.class));
                    return AUTH_TOKEN_NOT_AVAILABLE;
                }
            } else {
                if (isSnackbarEnabled)
                    Snackbar.make(coordinatorLayout, AppConstants.WEB_ERROR_MSG, Snackbar.LENGTH_LONG)
                            .setAction(R.string.btn_dismiss, snackBarDismissOnClickListener).show();
                return AUTH_TOKEN_NOT_AVAILABLE;
            }
        } else {
            if (App.isNetworkAvailable(getApplicationContext())) {
                return SERVER_CONNECTION_AVAILABLE;
            } else {
                if (isSnackbarEnabled)
                    Snackbar.make(coordinatorLayout, AppConstants.NO_NETWORK_AVAILABLE, Snackbar.LENGTH_LONG)
                            .setAction(R.string.btn_dismiss, snackBarDismissOnClickListener).show();
                return NETWORK_NOT_AVAILABLE;
            }
        }
    }
*/

    protected void setMessageScreenVisibility(boolean isScreenVisible, boolean isImageVisible,
                                              boolean isTransparentBackground, int imagePath, String message) {
        if (isScreenVisible) {
            lytMessage.setVisibility(View.VISIBLE);
            txtMessage.setText(message);
            if (isImageVisible) {
                ivMessage.setVisibility(View.VISIBLE);
                ivMessage.setImageResource(imagePath);
 /*               Glide.with(getApplicationContext())
                        .load(imagePath)
                        .into(ivMessage);*/
            } else {
                ivMessage.setVisibility(View.GONE);
            }
        } else
            lytMessage.setVisibility(View.GONE);

        if (isTransparentBackground) {
            lytMessage.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.transparent));
        } else {
            lytMessage.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        }
    }

    protected void setProgressScreenVisibility(boolean isScreenVisible, boolean isProgressVisible) {
        if (isScreenVisible) {
            lytProgress.setVisibility(View.VISIBLE);
            if (isProgressVisible) {
                progressBase.setVisibility(View.VISIBLE);
            } else {
                progressBase.setVisibility(View.GONE);
            }
        } else
            lytProgress.setVisibility(View.GONE);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (drawerLayout.isDrawerOpen(leftDrawer)) {
                drawerLayout.closeDrawer(leftDrawer);
            }
            /*			else if(drawerLayout.isDrawerOpen(rightDrawer)){
                drawerLayout.closeDrawer(rightDrawer);
			}*/
            else {
                onBackPressed();
//                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        }

        if (keyCode == KeyEvent.KEYCODE_MENU) {
            openOptionsMenu();
        }
        return true;
    }

    /* private void performDriverLocationUpdate(Location location) {

         JSONObject postData = getUpdateDriverLocationJSObj(location);

         DataManager.performUpdateDriverLocation(postData, new BasicListener() {
             @Override
             public void onLoadCompleted(BasicBean basicBean) {

             }

             @Override
             public void onLoadFailed(String error) {

             }
         });


     }

     private JSONObject getUpdateDriverLocationJSObj(Location location) {
         JSONObject postData = new JSONObject();

         try {
             postData.put("latitude", location.getLatitude());
             postData.put("longitude", location.getLongitude());
         } catch (JSONException e) {
             e.printStackTrace();
         }

         return postData;
     }
 */
    private void getLocationName() {

//        swipeView.setRefreshing(true);

        String currentLatitude = Config.getInstance().getCurrentLatitude();
        String currentLongitude = Config.getInstance().getCurrentLongitude();

        System.out.println("Current Location : " + currentLatitude + "," + currentLongitude);

        HashMap<String, String> urlParams = new HashMap<>();
        //	postData.put("uid", id);
        urlParams.put("latlng", currentLatitude + "," + currentLongitude);
        urlParams.put("sensor", "true");

        LocationNameTask locationNameTask = new LocationNameTask(urlParams);
        locationNameTask.setLocationNameTaskListener(new LocationNameTask.LocationNameTaskListener() {

            @Override
            public void dataDownloadedSuccessfully(BasicBean basicBean) {
                System.out.println("Location Name Retrieved : " + basicBean.getAddress());
                Config.getInstance().setCurrentLocation(basicBean.getAddress());
                    /*					txtLocation.setText(address);
                    Toast.makeText(CreateActivity.this,"Location Name Retrieved : "+address, Toast.LENGTH_SHORT).show();
					 */
            }

            @Override
            public void dataDownloadFailed() {

            }
        });
        locationNameTask.execute();
    }


    public void onDrawerMyDearestCountriesClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        //mVibrator.vibrate(25);

        startActivity(new Intent(BaseAppCompatActivity.this, DearestCountryListActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));

    }

    public void onDrawerMyDearestFriendsClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        //mVibrator.vibrate(25);

        startActivity(new Intent(BaseAppCompatActivity.this, DearestFriendsActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));

    }

    public void onDrawerSearchClick(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        //mVibrator.vibrate(25);

        startActivity(new Intent(BaseAppCompatActivity.this, SearchCategoriesActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));

    }
}
package com.quartzbit.myzakaat.activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.widget.Toolbar;
import android.util.TypedValue;
import android.view.HapticFeedbackConstants;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.quartzbit.myzakaat.R;
import com.quartzbit.myzakaat.app.App;
import com.quartzbit.myzakaat.config.Config;
import com.quartzbit.myzakaat.util.AppConstants;
import com.quartzbit.myzakaat.util.FileOp;
import com.quartzbit.myzakaat.widgets.TextViewWithImage;

import static com.quartzbit.myzakaat.app.App.NETWORK_NOT_AVAILABLE;
import static com.quartzbit.myzakaat.app.App.SERVER_CONNECTION_AVAILABLE;


public class BaseAppCompatNoDrawerActivity extends BaseActivity {

    protected FileOp fop = new FileOp(this);

    protected CoordinatorLayout lytBase;
    protected FrameLayout lytContent;
    protected SwipeRefreshLayout swipeView;
    protected CoordinatorLayout coordinatorLayout;
    protected Toolbar toolbar;
    protected View lytProgress;
    private ProgressBar progressBase;
    private View lytMessage;
    private TextViewWithImage txtMessage;
    private ImageView ivMessage;
    protected TextView txtTitle;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
/*
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                isBluetoothEnableRequestShown = false;
            } else {
                isBluetoothEnableRequestShown = false;
                Snackbar.make(coordinatorLayout, R.string.message_bluetooth_not_enabled, Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.btn_dismiss, snackBarDismissOnClickListener).show();
            }
        }
*/

    }

    public void initViewBase() {

        initBase();
        //	getActionBar().setHomeButtonEnabled(true);

        //FacebookSdk.sdkInitialize(this.getApplicationContext());


        lytContent = (FrameLayout) findViewById(R.id.lyt_contents_base_appcompat_no_drawer);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorlayout_base_appcompat_no_drawer);

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            lytBase.setPadding(0, App.getStatusBarHeight(), 0, 0);
//            toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
        }

        lytProgress = findViewById(R.id.lyt_progress_base);
        progressBase = (ProgressBar) findViewById(R.id.progress_base_appcompat_no_drawer);

        lytMessage = findViewById(R.id.lyt_default_message_base_appcompat_no_drawer);
        txtMessage = (TextViewWithImage) findViewById(R.id.txt_default_message_base_appcompat_no_drawer);
        ivMessage = (ImageView) findViewById(R.id.iv_default_message_base_appcompat_no_drawer);

        swipeView = (SwipeRefreshLayout) findViewById(R.id.swipe_base_appcompat_no_drawer);
        swipeView.setColorSchemeResources(android.R.color.holo_blue_dark, android.R.color.holo_blue_light,
                android.R.color.holo_green_light, android.R.color.holo_orange_light);
        swipeView.setEnabled(false);
        swipeView.setProgressViewOffset(false, 0,
                (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24,
                        getResources().getDisplayMetrics()) + mActionBarHeight));



  /*      if (android.os.Build.VERSION.SDK_INT >= 21) {
            swipeView.setPadding(0, (int) (getStatusBarHeight()), 0, 0);
        }*/

        setMessageScreenVisibility(false, false, false,
                R.drawable.logo_splash, getString(R.string.label_nothing_to_show));
        setProgressScreenVisibility(false, false);

    }

    void setToolbar(int toolbarTemp) {


        /*coordinatorLayout.removeView(toolbar);
        toolbar = (Toolbar) getLayoutInflater().inflate(R.layout.layout_toolbar_dark, toolbar);
        coordinatorLayout.addView(toolbar, 0);
        toolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        toolbar.getContext().setTheme(R.style.ToolbarStyleDark);
        setSupportActionBar(toolbar);
        txtTitle = (TextView) toolbar.findViewById(R.id.txt_toolbar_title);*/

    }


    @Override
    public void setContentView(final int layoutResID) {
        lytBase = (CoordinatorLayout) getLayoutInflater().inflate(R.layout.layout_base_appcompat_no_drawer, null);
        lytContent = (FrameLayout) lytBase.findViewById(R.id.lyt_contents_base_appcompat_no_drawer);
        getLayoutInflater().inflate(layoutResID, lytContent, true);
        super.setContentView(lytBase);
        initViewBase();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_base, menu);
        setMenuIconColor(menu, R.color.white);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                lytContent.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
//                mVibrator.vibrate(25);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);

    }

    int getServerConnectionAvailableStatus(boolean isSnackbarEnabled) {
        if (Config.getInstance().getAuthToken() == null || Config.getInstance().getAuthToken().equals("")) {
            if (App.checkForToken() && !Config.getInstance().getAuthToken().equals("")) {
                if (App.isNetworkAvailable()) {
                    return SERVER_CONNECTION_AVAILABLE;
                } else {
                    if (isSnackbarEnabled)
                        Snackbar.make(coordinatorLayout, AppConstants.NO_NETWORK_AVAILABLE, Snackbar.LENGTH_LONG)
                                .setAction(R.string.btn_dismiss, snackBarDismissOnClickListener).show();
                    return NETWORK_NOT_AVAILABLE;
                }
            } else {
                if (isSnackbarEnabled)
                    Snackbar.make(coordinatorLayout, AppConstants.WEB_ERROR_MSG, Snackbar.LENGTH_LONG)
                            .setAction(R.string.btn_dismiss, snackBarDismissOnClickListener).show();
                return App.AUTH_TOKEN_NOT_AVAILABLE;
            }
        } else {
            if (App.isNetworkAvailable()) {
                return SERVER_CONNECTION_AVAILABLE;
            } else {
                if (isSnackbarEnabled)
                    Snackbar.make(coordinatorLayout, AppConstants.NO_NETWORK_AVAILABLE, Snackbar.LENGTH_LONG)
                            .setAction(R.string.btn_dismiss, snackBarDismissOnClickListener).show();
                return NETWORK_NOT_AVAILABLE;
            }
        }
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
            lytMessage.setClickable(false);
        } else {
            lytMessage.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
            lytMessage.setClickable(true);
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

    public void onBaseAppCompatNoDrawerDummyClick(View view) {

    }

}

package com.quartzbit.myzakaat.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.quartzbit.myzakaat.R
import com.quartzbit.myzakaat.app.App
import com.quartzbit.myzakaat.net.parsers.RequestParser

class SplashActivity : BaseAppCompatNoDrawerActivity() {

    private val TAG = "Splash"
    private var requestID = ""
    private var mHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        lytBase.setFitsSystemWindows(false)
        swipeView.setPadding(0, 0, 0, 0)
        getSupportActionBar()?.hide()
        App.getInstance().setDemo(true)


        if (getIntent().getExtras() != null) {
            for (key in getIntent().getExtras().keySet()) {
                Log.i(TAG, "onCreate: Key : " + key + " Value: " + getIntent().getExtras().get(key))
            }
        }
        if (getIntent().hasExtra("response")) {
            val body = getIntent().getStringExtra("response")
            val requestParser = RequestParser()
            val basicBean = requestParser.parseBasicResponse(body)

            if (basicBean != null) {
                if (basicBean.status =="Success") {
                    //                    initiateDriverRatingService(basicBean.getId());
                    requestID = basicBean.requestID
                }
            }
        }

        mHandler.postDelayed(splashTask, 2000)

        //        getData(false);

    }

    internal var splashTask: Runnable = object : Runnable {
        override fun run() {

            //                Log.i(TAG, "PeriodicTask: " + Config.getInstance().isFirstTime());
            if (App.getInstance().isDemo()) {
                navigate()
                //                startActivity(new Intent(SplashActivity.this, ProfileViewActivity.class));
                //                startActivity(new Intent(SplashActivity.this, TripDetailsActivity.class));
                finish()
            } else {
                navigate()
            }
            //            navigate();
            //            new Handler().postDelayed(splashTask, 2000);
        }
    }

    private fun navigate() {

        if (App.checkForToken() && fop.checkSPHash()) {
            //                    startActivity(new Intent(SplashActivity.this, RegistrationActivity.class));
            if (requestID.equals("", ignoreCase = true)) {

                startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
                //                        startActivity(new Intent(SplashActivity.this, DriverDocumentsActivity.class));

            } else {
                /*startActivity(new Intent(SplashActivity.this, RequestConfirmationActivity.class)
                        .putExtra("request_id", requestID));*/
                //                    startActivity(new Intent(SplashActivity.this, RegistrationActivity.class));
            }
            finish()
        } else {
            App.logout()
            startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
            finish()
        }
    }

}

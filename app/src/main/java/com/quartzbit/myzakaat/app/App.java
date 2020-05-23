package com.quartzbit.myzakaat.app;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.quartzbit.myzakaat.R;
import com.quartzbit.myzakaat.config.Config;
import com.quartzbit.myzakaat.model.AuthBean;
import com.quartzbit.myzakaat.util.AppConstants;
import com.quartzbit.myzakaat.util.FileOp;
import com.quartzbit.myzakaat.util.RobotoTextStyleExtractor;
import com.quartzbit.myzakaat.util.TypefaceManager;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.widget.ActionMenuView;
import androidx.appcompat.widget.Toolbar;
import androidx.multidex.MultiDex;

//import com.digits.sdk.android.Digits;

public class App extends Application {

    public static final int SERVER_CONNECTION_AVAILABLE = 0;
    public static final int NETWORK_NOT_AVAILABLE = 1;
    public static final int AUTH_TOKEN_NOT_AVAILABLE = 2;
    private static final String TAG = "App";

    public static final int DATE_FORMAT_0 = 0;
    public static final int DATE_FORMAT_1 = 1;
    public static final int DATE_FORMAT_2 = 2;
    public static final int DATE_FORMAT_3 = 3;
    public static final int DATE_FORMAT_4 = 4;
    public static final int DATE_FORMAT_5 = 5;
    public static final int DATE_FORMAT_6 = 6;

    public static final int TIME_FORMAT_0 = 0;
    public static final int TIME_FORMAT_1 = 1;

    public static final long HOURS_24 = 86400000;
    public static final long SECOND = 1000;
    public static final long MINUTE = 60000;
    public static final long HOUR = 3600000;


    private final Thread.UncaughtExceptionHandler defaultHandler;

    private static App instance;

    private FileOp fop = new FileOp(this);

    Resources r;
    float px;
    int width;
    int height;

    private GoogleApiClient googleApiClient;

    private boolean isDemo;

    public boolean isDemo() {
        return isDemo;
    }

    public void setDemo(boolean demo) {
        isDemo = demo;
    }

    public GoogleApiClient getGoogleApiClient() {
        return googleApiClient;
    }

    public void setGoogleApiClient(GoogleApiClient googleApiClient) {
        this.googleApiClient = googleApiClient;
    }


    public float getPx() {
        return px;
    }

    public void setPx(float px) {
        this.px = px;
    }

    public App() {
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // setup handler for uncaught exception
        Thread.UncaughtExceptionHandler _unCaughtExceptionHandler = new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {

                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>IN EXCEPTION HANDLER>>>>>>>>>>>>>>>>>>>>>");
                final Writer result = new StringWriter();
                final PrintWriter printWriter = new PrintWriter(result);
                ex.printStackTrace(printWriter);
                printWriter.close();

                ex.printStackTrace();
                Toast.makeText(getApplicationContext(), R.string.message_app_crash, Toast.LENGTH_SHORT).show();
                //	    			restart(getApplicationContext(), 2000);
                //	check(getApplicationContext(), 1);
                fop = new FileOp(getApplicationContext());
                fop.writeDebug(result.toString());
                checkForToken();

                //	android.os.Process.killProcess(android.os.Process.myPid());
                defaultHandler.uncaughtException(thread, ex);
            }
        };
        Thread.setDefaultUncaughtExceptionHandler(_unCaughtExceptionHandler);

    }

    public static App getInstance() {

        if (instance == null)
            instance = new App();
        return instance;
    }


    public static void restart(Context context, int delay) {
        if (delay == 0) {
            delay = 1;
        }
        Intent restartIntent = context.getPackageManager()
                .getLaunchIntentForPackage(context.getPackageName());
        PendingIntent intent = PendingIntent.getActivity(
                context, 0,
                restartIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.set(AlarmManager.RTC, System.currentTimeMillis() + delay, intent);
        System.exit(2);
    }

    public static void check(Context context, int delay) {
        if (delay == 0) {
            delay = 1;
        }
        System.exit(0);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

//        firebaseDB = FirebaseDatabase.getInstance().getReference();
//        FacebookSdk.sdkInitialize(this.getApplicationContext());

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        checkForToken();

        TypefaceManager.addTextStyleExtractor(RobotoTextStyleExtractor.getInstance());
        setDefaultFont();

        r = getResources();
        px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, r.getDisplayMetrics());
        width = r.getDisplayMetrics().widthPixels;
        height = r.getDisplayMetrics().heightPixels;

    }

    private void setDefaultFont() {

        try {
            /*			final Typeface bold = Typeface.createFromAsset(getAssets(), "HelveticaNeueBold.ttf");
            final Typeface italic = Typeface.createFromAsset(getAssets(), "HelveticaNeueItalic.ttf");
			final Typeface boldItalic = Typeface.createFromAsset(getAssets(), "HelveticaNeueBoldItalic.ttf");
			final Typeface regular = Typeface.createFromAsset(getAssets(),"HelveticaNeue.ttf");

			final Typeface normal = Typeface.createFromAsset(getAssets(), "HelveticaNeue.ttf");
			final Typeface monospace = Typeface.createFromAsset(getAssets(), "HelveticaNeue.ttf");
			final Typeface serif = Typeface.createFromAsset(getAssets(), "HelveticaNeue.ttf");
			final Typeface sansSerif = Typeface.createFromAsset(getAssets(), "HelveticaNeue.ttf");
			final Typeface sans = Typeface.createFromAsset(getAssets(), "HelveticaNeue.ttf");
			 */

            final Typeface bold = Typeface.createFromAsset(getAssets(), "Roboto-Bold.ttf");
            final Typeface italic = Typeface.createFromAsset(getAssets(), "Roboto-Italic.ttf");
            final Typeface boldItalic = Typeface.createFromAsset(getAssets(), "Roboto-BoldItalic.ttf");
            final Typeface regular = Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf");

            final Typeface normal = Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf");
            final Typeface monospace = Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");
            final Typeface serif = Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf");
            final Typeface sansSerif = Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf");
            final Typeface sans = Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf");

            Field DEFAULT = Typeface.class.getDeclaredField("DEFAULT");
            DEFAULT.setAccessible(true);
            DEFAULT.set(null, regular);

            Field DEFAULT_BOLD = Typeface.class.getDeclaredField("DEFAULT_BOLD");
            DEFAULT_BOLD.setAccessible(true);
            DEFAULT_BOLD.set(null, bold);

            Field SERIF = Typeface.class.getDeclaredField("SERIF");
            SERIF.setAccessible(true);
            SERIF.set(null, serif);

            Field SANS_SERIF = Typeface.class.getDeclaredField("SANS_SERIF");
            SANS_SERIF.setAccessible(true);
            SANS_SERIF.set(null, sansSerif);

            Field SANS = Typeface.class.getDeclaredField("SANS");
            SANS.setAccessible(true);
            SANS.set(null, sans);

            Field NORMAL = Typeface.class.getDeclaredField("NORMAL");
            NORMAL.setAccessible(true);
            NORMAL.set(null, normal);

            Field MONOSPACE = Typeface.class.getDeclaredField("MONOSPACE");
            MONOSPACE.setAccessible(true);
            MONOSPACE.set(null, monospace);

            Field sDefaults = Typeface.class.getDeclaredField("sDefaults");
            sDefaults.setAccessible(true);
            sDefaults.set(null, new Typeface[]{
                    regular, bold, italic, boldItalic, monospace, serif, sansSerif, normal, sans
            });

        } catch (Throwable e) {
            //cannot crash app if there is a failure with overriding the default font!
            System.out.println(e);
        }
    }

    public static String getImagePath(String path) {
        if (path.equals(""))
            return "";
        else
            return !path.startsWith("http") ? AppConstants.BASE_URL + path : path;
    }

    public static String removePublicFromPath(String path) {
        try {
            String str = path;
            if (path.startsWith("/public/")) {
                str = path.substring("/public/".length());
            } else if (path.startsWith("public/")) {
                str = path.substring("public/".length());
            }
            return str;
        } catch (Exception e) {
            return "";
        }
    }

    public static String removeBaseURL(String path) {
        if (path.startsWith(AppConstants.BASE_URL)) {
            path = "public/" + path.substring(AppConstants.BASE_URL.length());
        }
        return path;
    }

    public static boolean isNetworkAvailable() {
        Context context = getInstance().getApplicationContext();
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    // A method to find height of the status bar
    public static int getStatusBarHeight() {
        Context context = getInstance().getApplicationContext();
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static Calendar getUserTime(long GMTTime) {
        Calendar calTemp = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calTemp.setTimeInMillis(GMTTime * 1000);
        calTemp.setTimeZone(Calendar.getInstance().getTimeZone());
        return calTemp;
    }

    public static Calendar getUserTime(String GMTTime) {
        Calendar calTemp = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy", getCurrentLocale());
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            calTemp.setTime(sdf.parse(GMTTime));
            return calTemp;
        } catch (ParseException ignored) {
        }

        return calTemp;
    }

    public static String getUserTimeFromUnix(String GMTTime) {
        if (GMTTime.equalsIgnoreCase("-62169984000") || GMTTime.equalsIgnoreCase("false") || GMTTime.equalsIgnoreCase("true"))
            return "";
        try {
            Calendar calTemp = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calTemp.setTimeInMillis(Long.valueOf(GMTTime) * 1000);
            calTemp.setTimeZone(Calendar.getInstance().getTimeZone());
            GMTTime = new SimpleDateFormat("MMM dd, yyyy hh:mm a", getCurrentLocale())
                    .format(new Date(calTemp.getTimeInMillis()));
            return GMTTime;
        } catch (Exception e) {
            //	e.printStackTrace();
            return GMTTime;
        }
    }

    public static String getUserTimeFromChatUnix(String GMTTime) {
        if (GMTTime.equalsIgnoreCase("-62169984000") || GMTTime.equalsIgnoreCase("false") || GMTTime.equalsIgnoreCase("true"))
            return "";
        try {
            Calendar calTemp = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calTemp.setTimeInMillis(Long.valueOf(GMTTime));
            calTemp.setTimeZone(Calendar.getInstance().getTimeZone());
            GMTTime = new SimpleDateFormat("MMM dd, yyyy hh:mm a", getCurrentLocale())
                    .format(new Date(calTemp.getTimeInMillis()));
            return GMTTime;
        } catch (Exception e) {
            //	e.printStackTrace();
            return GMTTime;
        }
    }

    public static String getUserDateFromUnix(String GMTTime) {
        if (GMTTime.equalsIgnoreCase("-62169984000") || GMTTime.equalsIgnoreCase("false")
                || GMTTime.equalsIgnoreCase("true"))
            return "";
        try {
            Calendar calTemp = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calTemp.setTimeInMillis(Long.valueOf(GMTTime) * 1000);
            calTemp.setTimeZone(Calendar.getInstance().getTimeZone());
            GMTTime = new SimpleDateFormat("MMM dd, yyyy", getCurrentLocale())
                    .format(new Date(calTemp.getTimeInMillis()));
            return GMTTime;
        } catch (Exception e) {
            //	e.printStackTrace();
            return GMTTime;
        }
    }

    public static String getDateFromUnix(int format, boolean isOriginalGMT, boolean isResultGMT,
                                         long timeInMills, boolean isShorteningNeeded) {
        if (timeInMills <= -62169984000L)
            return "";
        String resultDate = "";
        try {
            Calendar calTemp;
            if (isOriginalGMT) {
                calTemp = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            } else {
                calTemp = Calendar.getInstance();
            }
            calTemp.setTimeInMillis(timeInMills);

            if (isShorteningNeeded && calTemp.before(Calendar.getInstance()) &&
                    Calendar.getInstance().getTimeInMillis() - calTemp.getTimeInMillis() < HOURS_24) {
                return getTimeDifference(calTemp.getTimeInMillis());
            } else {

                SimpleDateFormat sdf;
                switch (format) {
                    case DATE_FORMAT_0:
                        sdf = new SimpleDateFormat("MMM dd, yyyy", getCurrentLocale());
                        sdf.setTimeZone(getTimeZone(isResultGMT));
                        resultDate = sdf.format(new Date(calTemp.getTimeInMillis()));
                        break;
                    case DATE_FORMAT_1:
                        sdf = new SimpleDateFormat("dd/ MM/ yyyy", getCurrentLocale());
                        sdf.setTimeZone(getTimeZone(isResultGMT));
                        resultDate = sdf.format(new Date(calTemp.getTimeInMillis()));
                        break;
                    case DATE_FORMAT_2:
                        sdf = new SimpleDateFormat("dd MMM, yyyy hh:mm a", getCurrentLocale());
                        sdf.setTimeZone(getTimeZone(isResultGMT));
                        resultDate = sdf.format(new Date(calTemp.getTimeInMillis()));
                        break;
                    case DATE_FORMAT_3:
                        sdf = new SimpleDateFormat("dd/ MM/ yyyy, hh:mm a", getCurrentLocale());
                        sdf.setTimeZone(getTimeZone(isResultGMT));
                        resultDate = sdf.format(new Date(calTemp.getTimeInMillis()));
                        break;
                    case DATE_FORMAT_4:
                        sdf = new SimpleDateFormat("dd MMM, yyyy", getCurrentLocale());
                        sdf.setTimeZone(getTimeZone(isResultGMT));
                        resultDate = sdf.format(new Date(calTemp.getTimeInMillis()));
                        break;

                    case DATE_FORMAT_5:
                        sdf = new SimpleDateFormat("dd-MMM-yyyy", getCurrentLocale());
                        sdf.setTimeZone(getTimeZone(isResultGMT));
                        resultDate = sdf.format(new Date(calTemp.getTimeInMillis()));
                        break;

                    case DATE_FORMAT_6:
                        sdf = new SimpleDateFormat("dd/ MM/ yyyy,\nhh:mm a", getCurrentLocale());
                        sdf.setTimeZone(getTimeZone(isResultGMT));
                        resultDate = sdf.format(new Date(calTemp.getTimeInMillis()));
                        break;

                    default:
                        sdf = new SimpleDateFormat("MMM dd, yyyy", getCurrentLocale());
                        sdf.setTimeZone(getTimeZone(isResultGMT));
                        resultDate = sdf.format(new Date(calTemp.getTimeInMillis()));
                        break;
                }

                return resultDate;
            }
        } catch (Exception e) {
            //	e.printStackTrace();
            return resultDate;
        }
    }

    public static String getTimeFromUnix(int format, boolean isOriginalGMT, boolean isResultGMT, long time) {
        if (time <= -62169984000L)
            return "";
        String resultTime = "";
        try {
            Calendar calTemp;
            if (isOriginalGMT) {
                calTemp = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            } else {
                calTemp = Calendar.getInstance();
            }
            calTemp.setTimeInMillis(time);

            SimpleDateFormat sdf;
            switch (format) {
                case TIME_FORMAT_0:
                    sdf = new SimpleDateFormat("hh:mm a", getCurrentLocale());
                    sdf.setTimeZone(getTimeZone(isResultGMT));
                    resultTime = sdf.format(new Date(calTemp.getTimeInMillis()));
                    break;

                case TIME_FORMAT_1:
                    sdf = new SimpleDateFormat("hh:mm\na", getCurrentLocale());
                    sdf.setTimeZone(getTimeZone(isResultGMT));
                    resultTime = sdf.format(new Date(calTemp.getTimeInMillis()));
                    break;

                default:
                    sdf = new SimpleDateFormat("hh:mm a", getCurrentLocale());
                    sdf.setTimeZone(getTimeZone(isResultGMT));
                    resultTime = sdf.format(new Date(calTemp.getTimeInMillis()));
                    break;
            }

            return resultTime;
        } catch (Exception e) {
            //	e.printStackTrace();
            return resultTime;
        }
    }

    private static TimeZone getTimeZone(boolean isResultGMT) {
        return isResultGMT ? TimeZone.getTimeZone("UTC") : Calendar.getInstance().getTimeZone();
    }

    public static String getTimeDifference(long time) {

        Calendar calNow = Calendar.getInstance();
        long difference = calNow.getTimeInMillis() - time;

        if (difference < MINUTE) {
            return instance.getString(R.string.label_1_min_ago);
        } else if (difference < HOUR) {
            return (difference / MINUTE) + AppConstants.SPACE + instance.getString(R.string.label_min_ago);
        } else if (difference < HOURS_24) {
            return (difference / HOUR) + AppConstants.SPACE + instance.getString(R.string.label_hour_ago);
        } else {
            return "";
        }
    }

    public static String getMonthName(int month) {
        switch (month) {
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
            default:
                return "ERROR";
        }
    }

    public static String getDayOFWeek(int dayOfWeek) {

        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                return "Sunday";
            case Calendar.MONDAY:
                return "Monday";
            case Calendar.TUESDAY:
                return "Tuesday";
            case Calendar.WEDNESDAY:
                return "Wednesday";
            case Calendar.THURSDAY:
                return "Thursday";
            case Calendar.FRIDAY:
                return "Friday";
            case Calendar.SATURDAY:
                return "Saturday";
            default:
                return "Error";
        }
    }

    public static String getDayOFWeekShort(int dayOfWeek) {

        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                return "Sun";
            case Calendar.MONDAY:
                return "Mon";
            case Calendar.TUESDAY:
                return "Tue";
            case Calendar.WEDNESDAY:
                return "Wed";
            case Calendar.THURSDAY:
                return "Thu";
            case Calendar.FRIDAY:
                return "Fri";
            case Calendar.SATURDAY:
                return "Sat";
            default:
                return "Error";
        }
    }

    public static File createVideoFile() throws IOException {
        Context mContext = getInstance().getApplicationContext();
        new FileOp(mContext);
        File image = null;

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                .format(new Date());
        String imageFileName = "Dearest" + timeStamp + "_";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String path = Environment.getExternalStorageDirectory() + "/Dearest/Photo/";
            File storageDir = new File(path);
            if (!storageDir.exists()) {
                storageDir.mkdirs();
            }
            image = new File(path + imageFileName + ".mp4");
        } else {
            image = new File(mContext.getFilesDir() + "/" + imageFileName + ".mp4");
        }

        image.createNewFile();
        // Save a file: path for use with ACTION_VIEW intents
        /*imagePath = image.getAbsolutePath();*/
        return image;
    }

    public static File createImageFile(int op) throws IOException {
        Context mContext = getInstance().getApplicationContext();
        new FileOp(mContext);
        File image = null;

        if (op == 0) {
            // Create an image file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                    .format(new Date());
            String imageFileName = "Dearest" + timeStamp + "_";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String path = Environment.getExternalStorageDirectory() + "/Dearest/Photo/";
                File storageDir = new File(path);
                if (!storageDir.exists()) {
                    storageDir.mkdirs();
                }
                image = new File(path + imageFileName + ".jpg");
            } else {
                image = new File(mContext.getFilesDir() + "/" + imageFileName + ".jpg");
            }

            image.createNewFile();
            // Save a file: path for use with ACTION_VIEW intents
            /*imagePath = image.getAbsolutePath();*/
        }
        return image;
    }

    public static File createImageFile(String fileName) throws IOException {

        Context mContext = getInstance().getApplicationContext();
        new FileOp(mContext);
        File image = null;

        // Create an image file name

        String imageFileName = "Dearest" + "_" + fileName;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File storageDir = new File(
                    Environment.getExternalStorageDirectory() + "/Dearest/Photo/");
            if (!storageDir.exists()) {
                storageDir.mkdirs();
            }
            image = new File(storageDir.getAbsolutePath() + "/" + imageFileName + ".jpg");
        } else {
            image = new File(mContext.getFilesDir() + "/" + imageFileName + ".jpg");
        }

        image.createNewFile();
        // Save a file: path for use with ACTION_VIEW intents
        /*imagePath = image.getAbsolutePath();*/
        return image;
    }

    public static String getSnapshotPath() throws IOException {
        Context mContext = getInstance().getApplicationContext();
        new FileOp(mContext);
        File image = null;

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                .format(new Date());
        String imageFileName = "Dearest" + timeStamp + "_";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String path = Environment.getExternalStorageDirectory() + "/Dearest/Snapshot/";
            File storageDir = new File(path);
            if (!storageDir.exists()) {
                storageDir.mkdirs();
            }
            image = new File(path + imageFileName + ".jpg");
        } else {
            image = new File(mContext.getFilesDir() + "/" + imageFileName + ".jpg");
        }

        image.createNewFile();
        // Save a file: path for use with ACTION_VIEW intents
        /*imagePath = image.getAbsolutePath();*/
        return image.getAbsolutePath();
    }

    public static Bitmap getVideoSnapshot(String path) {

        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Images.Thumbnails.FULL_SCREEN_KIND);
        if (thumb == null) {
            try {
                MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
                    mediaMetadataRetriever.setDataSource(path, new HashMap<String, String>());
                else
                    mediaMetadataRetriever.setDataSource(path);
                return mediaMetadataRetriever.getFrameAtTime(500);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return Bitmap.createScaledBitmap(
                        BitmapFactory.decodeResource(getInstance().r, R.drawable.default_profile_pic),
                        640, 640, false);
            }
        }
        Matrix matrix = new Matrix();
        return Bitmap.createBitmap(thumb, 0, 0,
                thumb.getWidth(), thumb.getHeight(), matrix, true);
    }


    public static String getDeviceID(Context context) {
        String DEVICEID = "";
        String IMEI = "";

        try {
            TelephonyManager mngr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
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

    public static Locale getCurrentLocale() {
        return new Locale(Config.getInstance().getLocale());
    }

    public static Locale getLocale(String lang) {
        return new Locale(lang);
    }

    public static void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = App.getInstance().getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

    public static String getLocationName(double currentLatitude, double currentLongitude) {

//        swipeView.setRefreshing(true);

        Geocoder geocoder = new Geocoder(getInstance().getApplicationContext(), Locale.getDefault());
        try {

            List<Address> addresses = geocoder.getFromLocation(currentLatitude, currentLongitude, 1);

            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);

                System.out.println("Location Name Retrieved : " + address);
                Config.getInstance().setCurrentLocation(address.getFeatureName());
//                txtActionSearch.setText(address.getAddressLine(0));
                return Config.getInstance().getCurrentLocation();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return "";

    }


    /**
     * Use this method to colorize toolbar icons to the desired target color
     *
     * @param toolbarView       toolbar view being colored
     * @param toolbarIconsColor the target color of toolbar icons
     * @param activity          reference to activity needed to register observers
     */
    public static void colorizeToolbar(Toolbar toolbarView, int toolbarIconsColor, Activity activity) {
        final PorterDuffColorFilter colorFilter
                = new PorterDuffColorFilter(toolbarIconsColor, PorterDuff.Mode.MULTIPLY);

        for (int i = 0; i < toolbarView.getChildCount(); i++) {
            final View v = toolbarView.getChildAt(i);

            //Step 1 : Changing the color of back button (or open drawer button).
            if (v instanceof ImageButton) {
                //Action Bar back button
                ((ImageButton) v).getDrawable().setColorFilter(colorFilter);
            }

            if (v instanceof ActionMenuView) {
                for (int j = 0; j < ((ActionMenuView) v).getChildCount(); j++) {

                    //Step 2: Changing the color of any ActionMenuViews - icons that
                    //are not back button, nor text, nor overflow menu icon.
                    final View innerView = ((ActionMenuView) v).getChildAt(j);

                    if (innerView instanceof ActionMenuItemView) {
                        int drawablesCount = ((ActionMenuItemView) innerView).getCompoundDrawables().length;
                        for (int k = 0; k < drawablesCount; k++) {
                            if (((ActionMenuItemView) innerView).getCompoundDrawables()[k] != null) {
                                final int finalK = k;

                                //Important to set the color filter in seperate thread,
                                //by adding it to the message queue
                                //Won't work otherwise.
                                innerView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        ((ActionMenuItemView) innerView).getCompoundDrawables()[finalK].setColorFilter(colorFilter);
                                    }
                                });
                            }
                        }
                    }
                }
            }

            //Step 3: Changing the color of title and subtitle.
            toolbarView.setTitleTextColor(toolbarIconsColor);
            toolbarView.setSubtitleTextColor(toolbarIconsColor);

            //Step 4: Changing the color of the Overflow Menu icon.
            setOverflowButtonColor(activity, colorFilter);
        }
    }

    private static void setOverflowButtonColor(final Activity activity, final PorterDuffColorFilter colorFilter) {
        final String overflowDescription = activity.getString(R.string.abc_action_menu_overflow_description);
        final ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        final ViewTreeObserver viewTreeObserver = decorView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                final ArrayList<View> outViews = new ArrayList<View>();
                decorView.findViewsWithText(outViews, overflowDescription,
                        View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                if (outViews.isEmpty()) {
                    return;
                }
                ImageView overflow = (ImageView) outViews.get(0);
                overflow.setColorFilter(colorFilter);
                removeOnGlobalLayoutListener(decorView, this);
            }
        });
    }

    private static void removeOnGlobalLayoutListener(View v, ViewTreeObserver.OnGlobalLayoutListener listener) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            v.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
        } else {
            v.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        }
    }

    public static boolean checkForToken() {
        Context context = getInstance().getApplicationContext();
        SharedPreferences prfs = context.getSharedPreferences(AppConstants.PREFERENCE_NAME_SESSION, Context.MODE_PRIVATE);
        String token = prfs.getString(AppConstants.PREFERENCE_KEY_SESSION_TOKEN, "");
        String fcmID = prfs.getString(AppConstants.PREFERENCE_KEY_SESSION_FCM_ID, "");
        String email = prfs.getString(AppConstants.PREFERENCE_KEY_SESSION_EMAIL, "");
        String userID = prfs.getString(AppConstants.PREFERENCE_KEY_SESSION_USERID, "");
        String profilePhoto = prfs.getString(AppConstants.PREFERENCE_KEY_SESSION_PROFILE_PHOTO, "");
//        String coverPhoto = prfs.getString(AppConstants.PREFERENCE_KEY_SESSION_COVER_PHOTO, "");
        String name = prfs.getString(AppConstants.PREFERENCE_KEY_SESSION_NAME, "");
        String firstName = prfs.getString(AppConstants.PREFERENCE_KEY_SESSION_FIRSTNAME, "");
        String lastName = prfs.getString(AppConstants.PREFERENCE_KEY_SESSION_LASTNAME, "");
        String phone = prfs.getString(AppConstants.PREFERENCE_KEY_SESSION_PHONE, "");
        String password = prfs.getString(AppConstants.PREFERENCE_KEY_SESSION_PASSWORD, "");
        String gender = prfs.getString(AppConstants.PREFERENCE_KEY_SESSION_GENDER, "");
        long DOB = prfs.getLong(AppConstants.PREFERENCE_KEY_SESSION_DOB, 0);
        String locale = prfs.getString(AppConstants.PREFERENCE_KEY_SESSION_LOCALE, Locale.getDefault().getLanguage());
//        boolean isFirstTime = prfs.getBoolean(AppConstants.PREFERENCE_KEY_SESSION_IS_FIRST_TIME, true);
//        boolean isPhoneVerified = prfs.getBoolean(AppConstants.PREFERENCE_KEY_SESSION_IS_PHONE_VERIFIED, false);
//        boolean isPremiumMember = prfs.getBoolean(AppConstants.PREFERENCE_KEY_SESSION_IS_PREMIUM_MEMBER, false);
      /*  boolean isEmailVerified = prfs.getBoolean(AppConstants.PREFERENCE_IS_EMAIL_VERIFIED, false);
        boolean isRegistrationCompleted = prfs.getBoolean(AppConstants.PREFERENCE_IS_REGISTRATION_COMPLETED, false);
*/
        Log.i(TAG, "checkForToken: " + prfs.getAll());
        if (!"".equals(token)) {
//        if (!userID.equals("") && FirebaseAuth.getInstance().getCurrentUser() != null) {
            Config.getInstance().setAuthToken(token);
            Config.getInstance().setUserID(userID);
            Config.getInstance().setFcmID(fcmID);
            Config.getInstance().setProfilePhoto(profilePhoto);
//            Config.getInstance().setCoverPhoto(coverPhoto);
            Config.getInstance().setEmail(email);
            Config.getInstance().setName(name);
            Config.getInstance().setFirstName(firstName);
            Config.getInstance().setLastName(lastName);
            Config.getInstance().setPassword(password);
            Config.getInstance().setPhone(phone);
            Config.getInstance().setGender(gender);
            Config.getInstance().setDOB(DOB);
            Config.getInstance().setLocale(locale);
//            Config.getInstance().setPhoneVerified(isPhoneVerified);
//            Config.getInstance().setPremiumMember(isPremiumMember);
            if (Config.getInstance().getCurrentLatitude() == null) {
                Config.getInstance().setCurrentLatitude("");
                Config.getInstance().setCurrentLongitude("");
            }
            return true;
        } else {
            if (Config.getInstance().getCurrentLatitude() == null) {
                Config.getInstance().setCurrentLatitude("");
                Config.getInstance().setCurrentLongitude("");
            }
            return false;
        }

    }

    public static void saveToken() {
        Context context = getInstance().getApplicationContext();
        System.out.println("SAVE STARTED");
        SharedPreferences preferences = context.getSharedPreferences(AppConstants.PREFERENCE_NAME_SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_TOKEN, Config.getInstance().getAuthToken());
        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_FCM_ID, Config.getInstance().getFcmID());
        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_USERID, Config.getInstance().getUserID());
        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_PROFILE_PHOTO, Config.getInstance().getProfilePhoto());
        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_COVER_PHOTO, Config.getInstance().getCoverPhoto());
        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_EMAIL, Config.getInstance().getEmail());
        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_FIRSTNAME, Config.getInstance().getFirstName());
        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_LASTNAME, Config.getInstance().getLastName());
        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_NAME, Config.getInstance().getName());
        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_PHONE, Config.getInstance().getPhone());
        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_ADDRESS, Config.getInstance().getAddress());
        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_GENDER, Config.getInstance().getGender());
        editor.putLong(AppConstants.PREFERENCE_KEY_SESSION_DOB, Config.getInstance().getDOB());
        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_LOCALE, Config.getInstance().getLocale());
//        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_GCM_ID, Config.getInstance().getGCMID());
//        editor.putBoolean(AppConstants.PREFERENCE_KEY_SESSION_IS_FIRST_TIME, Config.getInstance().isFirstTime());
        editor.putBoolean(AppConstants.PREFERENCE_KEY_SESSION_IS_PHONE_VERIFIED, Config.getInstance().isPhoneVerified());
        editor.putBoolean(AppConstants.PREFERENCE_KEY_SESSION_IS_EMAIL_VERIFIED, Config.getInstance().isEmailVerified());
//        editor.putBoolean(AppConstants.PREFERENCE_KEY_SESSION_IS_PREMIUM_MEMBER, Config.getInstance().isPremiumMember());
        editor.commit();
        FileOp fileOp = new FileOp(context);
        fileOp.writeHash();
        System.out.println("SAVE COMPLETE");
    }

/*
    public static void saveToken(ProfileBean profileBean) {
        Context context = getInstance().getApplicationContext();
        FileOp fileOp = new FileOp(context);
        setConfig(profileBean);
        Log.i(TAG, "saveToken: AuthBean : " + new Gson().toJson(profileBean));

        System.out.println("SAVE STARTED");
        SharedPreferences preferences = context.getSharedPreferences(AppConstants.PREFERENCE_NAME_SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_TOKEN, profileBean.getAuthToken());
        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_FCM_ID, Config.getInstance().getFcmID());
        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_USERID, profileBean.getId());
        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_PROFILE_PHOTO, profileBean.getProfilePhoto());
        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_COVER_PHOTO, profileBean.getCoverPhoto());
        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_EMAIL, profileBean.getEmail());
        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_FIRSTNAME, profileBean.getFirstName());
        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_LASTNAME, profileBean.getLastName());
        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_NAME, profileBean.getName());
        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_PHONE, profileBean.getPhone());
        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_ADDRESS, profileBean.getAddress());
        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_GENDER, profileBean.getGender());
        editor.putLong(AppConstants.PREFERENCE_KEY_SESSION_DOB, profileBean.getDob());
        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_LOCALE, Config.getInstance().getLocale());
//        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_GCM_ID, Config.getInstance().getGCMID());
//        editor.putBoolean(AppConstants.PREFERENCE_KEY_SESSION_IS_FIRST_TIME, false);
        editor.putBoolean(AppConstants.PREFERENCE_KEY_SESSION_IS_PHONE_VERIFIED, profileBean.isPhoneVerified());
        editor.putBoolean(AppConstants.PREFERENCE_KEY_SESSION_IS_EMAIL_VERIFIED, profileBean.isEmailVerified());
//        editor.putBoolean(AppConstants.PREFERENCE_KEY_SESSION_IS_PREMIUM_MEMBER, true);
        editor.commit();
        fileOp.writeHash();
        System.out.println("SAVE COMPLETE");
    }
*/

    public static void saveToken(AuthBean authBean) {
        Context context = getInstance().getApplicationContext();
        FileOp fileOp = new FileOp(context);
        setConfig(authBean);
        Log.i(TAG, "saveToken: AuthBean : " + new Gson().toJson(authBean));

        System.out.println("SAVE STARTED");
        SharedPreferences preferences = context.getSharedPreferences(AppConstants.PREFERENCE_NAME_SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_TOKEN, authBean.getAuthToken());
        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_FCM_ID, Config.getInstance().getFcmID());
        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_USERID, authBean.getUserID());
        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_PROFILE_PHOTO, authBean.getProfilePhoto());
        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_COVER_PHOTO, authBean.getCoverPhoto());
        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_EMAIL, authBean.getEmail());
        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_FIRSTNAME, authBean.getFirstName());
        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_LASTNAME, authBean.getLastName());
        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_NAME, authBean.getName());
        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_PHONE, authBean.getPhone());
        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_ADDRESS, authBean.getAddress());
        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_GENDER, authBean.getGender());
        editor.putLong(AppConstants.PREFERENCE_KEY_SESSION_DOB, authBean.getDOB());
        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_LOCALE, Config.getInstance().getLocale());
//        editor.putString(AppConstants.PREFERENCE_KEY_SESSION_GCM_ID, Config.getInstance().getGCMID());
//        editor.putBoolean(AppConstants.PREFERENCE_KEY_SESSION_IS_FIRST_TIME, false);
//        editor.putBoolean(AppConstants.PREFERENCE_KEY_SESSION_IS_PHONE_VERIFIED, authBean.isPhoneVerified());
//        editor.putBoolean(AppConstants.PREFERENCE_KEY_SESSION_IS_PREMIUM_MEMBER, authBean.isPremiumMember());
//        editor.putBoolean(AppConstants.PREFERENCE_KEY_SESSION_IS_PREMIUM_MEMBER, true);
        editor.commit();
        fileOp.writeHash();
        System.out.println("SAVE COMPLETE");
    }

/*
    private static void setConfig(ProfileBean profileBean) {
        Config.getInstance().setAuthToken(profileBean.getAuthToken());
        Config.getInstance().setUserID(profileBean.getId());
        Config.getInstance().setProfilePhoto(profileBean.getProfilePhoto());
        Config.getInstance().setCoverPhoto(profileBean.getCoverPhoto());
        Config.getInstance().setEmail(profileBean.getEmail());
        Config.getInstance().setName(profileBean.getName());
        Config.getInstance().setFirstName(profileBean.getFirstName());
        Config.getInstance().setLastName(profileBean.getLastName());
//            Config.getInstance().setUsername(username);
        Config.getInstance().setPhone(profileBean.getPhone());
        Config.getInstance().setAddress(profileBean.getAddress());
        Config.getInstance().setGender(profileBean.getGender());
        Config.getInstance().setDOB(profileBean.getDob());
        Config.getInstance().setPhoneVerified(true);
//        Config.getInstance().setPremiumMember(profileBean.isPremiumMember());
        Config.getInstance().setEmailVerified(profileBean.isEmailVerified());
//            Config.getInstance().setRegistrationCompleted(isRegistrationCompleted);
//        Config.getInstance().setFirstTime(false);
        if (Config.getInstance().getCurrentLatitude() == null) {
            Config.getInstance().setCurrentLatitude("");
            Config.getInstance().setCurrentLongitude("");
        }

    }
*/

    private static void setConfig(AuthBean authBean) {
        Config.getInstance().setAuthToken(authBean.getAuthToken());
        Config.getInstance().setUserID(authBean.getUserID());
        Config.getInstance().setProfilePhoto(authBean.getProfilePhoto());
//        Config.getInstance().setCoverPhoto(authBean.getCoverPhoto());
        Config.getInstance().setEmail(authBean.getEmail());
        Config.getInstance().setName(authBean.getName());
        Config.getInstance().setFirstName(authBean.getFirstName());
        Config.getInstance().setLastName(authBean.getLastName());
//            Config.getInstance().setUsername(username);
        Config.getInstance().setPhone(authBean.getPhone());
        Config.getInstance().setAddress(authBean.getAddress());
        Config.getInstance().setGender(authBean.getGender());
        Config.getInstance().setDOB(authBean.getDOB());
        Config.getInstance().setPhoneVerified(authBean.isPhoneVerified());
//        Config.getInstance().setPremiumMember(authBean.isPremiumMember());
        Config.getInstance().setEmailVerified(authBean.isEmailVerified());
//            Config.getInstance().setRegistrationCompleted(isRegistrationCompleted);
//        Config.getInstance().setFirstTime(false);
        if (Config.getInstance().getCurrentLatitude() == null) {
            Config.getInstance().setCurrentLatitude("");
            Config.getInstance().setCurrentLongitude("");
        }

    }

    public static void logout() {
        Context context = getInstance().getApplicationContext();
        SharedPreferences preferences = context.getSharedPreferences(AppConstants.PREFERENCE_NAME_SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        //editor.remove(AppConstants.PREFERENCE_KEY_SESSION_TOKEN);
        editor.commit();

        Config.clear();

//        new DBHandler(context).clearDatabase();
//        Digits.logout();
//        FirebaseAuth.getInstance().signOut();
        clearApplicationData(context);
//        restart(context, 500);

    }


    public static void clearApplicationData(Context context) {
        File cache = context.getFilesDir();
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


}

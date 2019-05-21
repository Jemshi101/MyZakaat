package com.quartzbit.myzakaat.util;

import android.util.Log;

import com.quartzbit.myzakaat.R;
import com.quartzbit.myzakaat.app.App;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;


/**
 * Created by Jemsheer K D on 20 July, 2018.
 * Package com.royalarabian.royalarabiantours.util
 * Project RoyalArabianTours
 */
public class TimeUtil {


    public static final int DATE_FORMAT_0 = 0;
    public static final int DATE_FORMAT_1 = 1;
    public static final int DATE_FORMAT_2 = 2;
    public static final int DATE_FORMAT_3 = 3;
    public static final int DATE_FORMAT_4 = 4;
    public static final int DATE_FORMAT_5 = 5;
    public static final int DATE_FORMAT_6 = 6;
    public static final int DATE_FORMAT_7 = 7;
    public static final int DATE_FORMAT_8 = 8;
    public static final int DATE_FORMAT_9 = 9;
    public static final int DATE_FORMAT_10 = 10;
    public static final int DATE_FORMAT_11_NO_TIMEZONE = 11;
    public static final int DATE_FORMAT_12 = 12;
    public static final int DATE_FORMAT_13 = 13;
    public static final int DATE_FORMAT_14 = 14;
    public static final int DATE_FORMAT_15 = 15;

    public static final int TIME_FORMAT_0 = 0;
    public static final int TIME_FORMAT_1 = 1;
    public static final int TIME_FORMAT_2 = 2;

    public static final long HOURS_24 = 86400000;
    public static final long SECOND = 1000;
    public static final long MINUTE = 60000;
    public static final long HOUR = 3600000;
    public static final SimpleDateFormat DATE_FORMAT_FULL =
//            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.ENGLISH);
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZZZZ", Locale.ENGLISH);
    public static final SimpleDateFormat DATE_FORMAT_NO_TIMEZONE =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
    public static final SimpleDateFormat DATE_FORMAT_CC_AVENUE_TRANSACTION =
            new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
    public static final SimpleDateFormat DATE_FORMAT_DEFAULT =
            new SimpleDateFormat("dd MMM, yyyy", Locale.ENGLISH);

    private static final String SPACE = " ";

    public static Calendar getUserTime(long GMTTime) {
        Calendar calTemp = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calTemp.setTimeInMillis(GMTTime * 1000);
        calTemp.setTimeZone(Calendar.getInstance().getTimeZone());
        return calTemp;
    }

    public static Calendar getCalendarFromString(String date, boolean isGMT, SimpleDateFormat sdf) {
        Calendar calTemp = getCurrentDateWithoutTime(isGMT);
        try {
            if (isGMT) {
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                calTemp.setTimeZone(TimeZone.getTimeZone("UTC"));
            }
            calTemp.setTime(sdf.parse(date));
            return calTemp;
        } catch (ParseException ignored) {
        }
        return calTemp;
    }

    public static String getUserTimeFromUnix(String GMTTime) {
        if (GMTTime.equalsIgnoreCase("-62169984000")
                || GMTTime.equalsIgnoreCase("false")
                || GMTTime.equalsIgnoreCase("true"))
            return "";
        try {
            Calendar calTemp = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calTemp.setTimeInMillis(Long.valueOf(GMTTime) * 1000);
            calTemp.setTimeZone(Calendar.getInstance().getTimeZone());
            GMTTime = new SimpleDateFormat("MMM dd, yyyy hh:mm a", App.getCurrentLocale())
                    .format(new Date(calTemp.getTimeInMillis()));
            return GMTTime;
        } catch (Exception e) {
            //	e.printStackTrace();
            return GMTTime;
        }
    }

    public static String getUserTimeFromChatUnix(String GMTTime) {
        if (GMTTime.equalsIgnoreCase("-62169984000")
                || GMTTime.equalsIgnoreCase("false")
                || GMTTime.equalsIgnoreCase("true"))
            return "";
        try {
            Calendar calTemp = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calTemp.setTimeInMillis(Long.valueOf(GMTTime));
            calTemp.setTimeZone(Calendar.getInstance().getTimeZone());
            GMTTime = new SimpleDateFormat("MMM dd, yyyy hh:mm a", App.getCurrentLocale())
                    .format(new Date(calTemp.getTimeInMillis()));
            return GMTTime;
        } catch (Exception e) {
            //	e.printStackTrace();
            return GMTTime;
        }
    }

    public static String getUserDateFromUnix(String GMTTime) {
        if (GMTTime.equalsIgnoreCase("-62169984000")
                || GMTTime.equalsIgnoreCase("false")
                || GMTTime.equalsIgnoreCase("true"))
            return "";
        try {
            Calendar calTemp = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calTemp.setTimeInMillis(Long.valueOf(GMTTime) * 1000);
            calTemp.setTimeZone(Calendar.getInstance().getTimeZone());
            GMTTime = new SimpleDateFormat("MMM dd, yyyy", App.getCurrentLocale())
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
                        sdf = new SimpleDateFormat("MMM dd, yyyy", App.getCurrentLocale());
                        sdf.setTimeZone(getTimeZone(isResultGMT));
                        resultDate = sdf.format(new Date(calTemp.getTimeInMillis()));
                        break;
                    case DATE_FORMAT_1:
                        sdf = new SimpleDateFormat("dd/MM/yyyy", App.getCurrentLocale());
                        sdf.setTimeZone(getTimeZone(isResultGMT));
                        resultDate = sdf.format(new Date(calTemp.getTimeInMillis()));
                        break;
                    case DATE_FORMAT_2:
                        sdf = new SimpleDateFormat("dd MMM, yyyy hh:mm a", App.getCurrentLocale());
                        sdf.setTimeZone(getTimeZone(isResultGMT));
                        resultDate = sdf.format(new Date(calTemp.getTimeInMillis()));
                        break;
                    case DATE_FORMAT_3:
                        sdf = new SimpleDateFormat("dd/MM/yyyy, hh:mm a", App.getCurrentLocale());
                        sdf.setTimeZone(getTimeZone(isResultGMT));
                        resultDate = sdf.format(new Date(calTemp.getTimeInMillis()));
                        break;
                    case DATE_FORMAT_4:
                        sdf = new SimpleDateFormat("dd MMM, yyyy", App.getCurrentLocale());
                        sdf.setTimeZone(getTimeZone(isResultGMT));
                        resultDate = sdf.format(new Date(calTemp.getTimeInMillis()));
                        break;

                    case DATE_FORMAT_5:
                        sdf = new SimpleDateFormat("dd MMMM, yyyy", App.getCurrentLocale());
                        sdf.setTimeZone(getTimeZone(isResultGMT));
                        resultDate = sdf.format(new Date(calTemp.getTimeInMillis()));
                        break;

                    case DATE_FORMAT_6:
                        sdf = new SimpleDateFormat("dd/MM/yyyy,\nhh:mm a", App.getCurrentLocale());
                        sdf.setTimeZone(getTimeZone(isResultGMT));
                        resultDate = sdf.format(new Date(calTemp.getTimeInMillis()));
                        break;

                    case DATE_FORMAT_7:
                        sdf = new SimpleDateFormat("dd/MM/yy", App.getCurrentLocale());
                        sdf.setTimeZone(getTimeZone(isResultGMT));
                        resultDate = sdf.format(new Date(calTemp.getTimeInMillis()));
                        break;

                    case DATE_FORMAT_8:
                        sdf = new SimpleDateFormat("EEE, dd MMM", App.getCurrentLocale());
                        sdf.setTimeZone(getTimeZone(isResultGMT));
                        resultDate = sdf.format(new Date(calTemp.getTimeInMillis()));
                        break;

                    case DATE_FORMAT_9:
                        sdf = new SimpleDateFormat("dd MMM", App.getCurrentLocale());
                        sdf.setTimeZone(getTimeZone(isResultGMT));
                        resultDate = sdf.format(new Date(calTemp.getTimeInMillis()));
                        break;

                    case DATE_FORMAT_10:
                        sdf = new SimpleDateFormat("dd/MM/yyyy, HH:mm:ss", App.getCurrentLocale());
                        sdf.setTimeZone(getTimeZone(isResultGMT));
                        resultDate = sdf.format(new Date(calTemp.getTimeInMillis()));
                        break;
                    case DATE_FORMAT_11_NO_TIMEZONE:
                        sdf = DATE_FORMAT_NO_TIMEZONE;
                        sdf.setTimeZone(getTimeZone(isResultGMT));
                        resultDate = sdf.format(new Date(calTemp.getTimeInMillis()));
                        break;

                    case DATE_FORMAT_12:
                        sdf = DATE_FORMAT_DEFAULT;
                        sdf.setTimeZone(getTimeZone(isResultGMT));
                        resultDate = sdf.format(new Date(calTemp.getTimeInMillis()));
                        break;

                    case DATE_FORMAT_13:
                        sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm", App.getCurrentLocale());
                        sdf.setTimeZone(getTimeZone(isResultGMT));
                        resultDate = sdf.format(new Date(calTemp.getTimeInMillis()));
                        break;
                    case DATE_FORMAT_14:
                        sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", App.getCurrentLocale());
                        sdf.setTimeZone(getTimeZone(isResultGMT));
                        resultDate = sdf.format(new Date(calTemp.getTimeInMillis()));
                        break;

                    case DATE_FORMAT_15:
                        sdf = new SimpleDateFormat("EEE, dd MMM yyyy", App.getCurrentLocale());
                        sdf.setTimeZone(getTimeZone(isResultGMT));
                        resultDate = sdf.format(new Date(calTemp.getTimeInMillis()));
                        break;

                    default:
                        sdf = new SimpleDateFormat("MMM dd, yyyy", App.getCurrentLocale());
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
                    sdf = new SimpleDateFormat("hh:mm a", App.getCurrentLocale());
                    sdf.setTimeZone(getTimeZone(isResultGMT));
                    resultTime = sdf.format(new Date(calTemp.getTimeInMillis()));
                    break;

                case TIME_FORMAT_1:
                    sdf = new SimpleDateFormat("hh:mm\na", App.getCurrentLocale());
                    sdf.setTimeZone(getTimeZone(isResultGMT));
                    resultTime = sdf.format(new Date(calTemp.getTimeInMillis()));
                    break;

                case TIME_FORMAT_2:
                    sdf = new SimpleDateFormat("HH:mm", App.getCurrentLocale());
                    sdf.setTimeZone(getTimeZone(isResultGMT));
                    resultTime = sdf.format(new Date(calTemp.getTimeInMillis()));
                    break;

                default:
                    sdf = new SimpleDateFormat("hh:mm a", App.getCurrentLocale());
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
            return App.getInstance().getString(R.string.label_1_min_ago);
        } else if (difference < HOUR) {
            return (difference / MINUTE) + SPACE + App.getInstance().getString(R.string.label_min_ago);
        } else if (difference < HOURS_24) {
            return (difference / HOUR) + SPACE + App.getInstance().getString(R.string.label_hour_ago);
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

    public static String getMonthShortName(Calendar cal) {
        return cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.ENGLISH);
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

    public static Calendar getGMTCal(Calendar localCal) {
        Calendar tempCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        tempCal.set(Calendar.YEAR, localCal.get(Calendar.YEAR));
        tempCal.set(Calendar.MONTH, localCal.get(Calendar.MONTH));
        tempCal.set(Calendar.DAY_OF_MONTH, localCal.get(Calendar.DAY_OF_MONTH));
        tempCal.set(Calendar.HOUR_OF_DAY, localCal.get(Calendar.HOUR_OF_DAY));
        tempCal.set(Calendar.MINUTE, localCal.get(Calendar.MINUTE));
        tempCal.set(Calendar.SECOND, localCal.get(Calendar.SECOND));
        tempCal.set(Calendar.MILLISECOND, localCal.get(Calendar.MILLISECOND));
        return tempCal;
    }

    public static String formatDate(SimpleDateFormat simpleDateFormat, SimpleDateFormat newSimpleDateFormat, String date) {

        try {
            return newSimpleDateFormat.format(simpleDateFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
    }

    public static String formatDate(SimpleDateFormat dateFormat, Calendar cal) {
        return dateFormat.format(new Date(cal.getTimeInMillis()));
    }

    public static String formatDate(SimpleDateFormat dateFormat, Calendar cal, boolean isOriginalGMT, boolean isResultGMT) {
        Calendar calTemp;
        long timeInMills = cal.getTimeInMillis();
        if (isOriginalGMT) {
            calTemp = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        } else {
            calTemp = Calendar.getInstance();
        }
        calTemp.setTimeInMillis(timeInMills);

        if (isResultGMT)
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(new Date(calTemp.getTimeInMillis()));
    }

    public static boolean isDateAfter(SimpleDateFormat newSimpleDateFormat, String date) {
        Log.d("isDateAfter()", "date " + date + ", newSimpleDateFormat " + newSimpleDateFormat);
        try {
            Date mCompareDate = newSimpleDateFormat.parse(date);
            Calendar mCompareCalendar = Calendar.getInstance();
            mCompareCalendar.setTime(mCompareDate);
            mCompareCalendar.add(Calendar.DAY_OF_MONTH, 1);
            return Calendar.getInstance().getTime().after(mCompareCalendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isSameDate(String strDate1, String strDate2) {

        Calendar cal1 = TimeUtil.getCalendarFromString(strDate1, false, TimeUtil.DATE_FORMAT_NO_TIMEZONE);
        Calendar cal2 = TimeUtil.getCalendarFromString(strDate2, false, TimeUtil.DATE_FORMAT_NO_TIMEZONE);

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)
                && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    @NotNull
    public static Calendar getNextDateWithoutTime() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal;
    }

    @NotNull
    public static Calendar getCurrentDateWithoutTime() {
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal;
    }

    @NotNull
    public static Calendar getCurrentDateWithoutTime(boolean isGMT) {
        Calendar cal = isGMT ? Calendar.getInstance(TimeZone.getTimeZone("UTC")) : Calendar.getInstance();

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal;
    }

    @NotNull
    public static Calendar getCalendarWithoutTime(Calendar cal) {

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal;
    }

    public static String format24Hour(int hourOfDay) {
        int hour = hourOfDay % 24;
        String strHour = String.valueOf(hour);
        if (String.valueOf(hour).length() == 1) {
            strHour = "0" + hour;
        }
        return strHour;
    }

    public static String formatMinute(int minute) {
        minute = minute % 60;
        String strMinute = String.valueOf(minute);
        if (String.valueOf(minute).length() == 1) {
            strMinute = "0" + minute;
        }
        return strMinute;
    }

    public static String getFormattedDayOfMonth(@Nullable Date date) {
        Calendar cal = getCurrentDateWithoutTime();
        if (date == null)
            date = getCurrentDateWithoutTime().getTime();

        cal.setTime(date);
        return getFormattedDayOfMonth(cal);
    }

    public static String getFormattedDayOfMonth(@Nullable Calendar cal) {
        if (cal == null)
            cal = getCurrentDateWithoutTime();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return day >= 10 ? String.valueOf(day) : "0" + day;
    }

    public static String getFormattedMonth(@Nullable Calendar cal) {
        if (cal == null)
            cal = getCurrentDateWithoutTime();
        int day = cal.get(Calendar.MONTH);
        return day >= 10 ? String.valueOf(day) : "0" + day;
    }

    public static Calendar getNonNullCalendar(@Nullable Calendar cal) {
        return cal != null ? cal : Calendar.getInstance();
    }

    public static Calendar getCalendarFromDate(@Nullable Date date) {
        if (date == null)
            date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public static long getDaysBetween(@NotNull String startDate, @NotNull String endDate) {
        Calendar calStart = getCalendarFromString(startDate, false, DATE_FORMAT_NO_TIMEZONE);
        Calendar calEnd = getCalendarFromString(endDate, false, DATE_FORMAT_NO_TIMEZONE);

        return Math.abs(TimeUnit.DAYS.convert(calEnd.getTimeInMillis() - calStart.getTimeInMillis(), TimeUnit.MILLISECONDS));
    }

    public static boolean isSameDay(long date) {
        Calendar now = Calendar.getInstance();
        Calendar cdate = getCurrentDateWithoutTime();
        cdate.setTimeInMillis(date);

        return (now.get(Calendar.YEAR) == cdate.get(Calendar.YEAR)
                && now.get(Calendar.MONTH) == cdate.get(Calendar.MONTH)
                && now.get(Calendar.DATE) == cdate.get(Calendar.DATE));
    }
}

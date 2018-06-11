package com.quartzbit.myzakaat.model;

/**
 * Created by Jemsheer K D on 14 June, 2017.
 * Package com.quartzbit.myzakaat.model
 * Project Dearest
 */

public class PathBean extends BaseBean {

    private int index;
    private long time;
    private String latitude;
    private String longitude;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}

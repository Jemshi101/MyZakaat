package com.quartzbit.myzakaat.model;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Jemsheer K D on 09 May, 2017.
 * Package com.quartzbit.myzakaat.model
 * Project Dearest
 */

public class PolyPointBean extends BaseBean {

    private long time;
    private int distance;
    private String timeText;
    private String distanceText;

    private List<List<HashMap<String, String>>> routes;


    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getTimeText() {
        return timeText;
    }

    public void setTimeText(String timeText) {
        this.timeText = timeText;
    }

    public String getDistanceText() {
        return distanceText;
    }

    public void setDistanceText(String distanceText) {
        this.distanceText = distanceText;
    }

    public List<List<HashMap<String, String>>> getRoutes() {
        return routes;
    }

    public void setRoutes(List<List<HashMap<String, String>>> routes) {
        this.routes = routes;
    }

}

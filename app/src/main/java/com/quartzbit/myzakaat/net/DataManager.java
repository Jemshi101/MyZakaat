package com.quartzbit.myzakaat.net;

import com.quartzbit.myzakaat.listeners.BasicListener;
import com.quartzbit.myzakaat.listeners.PolyPointListener;
import com.quartzbit.myzakaat.model.BasicBean;
import com.quartzbit.myzakaat.model.PolyPointBean;
import com.quartzbit.myzakaat.net.WSAsyncTasks.LocationNameTask;
import com.quartzbit.myzakaat.net.WSAsyncTasks.PolyPointTask;
import com.quartzbit.myzakaat.net.WSAsyncTasks.UpdateFCMTokenTask;
import com.quartzbit.myzakaat.util.AppConstants;

import org.json.JSONObject;

import java.util.HashMap;


public class DataManager {


    public static void fetchPolyPoints(HashMap<String, String> urlParams, final PolyPointListener listener) {

        PolyPointTask polyPointTask = new PolyPointTask(urlParams);
        polyPointTask.setPolyPointTaskListener(new PolyPointTask.PolyPointTaskListener() {
            @Override
            public void dataDownloadedSuccessfully(PolyPointBean polyPointBean) {
                if (polyPointBean == null)
                    listener.onLoadFailed(AppConstants.WEB_ERROR_MSG);
                else {
                    if (polyPointBean.getStatus().equalsIgnoreCase("Success")) {
                        listener.onLoadCompleted(polyPointBean);
                    } else if (polyPointBean.getStatus().equalsIgnoreCase("Error")) {
                        listener.onLoadFailed(polyPointBean.getErrorMsg());
                    } else {
                        listener.onLoadFailed(AppConstants.WEB_ERROR_MSG);
                    }
                }
            }

            @Override
            public void dataDownloadFailed() {
                listener.onLoadFailed(AppConstants.WEB_ERROR_MSG);
            }
        });
        polyPointTask.execute();
    }

    public static void performUpdateFCMToken(JSONObject postData, final BasicListener listener) {

        UpdateFCMTokenTask updateFCMTokenTask = new UpdateFCMTokenTask(postData);
        updateFCMTokenTask.setUpdateFCMTokenTaskListener(new UpdateFCMTokenTask.UpdateFCMTokenTaskListener() {
            @Override
            public void dataDownloadedSuccessfully(BasicBean basicBean) {
                if (basicBean == null)
                    listener.onLoadFailed(AppConstants.WEB_ERROR_MSG);
                else {
                    if (basicBean.getStatus().equalsIgnoreCase("Success")) {
                        listener.onLoadCompleted(basicBean);
                    } else if (basicBean.getStatus().equalsIgnoreCase("Error")) {
                        listener.onLoadFailed(basicBean.getErrorMsg());
                    } else {
                        listener.onLoadFailed(AppConstants.WEB_ERROR_MSG);
                    }
                }
            }

            @Override
            public void dataDownloadFailed() {
                listener.onLoadFailed(AppConstants.WEB_ERROR_MSG);
            }
        });
        updateFCMTokenTask.execute();
    }

    public static void fetchLocationName(HashMap<String, String> urlParams, final BasicListener listener) {

        LocationNameTask locationNameTask = new LocationNameTask(urlParams);
        locationNameTask.setLocationNameTaskListener(new LocationNameTask.LocationNameTaskListener() {
            @Override
            public void dataDownloadedSuccessfully(BasicBean basicBean) {
                if (basicBean == null)
                    listener.onLoadFailed(AppConstants.WEB_ERROR_MSG);
                else {
                    if (basicBean.getStatus().equalsIgnoreCase("Success")) {
                        listener.onLoadCompleted(basicBean);
                    } else if (basicBean.getStatus().equalsIgnoreCase("Error")) {
                        listener.onLoadFailed(basicBean.getErrorMsg());
                    } else {
                        listener.onLoadFailed(AppConstants.WEB_ERROR_MSG);
                    }
                }
            }

            @Override
            public void dataDownloadFailed() {
                listener.onLoadFailed(AppConstants.WEB_ERROR_MSG);
            }
        });
        locationNameTask.execute();
    }

}

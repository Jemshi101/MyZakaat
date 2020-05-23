package com.quartzbit.myzakaat.net;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.quartzbit.myzakaat.listeners.BasicListener;
import com.quartzbit.myzakaat.listeners.PolyPointListener;
import com.quartzbit.myzakaat.listeners.TransactionListListener;
import com.quartzbit.myzakaat.model.BasicBean;
import com.quartzbit.myzakaat.model.PolyPointBean;
import com.quartzbit.myzakaat.model.TransactionListBean;
import com.quartzbit.myzakaat.net.WSAsyncTasks.LocationNameTask;
import com.quartzbit.myzakaat.net.WSAsyncTasks.TransactionListTask;
import com.quartzbit.myzakaat.net.WSAsyncTasks.UpdateFCMTokenTask;
import com.quartzbit.myzakaat.util.AppConstants;

import org.json.JSONObject;

import java.util.HashMap;


public class DataManager {


    public static void fetchTransactionList(
            HashMap<String, String> urlParams,
            GoogleAccountCredential mCredential, final TransactionListListener listener) {

        TransactionListTask transactionListTask = new TransactionListTask(urlParams, mCredential);
        transactionListTask.setTransactionListTaskListener(new TransactionListTask.TransactionListTaskListener() {
            @Override
            public void dataDownloadedSuccessfully(TransactionListBean transactionListBean) {
                if (transactionListBean == null)
                    listener.onLoadFailed(AppConstants.WEB_ERROR_MSG);
                else {
                    if (transactionListBean.getStatus().equalsIgnoreCase("Success")) {
                        listener.onLoadCompleted(transactionListBean);
                    } else if (transactionListBean.getStatus().equalsIgnoreCase("Error")) {
                        listener.onLoadFailed(transactionListBean.getErrorMsg());
                    } else {
                        listener.onLoadFailed(AppConstants.WEB_ERROR_MSG);
                    }
                }
            }

            @Override
            public void dataDownloadFailed() {
                listener.onLoadFailed(AppConstants.WEB_ERROR_MSG);
            }

            @Override
            public void onCancelled(Exception mLastError) {
                listener.onLoadFailed(mLastError);
            }

            @Override
            public void onPermissionError(UserRecoverableAuthIOException e) {
                listener.onLoadFailed(e);
            }
        });
        transactionListTask.execute();
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

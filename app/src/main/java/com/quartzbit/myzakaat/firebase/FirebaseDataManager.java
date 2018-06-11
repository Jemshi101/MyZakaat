package com.quartzbit.myzakaat.firebase;

import com.quartzbit.myzakaat.R;
import com.quartzbit.myzakaat.app.App;
import com.quartzbit.myzakaat.firebase.firebaseTasks.UpdateFCMTokenTask;
import com.quartzbit.myzakaat.listeners.BasicListener;
import com.quartzbit.myzakaat.model.BasicBean;
import com.quartzbit.myzakaat.util.AppConstants;

import org.json.JSONObject;

/**
 * Created by Jemsheer K D on 09 August, 2017.
 * Package com.quartzbit.myzakaat.firebase
 * <p>
 * Project Dearest
 */

public class FirebaseDataManager {

    public static void performUpdateFCMToken(JSONObject postData, final BasicListener listener) {

        UpdateFCMTokenTask updateFCMTokenTask = new UpdateFCMTokenTask(postData);
        updateFCMTokenTask.setUpdateFCMTokenTaskListener(new UpdateFCMTokenTask.UpdateFCMTokenTaskListener() {
            @Override
            public void dataDownloadedSuccessfully(BasicBean basicBean) {
                if (basicBean == null)
                    listener.onLoadFailed(App.getInstance().getString(R.string.message_something_went_wrong));
                else {
                    listener.onLoadCompleted(basicBean);
                }
            }

            @Override
            public void dataDownloadFailed() {
                listener.onLoadFailed(AppConstants.WEB_ERROR_MSG);

            }
        });
        updateFCMTokenTask.performUpdateFCMToken();

    }

}



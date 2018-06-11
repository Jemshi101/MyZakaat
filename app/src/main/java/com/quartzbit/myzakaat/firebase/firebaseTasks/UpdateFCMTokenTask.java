package com.quartzbit.myzakaat.firebase.firebaseTasks;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import com.quartzbit.myzakaat.model.BasicBean;

/**
 * Created by Jemsheer K D on 21 August, 2017.
 * Package com.quartzbit.myzakaat.firebase.firebaseTasks
 * Project Dearest
 */

public class UpdateFCMTokenTask {

    private final DatabaseReference mDatabase;
    private final DatabaseReference usersRef;
    private final JSONObject postData;
    private DatabaseReference profileRef;
    private UpdateFCMTokenTaskListener updateFCMTokenTaskListener;

    public UpdateFCMTokenTask(JSONObject postData) {
        this.postData = postData;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        usersRef = mDatabase.child("users");
    }

    public void performUpdateFCMToken() {
        BasicBean basicBean = new BasicBean();


        profileRef = usersRef.child(postData.optString("uid"));
        profileRef.keepSynced(true);

        profileRef.child("fcmID").setValue(postData.optString("fcm_token"));
        basicBean.setStatus("success");

        updateFCMTokenTaskListener.dataDownloadedSuccessfully(basicBean);

    }

    public static interface UpdateFCMTokenTaskListener {
        void dataDownloadedSuccessfully(BasicBean updateFCMTokenBean);

        void dataDownloadFailed();
    }

    public UpdateFCMTokenTaskListener getUpdateFCMTokenTaskListener() {
        return updateFCMTokenTaskListener;
    }

    public void setUpdateFCMTokenTaskListener(UpdateFCMTokenTaskListener updateFCMTokenTaskListener) {
        this.updateFCMTokenTaskListener = updateFCMTokenTaskListener;
    }

}

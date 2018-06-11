package com.quartzbit.myzakaat.firebase.firebaseParsers;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

import com.quartzbit.myzakaat.app.App;
import com.quartzbit.myzakaat.model.VideoBean;
import com.quartzbit.myzakaat.util.AppConstants;

/**
 * Created by Jemsheer K D on 08 September, 2017.
 * Package com.quartzbit.myzakaat.firebase.firebaseParsers
 * Project Dearest
 */

public class VideoListParser extends BasicParser {

    public ArrayList<VideoBean> getVideoList(DataSnapshot dataSnapshot) {
        ArrayList<VideoBean> list = new ArrayList<>();
        if (dataSnapshot.exists()) {

            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                if (snapshot.exists()) {
                    VideoBean videoBean = new VideoBean();
                    videoBean.setId(snapshot.getKey());
                    videoBean.setSnapshotURL(App.getImagePath(getPhotoPath(snapshot, "snapshotURL")));
                    videoBean.setUrl(App.getImagePath(getPhotoPath(snapshot, "url")));
                    list.add(videoBean);
                }
            }

        }
        return list;
    }
}

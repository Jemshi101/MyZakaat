package com.quartzbit.myzakaat.firebase.firebaseParsers;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

import com.quartzbit.myzakaat.app.App;
import com.quartzbit.myzakaat.model.MediaBean;
import com.quartzbit.myzakaat.util.AppConstants;

/**
 * Created by Jemsheer K D on 08 September, 2017.
 * Package com.quartzbit.myzakaat.firebase.firebaseParsers
 * Project Dearest
 */

public class MediaListParser extends BasicParser {

    public ArrayList<MediaBean> getMediaList(DataSnapshot dataSnapshot) {
        ArrayList<MediaBean> list = new ArrayList<>();
        if (dataSnapshot.exists()) {

            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                if (snapshot.exists()) {
                    MediaBean mediaBean = new MediaBean();
                    mediaBean.setId(getString(snapshot, "id"));
                    mediaBean.setTitle(getString(snapshot, "title"));
                    mediaBean.setPostID(getString(snapshot, "postID"));
                    mediaBean.setTime(getLong(snapshot, "time"));
//                    mediaBean.setImageThumbURL(App.getImagePath(getPhotoPath(snapshot, "snapshotURL")));
                    mediaBean.setImageThumbURL(App.getImagePath(getPhotoPath(snapshot, "url")));
//                    mediaBean.setImageURL(App.getImagePath(getPhotoPath(snapshot, "url")));
                    mediaBean.setImageURL(App.getImagePath(getPhotoPath(snapshot, "url")));
                    mediaBean.setVideoURL(App.getImagePath(getPhotoPath(snapshot, "url")));
//                    mediaBean.setType(AppConstants.MEDIA_TYPE_PHOTO);
                    list.add(mediaBean);
                }
            }
        }
        return list;
    }
}

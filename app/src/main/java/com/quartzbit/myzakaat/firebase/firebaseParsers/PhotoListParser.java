package com.quartzbit.myzakaat.firebase.firebaseParsers;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

import com.quartzbit.myzakaat.app.App;
import com.quartzbit.myzakaat.model.PhotoBean;
import com.quartzbit.myzakaat.util.AppConstants;

/**
 * Created by Jemsheer K D on 08 September, 2017.
 * Package com.quartzbit.myzakaat.firebase.firebaseParsers
 * Project Dearest
 */

public class PhotoListParser extends BasicParser {

    public ArrayList<PhotoBean> getPhotoList(DataSnapshot dataSnapshot) {
        ArrayList<PhotoBean> list = new ArrayList<>();
        if (dataSnapshot.exists()) {

            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                if (snapshot.exists()) {
                    PhotoBean photoBean = new PhotoBean();
                    photoBean.setId(getString(snapshot, "id"));
                    photoBean.setPostID(getString(snapshot, "postID"));
                    photoBean.setTime(getLong(snapshot, "time"));
                    photoBean.setYear(getString(snapshot, "year"));
                    photoBean.setUrl(App.getImagePath(getPhotoPath(snapshot, "url")));
                    list.add(photoBean);
                }
            }
        }
        return list;
    }
}

package com.quartzbit.myzakaat.listeners;

import com.quartzbit.myzakaat.model.MediaListBean;

/**
 * Created by Jemsheer K D on 30 April, 2018.
 * Package com.quartzbit.myzakaat.listeners
 * Project Dearest
 */
public interface MediaListListener {
    void onLoadCompleted(boolean isExists, MediaListBean mediaListBean);

    void onLoadFailed(String error);
}

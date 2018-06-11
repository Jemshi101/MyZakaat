package com.quartzbit.myzakaat.listeners;


import com.quartzbit.myzakaat.model.BasicBean;

public interface BasicListener {

    void onLoadCompleted(BasicBean basicBean);

    void onLoadFailed(String error);
}

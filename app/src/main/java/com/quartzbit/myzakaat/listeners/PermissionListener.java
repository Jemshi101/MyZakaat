package com.quartzbit.myzakaat.listeners;


public interface PermissionListener {

    void onPermissionCheckCompleted(int requestCode, boolean isPermissionGranted);

}

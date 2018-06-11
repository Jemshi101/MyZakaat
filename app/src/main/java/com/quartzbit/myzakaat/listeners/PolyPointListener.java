package com.quartzbit.myzakaat.listeners;

import com.quartzbit.myzakaat.model.PolyPointBean;

/**
 * Created by Jemsheer K D on 09 May, 2017.
 * Package com.quartzbit.myzakaat.listeners
 * Project Dearest
 */

public interface PolyPointListener {
    void onLoadCompleted(PolyPointBean polyPointBean);

    void onLoadFailed(String error);
}

package com.quartzbit.myzakaat.listeners;

import com.quartzbit.myzakaat.model.TransactionListBean;

/**
 * Created by Jemsheer K D on 12 June, 2018.
 * Package com.quartzbit.myzakaat.listeners
 * Project MyZakaat
 */
public interface TransactionListListener {
    void onLoadCompleted(TransactionListBean transactionListBean);

    void onLoadFailed(String error);
}

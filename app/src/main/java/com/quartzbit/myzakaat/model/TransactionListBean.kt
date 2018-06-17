package com.quartzbit.myzakaat.model

import com.quartzbit.myzakaat.app.App
import java.util.*

/**
 * Created by Jemsheer K D on 11 June, 2018.
 * Package com.quartzbit.myzakaat.model
 * Project MyZakaat
 */
class TransactionListBean : BaseBean() {

    var transactions: ArrayList<TransactionBean> = ArrayList()
    var pagination: PaginationBean = PaginationBean()
    var lastTransaction: TransactionBean? = null

    fun getTransactionsOfDate(dateInMillis: Long): ArrayList<TransactionBean> {
        var list = ArrayList<TransactionBean>()

        for (bean in transactions) {
            if (bean.dateString == App.getDateFromUnix(App.DATE_FORMAT_5, false,
                            false, dateInMillis, false)) {
                list.add(bean)
            }
        }

        return list
    }

    fun getLastTransactionOfDate(dateInMillis: Long): TransactionBean? {
        var list = ArrayList<TransactionBean>()


        for (bean in transactions) {
            if (bean.dateString == App.getDateFromUnix(App.DATE_FORMAT_5, false,
                            false, dateInMillis, false)) {
                list.add(bean)
                lastTransaction = bean
            }
        }

        return if (!list.isEmpty()) list.last() else lastTransaction
    }


}
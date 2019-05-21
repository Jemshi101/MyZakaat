package com.quartzbit.myzakaat.model

import com.quartzbit.myzakaat.app.App
import com.quartzbit.myzakaat.util.TimeUtil
import java.util.*

/**
 * Created by Jemsheer K D on 11 June, 2018.
 * Package com.quartzbit.myzakaat.model
 * Project MyZakaat
 */
class TransactionListBean : BaseBean() {

    var bankBean: BankBean = BankBean()
    var transactions: ArrayList<TransactionBean> = ArrayList()
    var pagination: PaginationBean = PaginationBean()
    var lastTransaction: TransactionBean? = null

    fun setLastTransaction(dateInMillis: Long) {
        var currentTransaction: TransactionBean? = null
        for (bean in transactions) {
            if (bean.dateString == App.getDateFromUnix(App.DATE_FORMAT_5, false,
                            false, dateInMillis, false)) {
                currentTransaction = bean
                lastTransaction = currentTransaction;
                break;
            }
        }
        if(currentTransaction == null){
            for(bean in transactions){
                if(dateInMillis < bean.date){

                }
            }
        }
    }

    fun getTransactionsOfDate(dateInMillis: Long): ArrayList<TransactionBean> {
        val list = ArrayList<TransactionBean>()

        for (bean in getTransactionFromLastTransaction()) {
            if (bean.dateString == App.getDateFromUnix(App.DATE_FORMAT_5, false,
                            false, dateInMillis, false)) {
                list.add(bean)
            }
        }

        return list
    }

    private fun getTransactionFromLastTransaction() =
            if (lastTransaction != null)
                transactions.subList(transactions.indexOf(lastTransaction!!), transactions.size - 1)
            else transactions

    fun getLastLowestTransactionOfDate(dateInMillis: Long): TransactionBean? {

        val list = getTransactionsOfDate(dateInMillis)
        var lowestTransactionBean: TransactionBean? = null
        if (list.isNotEmpty()) {
            for (bean in list) {
                lowestTransactionBean?.let {
                    if (bean.realBalance < it.realBalance) {
                        lowestTransactionBean = bean
                    }
                } ?: kotlin.run {
                    lowestTransactionBean = bean
                }
            }
        }
        lowestTransactionBean?.let {
            lastTransaction = lowestTransactionBean
        }

        /*for (bean in transactions) {
            if (bean.dateString == App.getDateFromUnix(App.DATE_FORMAT_5, false,
                            false, dateInMillis, false)) {
                lastTransaction?.let {
                    if (it.)
                        lastTransaction = bean
                } ?: kotlin.run {
                    lastTransaction = bean
                }
            }
        }*/

        return lastTransaction
    }


}
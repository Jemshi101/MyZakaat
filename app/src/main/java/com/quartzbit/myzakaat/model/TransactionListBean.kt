package com.quartzbit.myzakaat.model

import com.quartzbit.myzakaat.app.App
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
        for (bean in transactions) {
            if (dateInMillis > bean.date) {
                lastTransaction = bean
            } else if (bean.dateString == App.getDateFromUnix(App.DATE_FORMAT_5, false,
                            false, dateInMillis, false)) {
                lastTransaction = bean
                break
            } else {
                break
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
                transactions.subList(transactions.indexOf(lastTransaction!!), transactions.size)
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
                lastTransaction = bean
            }
        }
        lowestTransactionBean?.let {
            return it
        } ?: kotlin.run {
            return lastTransaction
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

    }

    fun indexOf(transactionBean : TransactionBean): Int{
        for(bean in transactions){
            if(bean.id == transactionBean.id){
                return transactions.indexOf(bean)
            }
        }
        return -1;
    }


}
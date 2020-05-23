package com.quartzbit.myzakaat.viewModels

import androidx.lifecycle.ViewModel
import com.quartzbit.myzakaat.model.TransactionListBean
import java.util.*

/**
 * Created by Jemsheer K D on 17 June, 2018.
 * Package com.quartzbit.myzakaat
 * Project MyZakaat
 */
class HomeViewModel : ViewModel() {

    var transactionListBeanList: ArrayList<TransactionListBean> = ArrayList()
    var isFirst = true
    var currentLowestBalance: Float = Float.MAX_VALUE
    var currentBalance: Float = 0F
    var currentInterest: Float = 0F
    var currentTotalBalance: Float = 0F
    var zakaatStartDate = Calendar.getInstance()
    var currentTransactionDate = ""
    var lowestTransactionDate = ""
    var zakaat: Float = (currentLowestBalance * 2.5 / 100).toFloat()


    fun processTransaction() {
        zakaat = (currentLowestBalance * 2.5 / 100).toFloat()
        currentBalance = 0f
        currentInterest = 0f
        currentTotalBalance = 0f
        for (listBean in transactionListBeanList) {
            listBean.lastTransaction?.let {
                currentBalance += it.realBalance
                currentInterest += it.interest
                currentTotalBalance += it.balance
            }
        }
    }
}
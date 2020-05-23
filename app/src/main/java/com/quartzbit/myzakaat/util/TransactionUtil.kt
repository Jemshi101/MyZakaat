package com.quartzbit.myzakaat.util

import android.os.AsyncTask
import android.text.format.DateUtils
import android.util.Log
import com.google.gson.Gson
import com.quartzbit.myzakaat.app.App
import com.quartzbit.myzakaat.viewModels.HomeViewModel
import java.util.*

/**
 * Created by Jemsheer K D on 17 June, 2018.
 * Package com.quartzbit.myzakaat.util
 * Project MyZakaat
 */
class TransactionUtil {

    companion object {
        private val TAG: String = "TransactionUtil"
        var processTransactionTask: ProcessTransactionTask? = null

        fun processTransactions(mViewModel: HomeViewModel, transactionUtilListener: TransactionUtilListener) {

            processTransactionTask = ProcessTransactionTask(mViewModel, transactionUtilListener)
            processTransactionTask!!.execute()

        }

        fun cancelProcessingIfRunning() {
            if (processTransactionTask != null && !processTransactionTask!!.isCancelled)
                processTransactionTask!!.cancel(true)
        }
    }


    open class ProcessTransactionTask
    (var mViewModel: HomeViewModel, var transactionUtilListener: TransactionUtilListener)
        : AsyncTask<String, HomeViewModel, HomeViewModel>() {


        override fun doInBackground(vararg p0: String?): HomeViewModel {
            val cal = Calendar.getInstance()
            cal.timeInMillis = mViewModel.zakaatStartDate.timeInMillis

            var isFirst = true

            mViewModel.currentBalance = 0F
            mViewModel.currentInterest = 0F
            mViewModel.currentTotalBalance = 0F


            var currentLowestBalance = getCurrentLowestBalance(cal);


            for (transactionListBean in mViewModel.transactionListBeanList) {
                transactionListBean.setLastTransaction(cal.timeInMillis)
            }

            while (!DateUtils.isToday(cal.timeInMillis)) {
                Log.i(TAG, "DATE : " + App.getDateFromUnix(App.DATE_FORMAT_4, false,
                        false, cal.timeInMillis, false))
                var balance = 0F
                var interest = 0F
                var totalBalance = 0F
                for (transactionListBean in mViewModel.transactionListBeanList) {
                    val transactionBean = transactionListBean.getLastLowestTransactionOfDate(cal.timeInMillis)
                    transactionBean?.let {
                        Log.i(TAG, "process : transactionBean : $it")
                        balance += it.realBalance
                        interest += it.interest
                        totalBalance += it.balance
                    }
                }
                if (isFirst) {
//                    currentLowestBalance = balance
                    isFirst = false
                }

                if (balance < currentLowestBalance) {
                    currentLowestBalance = balance
                    mViewModel.lowestTransactionDate = App.getDateFromUnix(App.DATE_FORMAT_4, false,
                            false, cal.timeInMillis, false)
                }
                mViewModel.isFirst = isFirst
                mViewModel.currentLowestBalance = currentLowestBalance
//                mViewModel.currentBalance = balance
//                mViewModel.currentInterest = interest
//                mViewModel.currentTotalBalance = totalBalance
//                mViewModel.currentTotalBalance = totalBalance
                mViewModel.currentTransactionDate = App.getDateFromUnix(App.DATE_FORMAT_4, false,
                        false, cal.timeInMillis, false)
                mViewModel.processTransaction()

                publishProgress(mViewModel)
//                transactionUtilListener.publishProgress(mViewModel)

                cal.add(Calendar.DATE, 1)
            }

            return mViewModel
        }

        private fun getCurrentLowestBalance(cal: Calendar): Float {
            val currentLowestBalance: Float
            val tempCal = Calendar.getInstance()
            tempCal.timeInMillis = cal.timeInMillis

            var balance = 0f

            for (transactionListBean in mViewModel.transactionListBeanList) {
                val transactionBean = transactionListBean.getLastLowestTransactionOfDate(cal.timeInMillis)
                transactionBean?.let {
                    val index = transactionListBean.indexOf(it)
                    if (index != 0 && index != -1) {

                        val bean = transactionListBean.transactions[index - 1]

                        Log.i(TAG, "process : transactionBean : " + Gson().toJson(bean))
                        balance += bean.realBalance
                    }
                }

            }
            currentLowestBalance = balance;

            return currentLowestBalance
        }

        override fun onProgressUpdate(vararg values: HomeViewModel) {
            super.onProgressUpdate(*values)

            transactionUtilListener.publishProgress(values[0])
        }

        override fun onPostExecute(result: HomeViewModel?) {
            super.onPostExecute(result)

            result?.let {
                transactionUtilListener.actionCompletedSuccessfully(it);
            } ?: kotlin.run {
                transactionUtilListener.actionFailed("Processing Failed");
            }
        }
    }


    interface TransactionUtilListener {
        fun actionCompletedSuccessfully(mViewModel: HomeViewModel)
        fun publishProgress(mViewModel: HomeViewModel)
        fun actionFailed(errorMsg: String)
    }
}
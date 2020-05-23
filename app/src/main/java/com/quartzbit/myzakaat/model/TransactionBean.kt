package com.quartzbit.myzakaat.model

import android.util.Log
import android.util.TimeUtils
import com.quartzbit.myzakaat.app.App.getCurrentLocale
import com.quartzbit.myzakaat.util.TimeUtil
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Jemsheer K D on 11 June, 2018.
 * Package com.quartzbit.myzakaat.model
 * Project MyZakaat
 */
class TransactionBean : BaseBean() {

    var id: Int = 0
    var dateString: String = ""
    var date: Long = 0
    var description: String = ""
    var reference: String = ""
    var debit: Float = 0F
    var credit: Float = 0F
    var balance: Float = 0F
    var bankCut: Float = 0F
    var realBalance: Float = 0F
    var interest: Float = 0F
    var lowestAmount: Float = 0F

    fun formatDate(date: String): String {
        val index = if (!date.contains("\n(")) date.indexOf("(") else date.indexOf("\n(")
        val str = if (index == -1) date else date.substring(0, index + 1)
        Log.i("TransactionBean", "DATE STRING : $str")
        return str
    }

    fun dateToMillis(date: String): Long {
        val calTemp = TimeUtil.getCurrentDateWithoutTime()
        return try {
            val sdf = SimpleDateFormat("dd-MMM-yyyy", getCurrentLocale())
            calTemp.time = sdf.parse(date)
            calTemp.timeInMillis
        } catch (ignored: ParseException) {
            0
        }
    }

    override fun toString(): String {
        return "ID : $id \n" +
                "Date : $dateString ; TimeStamp : $date\n" +
                "Balance : $balance \n" +
                "Interest : $interest \n" +
                "Real Balance : $realBalance \n" +
                "Lowest Amount : $lowestAmount"
    }


}
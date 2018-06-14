package com.quartzbit.myzakaat.model

import com.quartzbit.myzakaat.app.App.getCurrentLocale
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
    var balance: Float  = 0F
    var bankCut: Float = 0F
    var realBalance: Float = 0F
    var interest: Float = 0F
    var lowestAmount: Float = 0F

    fun formatDate(date: String): String {
        val index = if (!date.contains("\n(")) date.indexOf("(") else date.indexOf("\n(")
        return if (index == -1) date else date.substring(index + 1)
    }

    fun dateToMillis(date: String): Long {
        val calTemp = Calendar.getInstance()
        try {
            val sdf = SimpleDateFormat("dd-MMM-yyyy", getCurrentLocale())
            calTemp.time = sdf.parse(date)
            return calTemp.timeInMillis
        } catch (ignored: ParseException) {
            return 0
        }
    }


}
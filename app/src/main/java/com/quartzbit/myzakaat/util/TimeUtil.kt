package com.quartzbit.myzakaat.util

import java.util.*

/**
 * Created by Jemsheer K D on 17 June, 2018.
 * Package com.quartzbit.myzakaat.util
 * Project MyZakaat
 */
class TimeUtil {
    companion object {
        fun isSameDay(date: Long): Boolean {
            val now = Calendar.getInstance()
            val cdate = Calendar.getInstance()
            cdate.setTimeInMillis(date)

            return (now.get(Calendar.YEAR) == cdate.get(Calendar.YEAR)
                    && now.get(Calendar.MONTH) == cdate.get(Calendar.MONTH)
                    && now.get(Calendar.DATE) == cdate.get(Calendar.DATE))
        }
    }
}
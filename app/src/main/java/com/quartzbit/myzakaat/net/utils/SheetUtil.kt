package com.quartzbit.myzakaat.net.utils

/**
 * Created by Jemsheer K D on 17 June, 2018.
 * Package com.quartzbit.myzakaat.net.utils
 * Project MyZakaat
 */
class SheetUtil {

    companion object {
        fun getInt(value: Any): Int {
            try {
                return (value as String).replace(",","").toInt()
            } catch (e: Exception) {
                return 0
            }
        }

        fun getLong(value: Any): Long {
            try {
                return (value as String).replace(",","").toLong()
            } catch (e: Exception) {
                return 0L
            }
        }

        fun getFloat(value: Any): Float {
            try {
                return (value as String).replace(",","").toFloat()
            } catch (e: Exception) {
                return 0F
            }
        }

        fun getDouble(value: Any): Double {
            try {
                return (value as String).replace(",","").toDouble()
            } catch (e: Exception) {
                return 0.0
            }
        }

        fun getString(value: Any): String {
            try {
                return value as String
            } catch (e: Exception) {
                return ""
            }
        }
    }



}
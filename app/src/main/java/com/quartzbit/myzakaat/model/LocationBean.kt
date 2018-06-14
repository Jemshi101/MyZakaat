package com.quartzbit.myzakaat.model

import com.google.android.gms.maps.model.LatLng

/**
 * Created by Jemsheer K D on 18 January, 2018.
 * Package `com`.quartzbit.myzakaat.model
 * Project Dearest_Android
 */
class LocationBean : BaseBean() {

    var id: String = ""
    var name: String = ""
    var latitude: String = ""
    var longitude: String = ""


    fun getLatLng(): LatLng {
        return LatLng(dLatitude, dLongitude)
    }

    val dLatitude: Double
        get() {
            try {
                return java.lang.Double.parseDouble(latitude)
            } catch (e: NumberFormatException) {
                e.printStackTrace()
                return 0.0
            }

        }


    val dLongitude: Double
        get() {
            try {
                return java.lang.Double.parseDouble(longitude)
            } catch (e: NumberFormatException) {
                e.printStackTrace()
                return 0.0
            }

        }


}

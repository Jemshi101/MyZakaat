package `in`.techware.afcetcbus.database.entity

import `com`.quartzbit.myzakaat.model.LocationBean
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Jemsheer K D on 08 January, 2018.
 * Package `in`.techware.afcetcbus.database.entity
 * Project AFS_Bus_App
 */

@Entity(tableName = "location")
class LocationEntity() {

    constructor(locationBean: LocationBean) : this() {

        this.id = locationBean.id
        this.name = locationBean.name
        this.latitude = locationBean.longitude
        this.longitude = locationBean.longitude
    }

    @ColumnInfo(name = "id")
    @PrimaryKey()
    var id: String = ""
    var name: String = ""
    var latitude: String = ""
    var longitude: String = ""


    fun getBean(): LocationBean {
        var locationBean: LocationBean = LocationBean()

        locationBean.id = this.id
        locationBean.name = this.name
        locationBean.latitude = this.latitude
        locationBean.longitude = this.longitude

        return locationBean;
    }

}
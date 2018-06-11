package `com`.quartzbit.myzakaat.model

/**
 * Created by Jemsheer K D on 07 May, 2018.
 * Package `com`.quartzbit.myzakaat.model
 * Project Dearest
 */
class MediaListBean : BaseBean() {

    var mediaList: ArrayList<MediaBean> = ArrayList()
    var paginationBean: PaginationBean = PaginationBean()
    var type: Int = 0;
}
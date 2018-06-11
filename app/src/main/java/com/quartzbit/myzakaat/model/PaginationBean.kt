package `com`.quartzbit.myzakaat.model

/**
 * Created by Jemsheer K D on 29 November, 2016.
 * Package com.quartzbit.myzakaat.model
 * Project Dearest
 */

class PaginationBean : BaseBean() {
    var totalPages: Int = 0
    var currentPage: Int = 0
    var totalCount: Int = 0
    var total: Int = 0
    var count: Int = 0
    var perPage: Int = 0
    var currentKey: String = ""
}

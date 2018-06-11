package `com`.quartzbit.myzakaat.model


/**
 * Created by Jemsheer K D on 18 October, 2017.
 * Package `in`.techware.pixel8.model
 * Project PXL8
 */
class PhoneContactsBean : BaseBean() {

    var contacts: ArrayList<ContactBean> = ArrayList()
    var pagination: PaginationBean = PaginationBean()

    fun hasContact(id: String): Boolean {
        for (bean in contacts) {
            if (bean.id.equals(id)) {
                return true
            }
        }
        return false
    }
    fun getIndex(id: String): Int {
        for (bean in contacts) {
            if (bean.id.equals(id)) {
                return contacts.indexOf(bean)
            }
        }
        return -1
    }
}
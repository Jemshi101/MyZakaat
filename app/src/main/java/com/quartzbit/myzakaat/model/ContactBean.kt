package `com`.quartzbit.myzakaat.model

/**
 * Created by Jemsheer K D on 14 February, 2018.
 * Package `com`.quartzbit.myzakaat.model
 * Project Dearest
 */

class ContactBean : BaseBean(), Comparable<ContactBean> {

    var id: String = ""
    var name: String = ""
    var profilePhoto: String = ""
    var phoneNumbers: ArrayList<String> = ArrayList()
    var emails: ArrayList<String> = ArrayList()

    override fun compareTo(other: ContactBean): Int {
        var bean = other;
        return name.compareTo(bean.name);
    }
}
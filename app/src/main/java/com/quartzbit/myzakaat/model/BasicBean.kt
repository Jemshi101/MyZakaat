package `com`.quartzbit.myzakaat.model

/**
 * Created by Jemsheer K D on 03 December, 2016.
 * Package com.quartzbit.myzakaat.model
 * Project Dearest
 */

class BasicBean : BaseBean() {


    var key: String = ""
    var requestKey: String = ""
    var eventKey: String = ""

    var keyList: ArrayList<String> = ArrayList()

    var photoType: Int = 0
    var photoUrl: String = ""

    var url: String = ""

    var address: String = ""

    /* private List<CountryBean> countries;
    private List<StateBean> states;
    private List<DistrictBean> cities;
    private List<CourtBean> courts;*/

    var requestID: String = ""
    var tripID: String = ""
    var tripStatus: Int = 0
    var otpCode: String = ""
    var phone: String = ""

    var countryID: Int = 0
    var stateID: Int = 0
    var districtID: Int = 0

    var isPhoneAvailable: Boolean = false
}

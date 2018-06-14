package com.quartzbit.myzakaat.activity

import android.accounts.AccountManager


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.View
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.material.snackbar.Snackbar
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.util.ExponentialBackOff
import com.google.api.services.sheets.v4.SheetsScopes
import com.quartzbit.myzakaat.R
import com.quartzbit.myzakaat.app.App
import com.quartzbit.myzakaat.dialogs.SelectDateDialog
import com.quartzbit.myzakaat.listeners.PermissionListener
import com.quartzbit.myzakaat.listeners.TransactionListListener
import com.quartzbit.myzakaat.model.BankListBean
import com.quartzbit.myzakaat.model.TransactionListBean
import com.quartzbit.myzakaat.net.DataManager
import com.quartzbit.myzakaat.util.AppConstants
import kotlinx.android.synthetic.main.activity_home.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class HomeActivity : BaseAppCompatNoDrawerActivity() {

    lateinit var mCredential: GoogleAccountCredential
    private var transactionListBeanList: ArrayList<TransactionListBean> = ArrayList()
    private lateinit var bankListBean: BankListBean

    val REQUEST_ACCOUNT_PICKER = 1000;
    val REQUEST_AUTHORIZATION = 1001;

    val REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    val BUTTON_TEXT = "Call Google Sheets API";
    val PREF_ACCOUNT_NAME = "accountName";

    val SCOPES = mutableListOf<String>(SheetsScopes.SPREADSHEETS_READONLY);

    private lateinit var selectDateDialog: SelectDateDialog

    private var zakaatStartDate: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        initViews()
    }

    override fun onResume() {
        super.onResume()
        if (checkForGetAccountsPermissions()) {
            chooseAccount()
        } else {
            getGetAccountsPermissions()
        }
    }


    private fun initViews() {


        btnHomeEditZakaatStartDate.setOnClickListener {
            onEditZakaatStartDateClick(it)
        }

        btnHomeViewBankDetails.setOnClickListener {
            onViewBankDetailsClick(it)
        }


        // Initialize credentials and service object.
        mCredential = GoogleAccountCredential.usingOAuth2(
                applicationContext, SCOPES)
                .setBackOff(ExponentialBackOff())

        val permissionListener = PermissionListener { requestCode, isPermissionGranted ->
            if (requestCode == REQUEST_PERMISSIONS_GET_ACCOUNTS) {
                if (isPermissionGranted) {
                    chooseAccount();
                } else {
                    Snackbar.make(coordinatorLayout, "Accounts Permission Required", Snackbar.LENGTH_LONG).show()
                }
            }
        }
        addPermissionListener(permissionListener)
    }

    private fun getResultsFromApi() {
        /*if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(applicationContext)
                != ConnectionResult.SUCCESS) {
            acquireGooglePlayServices()
        } else */if (mCredential.getSelectedAccountName() == null) {
            chooseAccount()
        } else if (!App.isNetworkAvailable()) {
            Snackbar.make(coordinatorLayout, AppConstants.NO_NETWORK_AVAILABLE, Snackbar.LENGTH_LONG).show()
        } else {
            bankListBean = AppConstants.getBankListBean();
            if (bankListBean.banks.isNotEmpty())
                fetchTransactionList(0, mCredential)

        }
    }

    private fun fetchTransactionList(position: Int, mCredential: GoogleAccountCredential) {

        var urlParams = HashMap<String, String>()
        var bankBean = bankListBean.banks.get(position)
        urlParams.put("id", bankBean.id)
        urlParams.put("range", bankBean.range)

//        DataManager.fetchTransactionList(urlParams, mCredential, TransactionListListener() { })
        DataManager.fetchTransactionList(urlParams, mCredential, object : TransactionListListener {
            override fun onLoadCompleted(transactionListBean: TransactionListBean) {

                transactionListBeanList.add(transactionListBean)

                if (position < bankListBean.banks.size - 1) {
                    fetchTransactionList(position + 1, mCredential)
                } else {
                    processTransactions();
                }

            }

            override fun onLoadFailed(error: String) {
                Snackbar.make(coordinatorLayout, error, Snackbar.LENGTH_LONG)
                        .setAction(R.string.btn_dismiss, snackBarDismissOnClickListener).show();
            }

        })


    }

    private fun processTransactions() {

        var cal: Calendar = Calendar.getInstance();
        cal.set(2017, Calendar.JUNE, 2)

        txtHomeZakaatStartDate.text = App.getDateFromUnix(App.DATE_FORMAT_4, false,
                false, cal.timeInMillis, false)


        var isFirst = true
        var currentLowestBalance: Float = Float.MAX_VALUE
        var currentBalance: Float = 0F
        var currentInterest: Float = 0F
        var currentTotalBalance: Float = 0F

        while (cal.equals(Calendar.getInstance())) {
            var balance = 0F
            var interest = 0F
            var totalBalance = 0F
            for (transactionListBean in transactionListBeanList) {
                var transactionBean = transactionListBean.getLastTransactionOfDate(cal.timeInMillis)
                balance += transactionBean.realBalance
                interest += transactionBean.interest
                totalBalance += transactionBean.balance
            }
            if (isFirst) {
                currentLowestBalance = balance
                isFirst = false
            }
            if (balance < currentLowestBalance) {
                currentLowestBalance = balance
            }
            currentBalance = balance
            currentInterest = interest
            currentTotalBalance = totalBalance
            cal.add(Calendar.DATE, 1)
        }

        txtHomeCurrentBalance.text = currentBalance.toString()
        txtHomeInterest.text = currentInterest.toString()
        txtHomeTotalBalance.text = currentTotalBalance.toString()
        txtHomeLowestAmount.text = currentLowestBalance.toString()
        var zakaat: Float = (currentLowestBalance * 2.5 / 100).toFloat()
        txtHomeZakaatAmount.text = zakaat.toString()
    }

    private fun chooseAccount() {
        val accountName = getPreferences(Context.MODE_PRIVATE)
                .getString(PREF_ACCOUNT_NAME, null)
        if (accountName != null) {
            mCredential.setSelectedAccountName(accountName)
            getResultsFromApi()
        } else {
            // Start a dialog from which the user can choose an account
            startActivityForResult(
                    mCredential.newChooseAccountIntent(),
                    REQUEST_ACCOUNT_PICKER)
        }

    }

    override fun onActivityResult(
            requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_GOOGLE_PLAY_SERVICES -> if (resultCode != Activity.RESULT_OK) {
                Snackbar.make(coordinatorLayout,
                        "This app requires Google Play Services. Please install "
                                + "Google Play Services on your device and relaunch this app."
                        , Snackbar.LENGTH_INDEFINITE).show()
            } else {
                getResultsFromApi()
            }
            REQUEST_ACCOUNT_PICKER -> if (resultCode == Activity.RESULT_OK && data != null &&
                    data.extras != null) {
                val accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME)
                if (accountName != null) {
                    val settings = getPreferences(Context.MODE_PRIVATE)
                    val editor = settings.edit()
                    editor.putString(PREF_ACCOUNT_NAME, accountName)
                    editor.apply()
                    mCredential.setSelectedAccountName(accountName)
                    getResultsFromApi()
                }
            }
            REQUEST_AUTHORIZATION -> if (resultCode == Activity.RESULT_OK) {
                getResultsFromApi()
            }/**/
        }
    }

    private fun acquireGooglePlayServices() {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(this)
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode)
        }
    }

    fun showGooglePlayServicesAvailabilityErrorDialog(
            connectionStatusCode: Int) {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val dialog = apiAvailability.getErrorDialog(
                this@HomeActivity,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES)
        dialog.show()
    }

    private fun onViewBankDetailsClick(view: View) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        //mVibrator.vibrate(25);


    }

    private fun onEditZakaatStartDateClick(view: View) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
        //mVibrator.vibrate(25);

        selectDateDialog = SelectDateDialog(this)
        selectDateDialog.selectDateDialogActionListener = object : SelectDateDialog.SelectDateDialogActionListener {
            override fun actionCompletedSuccessfully(cal: Calendar) {
                zakaatStartDate = cal
                var date: String = App.getDateFromUnix(App.DATE_FORMAT_1, false,
                        false, cal.timeInMillis, false)
                txtHomeZakaatStartDate.text = date

            }

            override fun actionFailed(errorMsg: String) {
                Snackbar.make(coordinatorLayout, errorMsg, Snackbar.LENGTH_LONG).show()
            }

        }

    }
}

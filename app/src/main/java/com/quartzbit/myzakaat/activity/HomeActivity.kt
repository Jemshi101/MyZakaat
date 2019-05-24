package com.quartzbit.myzakaat.activity


//import com.google.android.gms.common.GoogleApiAvailability
import android.accounts.AccountManager
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.HapticFeedbackConstants
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import com.google.api.client.util.ExponentialBackOff
import com.quartzbit.myzakaat.R
import com.quartzbit.myzakaat.app.App
import com.quartzbit.myzakaat.databinding.ActivityHomeBinding
import com.quartzbit.myzakaat.dialogs.SelectDateDialog
import com.quartzbit.myzakaat.listeners.PermissionListener
import com.quartzbit.myzakaat.listeners.TransactionListListener
import com.quartzbit.myzakaat.model.BankListBean
import com.quartzbit.myzakaat.model.TransactionListBean
import com.quartzbit.myzakaat.net.DataManager
import com.quartzbit.myzakaat.util.AppConstants
import com.quartzbit.myzakaat.util.TransactionUtil
import com.quartzbit.myzakaat.util.TransactionUtil.TransactionUtilListener
import com.quartzbit.myzakaat.viewModels.HomeViewModel
import java.util.*
import kotlin.collections.HashMap

class HomeActivity : BaseAppCompatNoDrawerActivity() {

    companion object {
        const val REQUEST_ACCOUNT_PICKER = 1000
        const val REQUEST_AUTHORIZATION = 1001

        const val REQUEST_GOOGLE_PLAY_SERVICES = 1002
        const val BUTTON_TEXT = "Call Google Sheets API"
        const val PREF_ACCOUNT_NAME = "accountName"

    }

    private lateinit var binding: ActivityHomeBinding
    lateinit var mCredential: GoogleAccountCredential
    private lateinit var bankListBean: BankListBean

    val SCOPES = mutableListOf<String>("https://www.googleapis.com/auth/spreadsheets.readonly",
            "https://www.googleapis.com/auth/spreadsheets")


    private lateinit var selectDateDialog: SelectDateDialog

    private lateinit var mViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_home)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.activity_home, null, false)
        setContentView(binding.root)

        mViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        initViews()
        populateHome()

        getSupportActionBar()?.let {
            setTitle(R.string.app_name, R.color.white)
            it.title = ""
            it.setDisplayHomeAsUpEnabled(false)
            it.setHomeButtonEnabled(true)
            it.setDisplayShowHomeEnabled(true)
            it.setDisplayShowTitleEnabled(true)
        }

    }

    private fun populateHome() {

        binding.txtHomeCurrentBalance.text = mViewModel.currentBalance.toString()
        binding.txtHomeInterest.text = mViewModel.currentInterest.toString()
        binding.txtHomeTotalBalance.text = mViewModel.currentTotalBalance.toString()
        binding.txtHomeLowestAmount.text = mViewModel.currentLowestBalance.toString()
        binding.txtHomeCurrentTransactionDate.text = mViewModel.currentTransactionDate
        binding.txtHomeZakaatAmount.text = mViewModel.zakaat.toString()

        binding.txtHomeLowestTransactionDate.text = mViewModel.lowestTransactionDate

        binding.txtHomeZakaatStartDate.text = App.getDateFromUnix(App.DATE_FORMAT_5, false,
                false, mViewModel.zakaatStartDate.timeInMillis, false)

    }

    override fun onResume() {
        super.onResume()
        if (checkForGetAccountsPermissions()) {
            chooseAccount()
        } else {
            getGetAccountsPermissions()
        }
    }

    override fun onPause() {
        super.onPause()
        TransactionUtil.cancelProcessingIfRunning()
    }


    private fun initViews() {


        binding.btnHomeEditZakaatStartDate.setOnClickListener {
            onEditZakaatStartDateClick(it)
        }

        binding.btnHomeViewBankDetails.setOnClickListener {
            onViewBankDetailsClick(it)
        }


        // Initialize credentials and service object.
        mCredential = GoogleAccountCredential.usingOAuth2(
                applicationContext, SCOPES)
                .setBackOff(ExponentialBackOff())

        val permissionListener = PermissionListener { requestCode, isPermissionGranted ->
            if (requestCode == REQUEST_PERMISSIONS_GET_ACCOUNTS) {
                if (isPermissionGranted) {
                    chooseAccount()
                } else {
                    Snackbar.make(coordinatorLayout, "Accounts Permission Required", Snackbar.LENGTH_LONG).show()
                }
            }
        }
        addPermissionListener(permissionListener)
    }

    private val TAG = "HomeA"

    private fun getResultsFromApi() {
        /*if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(applicationContext)
                != ConnectionResult.SUCCESS) {
            acquireGooglePlayServices()
        } else */if (mCredential.selectedAccountName == null) {
            chooseAccount()
        } else if (!App.isNetworkAvailable()) {
            Snackbar.make(coordinatorLayout, AppConstants.NO_NETWORK_AVAILABLE, Snackbar.LENGTH_LONG).show()
        } else {
            bankListBean = AppConstants.getBankListBean()
            if (bankListBean.banks.isNotEmpty()) {
                fetchTransactionList(0, mCredential)
            }
        }
    }

    private fun fetchTransactionList(position: Int, mCredential: GoogleAccountCredential) {

        var urlParams = HashMap<String, String>()
        var bankBean = bankListBean.banks[position]
        urlParams.put("id", bankBean.id)
        urlParams.put("range", bankBean.range)

//        DataManager.fetchTransactionList(urlParams, mCredential, TransactionListListener() { })
        DataManager.fetchTransactionList(urlParams, mCredential, object : TransactionListListener {
            override fun onLoadFailed(exception: UserRecoverableAuthIOException) {
                startActivityForResult(exception.intent, REQUEST_AUTHORIZATION)
            }

            override fun onLoadFailed(exception: Exception) {
            }

            override fun onLoadCompleted(transactionListBean: TransactionListBean) {
                transactionListBean.bankBean = bankListBean.banks[position]

                if (mViewModel.transactionListBeanList.size - 1 < position)
                    mViewModel.transactionListBeanList.add(transactionListBean)
                else {
                    mViewModel.transactionListBeanList.set(position, transactionListBean)
                }

                if (position < bankListBean.banks.size - 1) {
                    fetchTransactionList(position + 1, mCredential)
                } else {
                    processTransactions()
                }

            }

            override fun onLoadFailed(error: String) {
                Snackbar.make(coordinatorLayout, error, Snackbar.LENGTH_LONG)
                        .setAction(R.string.btn_dismiss, snackBarDismissOnClickListener).show()
            }

        })


    }

    private fun processTransactions() {

        TransactionUtil.processTransactions(mViewModel, object : TransactionUtilListener {
            override fun actionCompletedSuccessfully(mViewModel: HomeViewModel) {
                this@HomeActivity.mViewModel = mViewModel
                runOnUiThread(Runnable {
                    populateHome()
                })
            }

            override fun publishProgress(mViewModel: HomeViewModel) {
                this@HomeActivity.mViewModel = mViewModel
                runOnUiThread(Runnable {
                    populateHome()
                })
            }

            override fun actionFailed(errorMsg: String) {
                Snackbar.make(coordinatorLayout, errorMsg, Snackbar.LENGTH_LONG)
                        .setAction(R.string.btn_dismiss, snackBarDismissOnClickListener)
            }
        })


        /* var cal: Calendar = Calendar.getInstance();
         cal.set(2017, Calendar.JUNE, 2)*/
/*
        var cal = mViewModel.zakaatStartDate
        txtHomeZakaatStartDate.text = App.getDateFromUnix(App.DATE_FORMAT_4, false,
                false, cal.timeInMillis, false)


        mViewModel.isFirst = true
        mViewModel.currentLowestBalance = Float.MAX_VALUE
        mViewModel.currentBalance = 0F
        mViewModel.currentInterest = 0F
        mViewModel.currentTotalBalance = 0F

        while (!DateUtils.isToday(cal.timeInMillis)) {
            Log.i(TAG, "DATE : " + App.getDateFromUnix(App.DATE_FORMAT_4, false,
                    false, cal.timeInMillis, false))
            var balance = 0F
            var interest = 0F
            var totalBalance = 0F
            for (transactionListBean in mViewModel.transactionListBeanList) {
                var transactionBean = transactionListBean.getLastLowestTransactionOfDate(cal.timeInMillis)
                Log.i(TAG, "process : transactionBean : " + Gson().toJson(transactionBean))
                if (transactionBean != null) {
                    balance += transactionBean.realBalance
                    interest += transactionBean.interest
                    totalBalance += transactionBean.balance
                }
            }
            if (mViewModel.isFirst) {
                mViewModel.currentLowestBalance = balance
                mViewModel.isFirst = false
            } else if (balance < mViewModel.currentLowestBalance) {
                mViewModel.currentLowestBalance = balance
            }
            mViewModel.currentBalance = balance
            mViewModel.currentInterest = interest
            mViewModel.currentTotalBalance = totalBalance
            cal.add(Calendar.DATE, 1)
        }

        populateHome()*/
        /*txtHomeCurrentBalance.text = currentBalance.toString()
        txtHomeInterest.text = currentInterest.toString()
        txtHomeTotalBalance.text = currentTotalBalance.toString()
        txtHomeLowestAmount.text = currentLowestBalance.toString()
        var zakaat: Float = (currentLowestBalance * 2.5 / 100).toFloat()
        txtHomeZakaatAmount.text = zakaat.toString()*/
    }

    private fun chooseAccount() {
        val accountName = getPreferences(Context.MODE_PRIVATE)
                .getString(PREF_ACCOUNT_NAME, null)
        if (accountName != null) {
            mCredential.selectedAccountName = accountName
            Log.i(TAG, "chooseAccount : Account Name : " + accountName)
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
                    mCredential.selectedAccountName = accountName
                    getResultsFromApi()
                }
            }
            REQUEST_AUTHORIZATION -> if (resultCode == Activity.RESULT_OK) {
                getResultsFromApi()
            }/**/
        }
    }

    /*private fun acquireGooglePlayServices() {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(this)
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode)
        }
    }*/

    /*fun showGooglePlayServicesAvailabilityErrorDialog(
            connectionStatusCode: Int) {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val dialog = apiAvailability.getErrorDialog(
                this@HomeActivity,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES)
        dialog.show()
    }*/

    private fun onViewBankDetailsClick(view: View) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
        //mVibrator.vibrate(25);


    }

    private fun onEditZakaatStartDateClick(view: View) {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
        //mVibrator.vibrate(25);

        selectDateDialog = SelectDateDialog(this)
        selectDateDialog.selectDateDialogActionListener = object : SelectDateDialog.SelectDateDialogActionListener {
            override fun actionCompletedSuccessfully(cal: Calendar) {
                mViewModel.zakaatStartDate = cal
                var date: String = App.getDateFromUnix(App.DATE_FORMAT_5, false,
                        false, cal.timeInMillis, false)
                binding.txtHomeZakaatStartDate.text = date
                chooseAccount()


            }

            override fun actionFailed(errorMsg: String) {
                Snackbar.make(coordinatorLayout, errorMsg, Snackbar.LENGTH_LONG).show()
            }

        }
        selectDateDialog.show()

    }
}

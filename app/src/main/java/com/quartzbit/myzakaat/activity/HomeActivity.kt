package com.quartzbit.myzakaat.activity

import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.quartzbit.myzakaat.R
import com.quartzbit.myzakaat.app.App
import com.quartzbit.myzakaat.dialogs.SelectDateDialog
import kotlinx.android.synthetic.main.activity_home.*
import java.util.*

class HomeActivity : BaseAppCompatNoDrawerActivity() {

    private lateinit var selectDateDialog: SelectDateDialog

    private var zakaatStartDate: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        initViews()
    }

    private fun initViews() {


        btnHomeEditZakaatStartDate.setOnClickListener {
            onEditZakaatStartDateClick(it)
        }

        btnHomeViewBankDetails.setOnClickListener{
            onViewBankDetailsClick(it)
        }

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

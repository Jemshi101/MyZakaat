package com.quartzbit.myzakaat.dialogs


import android.app.Activity
import android.app.Dialog
import android.graphics.Typeface
import android.view.HapticFeedbackConstants
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import com.quartzbit.myzakaat.R
import com.quartzbit.myzakaat.app.App
import com.quartzbit.myzakaat.config.TypefaceCache
import java.util.*


/**
 * Created by Jemsheer K D on 11 January, 2017.
 * Package com.wakilishwa.dialogs
 * Project Wakilishwa
 */

class SelectDateDialog(private val mContext: Activity) {
    private lateinit var typeface: Typeface
    //    private final Vibrator mVibrator;
    var selectDateDialogActionListener: SelectDateDialogActionListener? = null
    private lateinit var dialogSelectDate: Dialog
    private lateinit var datePicker: DatePicker

    var currentDate: String = ""
        set(currentDate) {
            field = currentDate
            val cal = App.getUserTime(currentDate)
            datePicker.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))

        }

    init {

        try {
            typeface = TypefaceCache.getInstance().getTypeface(mContext.applicationContext, "Roboto-Regular.ttf")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        //        mVibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        setSelectDateDialog()
    }

    fun show() {
        dialogSelectDate.show()
    }

    private fun setSelectDateDialog() {

        dialogSelectDate = Dialog(mContext, R.style.ThemeDialogCustom_NoBackground)
        dialogSelectDate.setContentView(R.layout.dialog_select_date)
        dialogSelectDate.setTitle(R.string.label_select_date)

        datePicker = dialogSelectDate.findViewById<View>(R.id.datePicker_dialog_select_date) as DatePicker

        val btnSubmit = dialogSelectDate.findViewById<View>(R.id.btn_dialog_select_date) as Button
        btnSubmit.typeface = typeface

        btnSubmit.setOnClickListener { v ->
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            //                mVibrator.vibrate(25);

            val cal = Calendar.getInstance()
            cal.set(Calendar.YEAR, datePicker.year)
            cal.set(Calendar.MONTH, datePicker.month)
            cal.set(Calendar.DAY_OF_MONTH, datePicker.dayOfMonth)
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)

            selectDateDialogActionListener?.actionCompletedSuccessfully(cal)
            dialogSelectDate.dismiss()
        }
    }

    interface SelectDateDialogActionListener {
        fun actionCompletedSuccessfully(cal: Calendar)

        fun actionFailed(errorMsg: String)
    }
}

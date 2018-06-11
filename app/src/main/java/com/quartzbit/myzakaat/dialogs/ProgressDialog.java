package com.quartzbit.myzakaat.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.quartzbit.myzakaat.R;

/**
 * Created by Jemsheer K D on 23 January, 2018.
 * Package com.quartzbit.myzakaat.dialogs
 * Project Dearest
 */

public class ProgressDialog {

    private final Activity mContext;
    private ProgressDialogActionListener progressDialogActionListener;
    private Dialog dialogProgress;
    private ProgressBar progress;
    private TextView txtMessage;

    public ProgressDialog(Activity mContext) {
        this.mContext = mContext;

        setProgressDialog();
    }

    public void show() {
        dialogProgress.show();
    }

    public void dismiss() {
        dialogProgress.dismiss();
    }

    public void setCanceledOnTouch(boolean isCancellable) {
        dialogProgress.setCanceledOnTouchOutside(isCancellable);
    }

    private void setProgressDialog() {

        dialogProgress = new Dialog(mContext, R.style.ThemeDialogCustom_NoBackground);
        dialogProgress.setContentView(R.layout.dialog_progress);
        dialogProgress.setTitle(R.string.label_please_wait);

        progress = (ProgressBar) dialogProgress.findViewById(R.id.progress_dialog_progress);
        txtMessage = (TextView) dialogProgress.findViewById(R.id.txt_dialog_progress_message);

    }

    public interface ProgressDialogActionListener {
        void actionCompletedSuccessfully(String message);

        void actionFailed(String errorMsg);

        void onSnackBarShow(String message);

        void onSwipeRefreshChange(boolean isSwipeRefreshing);
    }

    public ProgressDialogActionListener getProgressDialogActionListener() {
        return progressDialogActionListener;
    }


    public void setProgressDialogActionListener(ProgressDialogActionListener progressDialogActionListener) {
        this.progressDialogActionListener = progressDialogActionListener;
    }
}

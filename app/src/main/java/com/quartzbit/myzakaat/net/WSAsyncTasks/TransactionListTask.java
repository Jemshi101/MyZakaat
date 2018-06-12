package com.quartzbit.myzakaat.net.WSAsyncTasks;

import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.quartzbit.myzakaat.model.TransactionBean;
import com.quartzbit.myzakaat.model.TransactionListBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Jemsheer K D on 12 June, 2018.
 * Package com.quartzbit.myzakaat.net.WSAsyncTasks
 * Project MyZakaat
 */
public class TransactionListTask extends AsyncTask<Void, Void, TransactionListBean> {
    private com.google.api.services.sheets.v4.Sheets mService = null;
    private Exception mLastError = null;
    private TransactionListTaskListener transactionListTaskListener;

    public TransactionListTask(HashMap<String, String> urlParams, GoogleAccountCredential credential) {
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        mService = new com.google.api.services.sheets.v4.Sheets.Builder(
                transport, jsonFactory, credential)
                .setApplicationName("MyZakaat")
                .build();
    }

    /**
     * Background task to call Google Sheets API.
     *
     * @param params no parameters needed for this task.
     */
    @Override
    protected TransactionListBean doInBackground(Void... params) {
        try {
            return getDataFromApi();
        } catch (Exception e) {
            mLastError = e;
            cancel(true);
            return null;
        }
    }

    /**
     * Fetch a list of names and majors of students in a sample spreadsheet:
     * https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
     *
     * @return List of names and majors
     * @throws IOException
     */
    private TransactionListBean getDataFromApi() throws IOException {
        String spreadsheetId = "1ziNJFrjbc_UoaZLQqS2Gd2jPCjLc9ugPcE4C7qgg3f4";
        String range = "Class Data!A22:J";
        List<String> results = new ArrayList<String>();
        ValueRange response = this.mService.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        List<List<Object>> values = response.getValues();
        TransactionListBean transactionListBean = new TransactionListBean();
        if (values != null) {
            results.add("Name, Major");
            for (List row : values) {
                TransactionBean transactionBean = new TransactionBean();
                transactionBean.setId(values.indexOf(row));
            }
        }
        return transactionListBean;
    }


    @Override
    protected void onPostExecute(TransactionListBean result) {
        super.onPostExecute(result);
        if (result != null)
            transactionListTaskListener.dataDownloadedSuccessfully(result);
        else
            transactionListTaskListener.dataDownloadFailed();
    }

    @Override
    protected void onCancelled() {
        transactionListTaskListener.onCancelled(mLastError);
    }

    public static interface TransactionListTaskListener {
        void dataDownloadedSuccessfully(TransactionListBean transactionListBean);

        void dataDownloadFailed();

        void onCancelled(Exception mLastError);
    }

    public TransactionListTaskListener getTransactionListTaskListener() {
        return transactionListTaskListener;
    }

    public void setTransactionListTaskListener(TransactionListTaskListener transactionListTaskListener) {
        this.transactionListTaskListener = transactionListTaskListener;
    }
}
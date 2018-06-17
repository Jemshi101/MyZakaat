package com.quartzbit.myzakaat.net.WSAsyncTasks;

import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.gson.Gson;
import com.quartzbit.myzakaat.app.App;
import com.quartzbit.myzakaat.model.TransactionBean;
import com.quartzbit.myzakaat.model.TransactionListBean;
import com.quartzbit.myzakaat.net.utils.SheetUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Jemsheer K D on 12 June, 2018.
 * Package com.quartzbit.myzakaat.net.WSAsyncTasks
 * Project MyZakaat
 */
public class TransactionListTask extends AsyncTask<Void, Void, TransactionListBean> {

    private static final String TAG = "TransactionListT";
    private final HashMap<String, String> urlParams;
    private com.google.api.services.sheets.v4.Sheets mService = null;
    private Exception mLastError = null;
    private TransactionListTaskListener transactionListTaskListener;

    public TransactionListTask(HashMap<String, String> urlParams, GoogleAccountCredential credential) {
        this.urlParams = urlParams;
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
        } catch (UserRecoverableAuthIOException e) {
            mLastError = e;
            e.printStackTrace();
            transactionListTaskListener.onPermissionError(e);
            return null;
        } catch (Exception e) {
            mLastError = e;
            e.printStackTrace();
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
        /*String spreadsheetId = "1ziNJFrjbc_UoaZLQqS2Gd2jPCjLc9ugPcE4C7qgg3f4";
        String range = "Class Data!A22:J";*/
        String spreadsheetId = urlParams.get("id");
        String range = urlParams.get("range");
        ValueRange response = this.mService.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        List<List<Object>> values = response.getValues();
        TransactionListBean transactionListBean = new TransactionListBean();
        if (values != null) {
            for (List row : values) {
//                Log.i(TAG, "getDataFromApi: VALUES : " + new Gson().toJson(row));
                TransactionBean transactionBean = new TransactionBean();
                transactionBean.setId(values.indexOf(row));
                transactionBean.setDate(transactionBean.dateToMillis(
                        transactionBean.formatDate(
                                SheetUtil.Companion.getString(row.get(0)))));
                transactionBean.setDateString(App.getDateFromUnix(App.DATE_FORMAT_5, false,
                        false, transactionBean.getDate(), false));
                transactionBean.setDescription(SheetUtil.Companion.getString(row.get(1)));
                transactionBean.setReference(SheetUtil.Companion.getString(row.get(2)));
                transactionBean.setDebit(SheetUtil.Companion.getFloat(row.get(3)));
                transactionBean.setCredit(SheetUtil.Companion.getFloat(row.get(4)));
                transactionBean.setBalance(SheetUtil.Companion.getFloat(row.get(5)));
                transactionBean.setBankCut(SheetUtil.Companion.getFloat(row.get(6)));
                transactionBean.setRealBalance(SheetUtil.Companion.getFloat(row.get(7)));
                transactionBean.setInterest(SheetUtil.Companion.getFloat(row.get(8)));
                transactionBean.setLowestAmount(SheetUtil.Companion.getFloat(row.get(9)));
//                Log.i(TAG, "getDataFromApi: TRANSACTION BEAN : " + new Gson().toJson(transactionBean));
                transactionListBean.getTransactions().add(transactionBean);
            }
        }
        transactionListBean.setStatus("Success");
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

        void onPermissionError(UserRecoverableAuthIOException e);
    }

    public TransactionListTaskListener getTransactionListTaskListener() {
        return transactionListTaskListener;
    }

    public void setTransactionListTaskListener(TransactionListTaskListener transactionListTaskListener) {
        this.transactionListTaskListener = transactionListTaskListener;
    }
}
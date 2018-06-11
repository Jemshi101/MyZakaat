package com.quartzbit.myzakaat.util;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;


import com.quartzbit.myzakaat.model.PhoneContactsBean;

import java.util.ArrayList;

import com.quartzbit.myzakaat.app.App;
import com.quartzbit.myzakaat.model.ContactBean;

/**
 * Created by Jemsheer K D on 20 October, 2017.
 * Package in.techware.pixel8.net.WSAsyncTasks
 * Project PXL8
 */

public class GetPhoneContactsTask extends AsyncTask<String, ContactBean, PhoneContactsBean> {

    private PhoneContactsBean phoneContactsBean;
    private GetPhoneContactsTaskListener getPhoneContactsTaskListener;

    public GetPhoneContactsTask() {
        super();
        phoneContactsBean = new PhoneContactsBean();
    }

    @Override
    protected PhoneContactsBean doInBackground(String... params) {
        System.out.println(">>>>>>>>>doInBackground");


        phoneContactsBean.setContacts(getContacts());

        return phoneContactsBean;

    }


    @Override
    protected void onProgressUpdate(ContactBean... values) {
        getPhoneContactsTaskListener.onContactAdded(values[0]);
        super.onProgressUpdate(values);
    }


    @Override
    protected void onPostExecute(PhoneContactsBean phoneContactsBean) {
        super.onPostExecute(phoneContactsBean);
        if (phoneContactsBean != null)
            getPhoneContactsTaskListener.onContactLoadCompleted(phoneContactsBean);
        else
            getPhoneContactsTaskListener.dataDownloadFailed();
    }

    private ArrayList<ContactBean> getContacts() {

        ArrayList<ContactBean> phoneContacts = new ArrayList<>();
        String phoneNumber = null;
        String email = null;
        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
        Uri EmailCONTENT_URI = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
        String DATA = ContactsContract.CommonDataKinds.Email.DATA;
        StringBuilder output = new StringBuilder();
        ContentResolver contentResolver = App.getInstance().getContentResolver();

        Cursor cursor;
        /*if (isSearch) {
            CONTENT_URI = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,Uri.encode(searchParam));
            String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};
            cursor = contentResolver.query(CONTENT_URI, projection, null, null,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        } else {*/
        cursor = contentResolver.query(CONTENT_URI, null, null, null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
//        }
        // Loop for every contact in the phone
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                String contact_id = cursor.getString(cursor.getColumnIndex(_ID));
                String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));

                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {
                    ContactBean contactBean = new ContactBean();
                    contactBean.setId(contact_id);
                    contactBean.setName(name);
                    System.out.print("\n" + name);
                    // Query and loop for every phone number of the contact
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null,
                            Phone_CONTACT_ID + " = ?", new String[]{contact_id}, null);
                    contactBean.setPhoneNumbers(new ArrayList<String>());
                    while (phoneCursor.moveToNext()) {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        contactBean.getPhoneNumbers().add(phoneNumber);
                        System.out.print(" " + phoneNumber);
                    }
                    phoneCursor.close();
                    // Query and loop for every email of the contact
                    Cursor emailCursor = contentResolver.query(EmailCONTENT_URI, null,
                            EmailCONTACT_ID + " = ?", new String[]{contact_id}, null);
                    contactBean.setEmails(new ArrayList<String>());
                    while (emailCursor.moveToNext()) {
                        email = emailCursor.getString(emailCursor.getColumnIndex(DATA));
                        contactBean.getEmails().add(email);
                        System.out.print(" " + email);
                    }
                    emailCursor.close();

                    try {
                        String photo = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                        contactBean.setProfilePhoto(photo != null ? photo : "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    phoneContacts.add(contactBean);
                    publishProgress(contactBean);
//                    getPhoneContactsTaskListener.onContactAdded(contactBean);
                }
            }
        }
        cursor.close();
        return phoneContacts;
    }


    public static interface GetPhoneContactsTaskListener {
        void onContactLoadCompleted(PhoneContactsBean phoneContactsBean);

        void onContactAdded(ContactBean contactBean);

        void dataDownloadFailed();
    }

    public GetPhoneContactsTaskListener getGetPhoneContactsTaskListener() {
        return getPhoneContactsTaskListener;
    }

    public void setGetPhoneContactsTaskListener(GetPhoneContactsTaskListener getPhoneContactsTaskListener) {
        this.getPhoneContactsTaskListener = getPhoneContactsTaskListener;
    }
}

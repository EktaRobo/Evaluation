package com.example.ekta.evaluation.utilities;

import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ekta.evaluation.R;
import com.example.ekta.evaluation.adapters.ContactsAdapter;
import com.example.ekta.evaluation.models.Contact;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by ekta on 12/4/17.
 */

public class ContactsDialogFragment extends DialogFragment {
    private static final String TAG = ContactsDialogFragment.class.getSimpleName();
    private static final int MIN_DIGITS_FOR_PHONE_NUMBER = 10;
    private RecyclerView mContactListView;
    private static final String[] PROJECTION = {
            Contacts._ID,
            Contacts.LOOKUP_KEY,
            Contacts.DISPLAY_NAME_PRIMARY
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle
            savedInstanceState) {
        // Inflate the fragment layout
        View root = inflater.inflate(R.layout.dialog_fragment, container, false);
        mContactListView = (RecyclerView) root.findViewById(R.id.contact_recycler_view);
        mContactListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mContactListView.setItemAnimator(new DefaultItemAnimator());
        return root;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       // Initializes a loader for loading the contacts
        new ContactFetchAsyncTask().execute();

    }

    public class ContactFetchAsyncTask extends AsyncTask<Void, Integer, ArrayList<Contact>> {

        @Override
        protected ArrayList<Contact> doInBackground(Void... params) {
            ArrayList<Contact> contactList = new ArrayList<>();
            ArrayList<String> addedNums = new ArrayList<>();
            long startTime = Calendar.getInstance().getTimeInMillis();
            int count = 0;
            final Uri contentUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            final String selection = ContactsContract.Contacts.HAS_PHONE_NUMBER + " = '" + ("1") + "' AND  " +
                    /*ContactsContract.CommonDataKinds.Phone.NUMBER + " REGEXP '" + StringUtils.AT_LEAST_10_DIGITS_IN_STRING_REG_EX + "'";*/
                    " LENGTH (" + ContactsContract.CommonDataKinds.Phone.NUMBER + ") >= " + MIN_DIGITS_FOR_PHONE_NUMBER;
            final String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                    ContactsContract.CommonDataKinds.Phone.PHOTO_URI,
                    ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER
            };
            final String[] selectionArgs = null;
            final String sortOrder = "upper(" + ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + ") COLLATE LOCALIZED ASC";
            Cursor phones = getActivity().getContentResolver().query(contentUri, projection, selection,
                    selectionArgs, sortOrder);
            while (phones.moveToNext()) {
                String id = phones
                        .getString(phones
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID));
                String name = phones
                        .getString(phones
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNumber = phones
                        .getString(phones
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String photo = phones
                        .getString(phones
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                final String phoneNumberWithoutChars = phoneNumber.replaceAll("[^0-9]", "");
                if (phoneNumberWithoutChars.length() >= MIN_DIGITS_FOR_PHONE_NUMBER) {
                    Contact contact = new Contact();
                    contact.setContactName(name);
                    contact.setContactNumber(phoneNumber);
                    contactList.add(contact);

                    final String tenDigitNumber;
//                    tenDigitNumber = StringUtils.getFormattedTenDigitMobileNumber(phoneNumberWithoutChars);
                    tenDigitNumber = phoneNumberWithoutChars;
                    if (!addedNums.contains(tenDigitNumber)) {
                        addedNums.add(tenDigitNumber);
                        if (!contactList.contains(contact)) {
                            Log.e(TAG, "Newly added Id = " + id + " , Name :" + name + " , phoneNumber =" + phoneNumberWithoutChars);
//                            contactList.add(contact);
//                            contact.addPhoneNumber(new PhoneNumber(phoneNumber));
                        } else {
                            int index = contactList.indexOf(contact);
                            Contact addedContact = contactList.get(index);
//                            addedContact.addPhoneNumber(new PhoneNumber(phoneNumber));
                            Log.e(TAG, "Updated Id = " + id + " , Name :" + name + " , phoneNumber =" + phoneNumberWithoutChars);
                        }
                    } else {
                        Log.e(TAG, "Already added Id = " + id + " , Name :" + name + " , phoneNumber =" + phoneNumberWithoutChars);
                    }
                    publishProgress(count++);
                } else {
                    Log.e(TAG, "INVALID CONTACT:  ======= >  Id = " + id + " , Name :" + name + " , phoneNumber =" + phoneNumber);
                }

            }
//            DbHelper.createOrUpdateContactList(contactList);
            phones.close();

            long endtTime = Calendar.getInstance().getTimeInMillis();
            final long ellpsedTime = endtTime - startTime;
            double seconds = ellpsedTime / 1000.0;
            Log.e(TAG, "Read took :" + seconds + " Seconds");
            Log.e(TAG, "Contacts Count :" + count);
            return contactList;
        }

        @Override
        protected void onPostExecute(ArrayList<Contact> contacts) {
            super.onPostExecute(contacts);
            ContactsAdapter contactsAdapter = new ContactsAdapter(contacts);
            mContactListView.setAdapter(contactsAdapter);
        }
    }



}

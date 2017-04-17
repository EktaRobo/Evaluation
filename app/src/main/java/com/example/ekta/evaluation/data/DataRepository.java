package com.example.ekta.evaluation.data;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.ekta.evaluation.constants.Constants;
import com.example.ekta.evaluation.data.database.DatabaseHelper;
import com.example.ekta.evaluation.data.database.models.SavedCard;
import com.example.ekta.evaluation.models.RechargeDetails;
import com.example.ekta.evaluation.utilities.RechargeUtils;
import com.stripe.android.model.Token;

import java.util.ArrayList;

/**
 * Created by ekta on 4/4/17.
 */

public class DataRepository implements DataSource{
    private static final String TAG = DataRepository.class.getSimpleName();
    //TODO

    @Override
    public void saveCard(Token token) {
        SavedCard savedCard = new SavedCard();
        savedCard.setMaskedCardNumber(token.getCard().getLast4());
        savedCard.setTokenId(token.getId());
        DatabaseHelper.addSavedCardToDatabase(savedCard);

    }

    @Override
    public ArrayList<SavedCard> getSavedCards() {
        return DatabaseHelper.getAllSavedCards();
    }

    @Override
    public void saveRechargeDetails(RechargeDetails rechargeDetails) {
        DatabaseHelper.addPaymentSuccessDetailsToDatabase(rechargeDetails);
    }

    @Override
    public ArrayList<RechargeDetails> getSuccessfulRechargeDetailList() {
        return DatabaseHelper.getRechargeHistory();
    }

    @Override
    public void makePayment(final PaymentCallback paymentCallback, final RechargeDetails rechargeDetails, final String tokenId) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                paymentCallback.onPaymentSuccess(rechargeDetails, tokenId);
            }
        }, 2000);
    }

    @Nullable
    public String getMobileNumber(Intent data, ContentResolver contentProvider) {
        String phoneNo = null;
        String name = null;

        Uri contactData = data.getData();
        Cursor cursor = contentProvider.query(contactData, null, null, null, null);
        if (cursor.moveToFirst()) {

            String id = cursor.getString(cursor.getColumnIndexOrThrow
                    (ContactsContract.Contacts._ID));

            String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract
                    .Contacts.HAS_PHONE_NUMBER));

            if (hasPhone.equalsIgnoreCase(Constants.ONE)) {
                Cursor phones = contentProvider.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                        null, null);
                phones.moveToFirst();
                phoneNo = phones.getString(phones.getColumnIndex(Constants.DATA_1));
                phones.close();
            }
            name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts
                    .DISPLAY_NAME));
            cursor.close();

            phoneNo = RechargeUtils.getFormattedTenDigitMobileNumber(phoneNo);

        }
        Log.e(TAG, "Name and Contact number is " + name + ", " + phoneNo);
        return phoneNo;
    }
}

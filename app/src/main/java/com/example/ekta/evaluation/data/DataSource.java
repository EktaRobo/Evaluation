package com.example.ekta.evaluation.data;

import android.content.ContentResolver;
import android.content.Intent;

import com.example.ekta.evaluation.data.database.models.SavedCard;
import com.example.ekta.evaluation.models.RechargeDetails;
import com.stripe.android.model.Token;

import java.util.ArrayList;

/**
 * Created by ekta on 4/4/17.
 */

public interface DataSource {

    void saveCard(Token token);

    ArrayList<SavedCard> getSavedCards();

    void saveRechargeDetails(RechargeDetails rechargeDetails);

    String getMobileNumber(Intent data, ContentResolver contentProvider);

    ArrayList<RechargeDetails> getSuccessfulRechargeDetailList();

    void makePayment(PaymentCallback paymentCallback, RechargeDetails rechargeDetails, String
            tokenId);

    interface PaymentCallback {

        void onPaymentSuccess(RechargeDetails rechargeDetails, String tokenId);

        void onPaymentFailure(String error);
    }
}

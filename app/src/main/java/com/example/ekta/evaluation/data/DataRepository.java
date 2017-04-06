package com.example.ekta.evaluation.data;

import android.os.Handler;

import com.example.ekta.evaluation.data.database.DatabaseHelper;
import com.example.ekta.evaluation.data.database.models.SavedCard;
import com.example.ekta.evaluation.models.RechargeDetails;
import com.stripe.android.model.Token;

import java.util.ArrayList;

/**
 * Created by ekta on 4/4/17.
 */

public class DataRepository implements DataSource{
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
}

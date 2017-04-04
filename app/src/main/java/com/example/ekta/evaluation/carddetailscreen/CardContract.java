package com.example.ekta.evaluation.carddetailscreen;

import com.example.ekta.evaluation.data.database.models.SavedCard;
import com.example.ekta.evaluation.models.RechargeDetails;
import com.stripe.android.Stripe;
import com.stripe.android.model.Card;

import java.util.ArrayList;

/**
 * Created by ekta on 4/4/17.
 */

public interface CardContract {
    interface View {
        void showProgress();

        void hideProgress();

        void displayToast(String message);

        RechargeDetails getRechargeDetails();

        void navigateToPaymentActivity(RechargeDetails rechargeDetails, String tokenId);
    }

    interface Presenter {

        void createToken(Stripe stripe, Card cardToSave);

        ArrayList<SavedCard> getSavedCards();

        void makePayment(RechargeDetails rechargeDetails, String tokenId);

    }
}

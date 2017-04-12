package com.example.ekta.evaluation.ui.carddetailscreen;

import com.example.ekta.evaluation.data.DataSource;
import com.example.ekta.evaluation.data.database.models.SavedCard;
import com.example.ekta.evaluation.models.RechargeDetails;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import java.util.ArrayList;

/**
 * Created by ekta on 4/4/17.
 */

public class CardDetailPresenter implements CardContract.Presenter, TokenCallback, DataSource
        .PaymentCallback {
    private DataSource mDataRepository;
    private CardContract.View mView;

    public CardDetailPresenter(DataSource dataRepository, CardContract.View view) {
        mDataRepository = dataRepository;
        mView = view;
    }

    @Override
    public void createToken(Stripe stripe, Card cardToSave) {
        if (stripe != null) {
            mView.showProgress();
            stripe.createToken(cardToSave, this);
        }
    }

    @Override
    public ArrayList<SavedCard> getSavedCards() {
        return mDataRepository.getSavedCards();
    }

    @Override
    public void makePayment(RechargeDetails rechargeDetails, String tokenId) {
        mView.showProgress();
        mDataRepository.makePayment(this, getRechargeDetails(), tokenId);
    }

    @Override
    public void onError(Exception error) {
        mView.hideProgress();
        mView.displayToast(error.getLocalizedMessage());

    }

    @Override
    public void onSuccess(Token token) {
        mView.hideProgress();
        mDataRepository.saveCard(token);
        makePayment(getRechargeDetails(), token.getId());

    }

    private RechargeDetails getRechargeDetails() {
        return mView.getRechargeDetails();
    }

    @Override
    public void onPaymentSuccess(RechargeDetails rechargeDetails, String tokenId) {
        mView.hideProgress();
        mDataRepository.saveRechargeDetails(rechargeDetails);
        mView.navigateToPaymentSuccessActivity(rechargeDetails, tokenId);
    }


    @Override
    public void onPaymentFailure(String error) {

    }
}

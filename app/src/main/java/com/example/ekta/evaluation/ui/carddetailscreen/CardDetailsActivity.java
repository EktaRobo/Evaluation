package com.example.ekta.evaluation.ui.carddetailscreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ekta.evaluation.R;
import com.example.ekta.evaluation.adapters.SavedCardsAdapter;
import com.example.ekta.evaluation.application.App;
import com.example.ekta.evaluation.constants.Constants;
import com.example.ekta.evaluation.data.database.models.SavedCard;
import com.example.ekta.evaluation.listeners.OnSavedCardClickedListener;
import com.example.ekta.evaluation.models.RechargeDetails;
import com.example.ekta.evaluation.ui.paymentsuccess.PaymentSuccessActivity;
import com.example.ekta.evaluation.utilities.LoaderDialogUtil;
import com.stripe.android.Stripe;
import com.stripe.android.exception.AuthenticationException;
import com.stripe.android.model.Card;
import com.stripe.android.view.CardInputWidget;

public class CardDetailsActivity extends AppCompatActivity implements OnSavedCardClickedListener,
        CardContract.View, View.OnClickListener {


    private CardInputWidget mCardInputWidget;
    private CardContract.Presenter mPresenter;
    private ImageView mEnterCardArrow;
    private ImageView mSelectCardArrow;
    private RecyclerView mRecyclerView;
    private SavedCard mSavedCard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_details);
        init();
    }

    private void init() {
        mCardInputWidget = (CardInputWidget) findViewById(R.id.card_input_widget);
        mEnterCardArrow = (ImageView) findViewById(R.id.enter_card_arrow);
        mSelectCardArrow = (ImageView) findViewById(R.id.select_card_arrow);
        mPresenter = new CardDetailPresenter(App.getDataRepository(), this);
        initRecyclerView();
        initClickListener();
    }


    private void initClickListener() {
        findViewById(R.id.pay).setOnClickListener(this);
        findViewById(R.id.enter_card_details).setOnClickListener(this);
        findViewById(R.id.select_from_previous_cards).setOnClickListener(this);

    }


    @Override
    public void displayToast(String message) {
        Toast.makeText(CardDetailsActivity.this, message, Toast.LENGTH_LONG)
                .show();
    }

    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        SavedCardsAdapter adapter = new SavedCardsAdapter(mPresenter.getSavedCards(),
                this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onSavedCardClicked(SavedCard savedCard) {
//        displayToast(savedCard.getTokenId());
//        mPresenter.makePayment(getRechargeDetails(), savedCard.getTokenId());
        mSavedCard = savedCard;
    }


    @Override
    public RechargeDetails getRechargeDetails() {
        Intent intent = getIntent();
        RechargeDetails rechargeDetails = null;
        if (intent != null) {
            rechargeDetails = intent.getParcelableExtra(Constants.RECHARGE_DETAILS);

        }
        return rechargeDetails;
    }

    @Override
    public void navigateToPaymentSuccessActivity(RechargeDetails rechargeDetails, String tokenId) {
        Intent paymentIntent = new Intent(CardDetailsActivity.this, PaymentSuccessActivity.class);
        paymentIntent.putExtra(Constants.RECHARGE_DETAILS, rechargeDetails);
        paymentIntent.putExtra(Constants.TOKEN_ID, tokenId);
        paymentIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(paymentIntent);
        finish();
    }


    @Override
    public void showProgress() {
        LoaderDialogUtil.getInstance().showLoader(this);
    }

    @Override
    public void hideProgress() {
        LoaderDialogUtil.getInstance().dismissLoader(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pay:
                if (mCardInputWidget.getVisibility() == View.VISIBLE) {
                    Card cardToSave = mCardInputWidget.getCard();
                    Stripe stripe = null;
                    if (cardToSave == null) {
                        displayToast(getString(R.string.invalid_card_data));
                    } else {
                        try {
                            stripe = new Stripe(CardDetailsActivity.this, Constants
                                    .PUBLISHABLE_KEY);
                        } catch (AuthenticationException e) {
                            e.printStackTrace();
                            displayToast(getString(R.string.stripe_problem));
                        }
                        mPresenter.createToken(stripe, cardToSave);
                    }
                } else {
                    mPresenter.makePayment(getRechargeDetails(), mSavedCard.getTokenId());
                }
                break;

            case R.id.enter_card_details:
                mCardInputWidget.setVisibility(View.VISIBLE);
                mEnterCardArrow.setImageDrawable(ContextCompat.getDrawable(this, R.drawable
                        .ic_keyboard_arrow_down_black_24px));
                mSelectCardArrow.setImageDrawable(ContextCompat.getDrawable(this, R.drawable
                        .ic_keyboard_arrow_right_black_24px));
                mRecyclerView.setVisibility(View.GONE);
                break;

            case R.id.select_from_previous_cards:
                mCardInputWidget.setVisibility(View.GONE);
                mEnterCardArrow.setImageDrawable(ContextCompat.getDrawable(this, R.drawable
                        .ic_keyboard_arrow_right_black_24px));
                mSelectCardArrow.setImageDrawable(ContextCompat.getDrawable(this, R.drawable
                        .ic_keyboard_arrow_down_black_24px));
                mRecyclerView.setVisibility(View.VISIBLE);
                break;
        }

    }
}

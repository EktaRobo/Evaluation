package com.example.ekta.evaluation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.ekta.evaluation.adapters.SavedCardsAdapter;
import com.example.ekta.evaluation.constants.Constants;
import com.example.ekta.evaluation.data.DataRepository;
import com.example.ekta.evaluation.data.DataSource;
import com.example.ekta.evaluation.data.database.models.SavedCard;
import com.example.ekta.evaluation.listeners.OnSavedCardClickedListener;
import com.example.ekta.evaluation.models.RechargeDetails;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.exception.AuthenticationException;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;

public class CardDetailsActivity extends AppCompatActivity implements OnSavedCardClickedListener,
        TokenCallback, DataSource.PaymentCallback{


    private CardInputWidget mCardInputWidget;
    private Stripe mStripe;
    private DataRepository mDataRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_details);
        init();
    }

    private void init() {
        mCardInputWidget = (CardInputWidget) findViewById(R.id.card_input_widget);
        initStripe();
        initRecyclerView();
        initClickListener();
    }

    private void initStripe() {
        try {
            mStripe = new Stripe(this, Constants.PUBLISHABLE_KEY);
        } catch (AuthenticationException e) {
            e.printStackTrace();
            Toast.makeText(CardDetailsActivity.this, R.string.stripe_problem, Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void initClickListener() {
        findViewById(R.id.pay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Card cardToSave = mCardInputWidget.getCard();
                if (cardToSave == null) {
                    Toast.makeText(CardDetailsActivity.this, R.string.invalid_card_data, Toast.LENGTH_LONG)
                            .show();
                } else {
                    Toast.makeText(CardDetailsActivity.this, cardToSave.toString(), Toast.LENGTH_LONG)
                            .show();
                    mStripe.createToken(cardToSave, CardDetailsActivity.this);
                }
            }
        });
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mDataRepository = new DataRepository();
        SavedCardsAdapter adapter = new SavedCardsAdapter(mDataRepository.getSavedCards(),
                this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onSavedCardClicked(SavedCard savedCard) {
        Toast.makeText(CardDetailsActivity.this,
                savedCard.getTokenId(),
                Toast.LENGTH_LONG).show();
        mDataRepository.makePayment(this, getRechargeDetails(), savedCard.getTokenId());
    }

    @Override
    public void onError(Exception error) {
        Toast.makeText(CardDetailsActivity.this,
                error.getLocalizedMessage(),
                Toast.LENGTH_LONG).show();

    }

    @Override
    public void onSuccess(Token token) {
        Toast.makeText(CardDetailsActivity.this,
                token.getId(),
                Toast.LENGTH_LONG).show();
        mDataRepository.saveCard(token);

        mDataRepository.makePayment(this, getRechargeDetails(), token.getId());
    }

    private RechargeDetails getRechargeDetails() {
        Intent intent = getIntent();
        RechargeDetails rechargeDetails = null;
        if (intent != null) {
            rechargeDetails = intent.getParcelableExtra(Constants.RECHARGE_DETAILS);

        }
        return rechargeDetails;
    }

    @Override
    public void onPaymentSuccess(RechargeDetails rechargeDetails, String tokenId) {
        Intent paymentIntent = new Intent(CardDetailsActivity.this, PaymentSuccessActivity.class);
        paymentIntent.putExtra(Constants.RECHARGE_DETAILS, rechargeDetails);
        paymentIntent.putExtra(Constants.TOKEN_ID, tokenId);
        paymentIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(paymentIntent);
        finish();
    }

    @Override
    public void onPaymentFailure(String error) {

    }
}

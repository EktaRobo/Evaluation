package com.example.ekta.evaluation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputFilter;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.Toast;

import com.example.ekta.evaluation.carddetailscreen.CardDetailsActivity;
import com.example.ekta.evaluation.constants.Constants;
import com.example.ekta.evaluation.models.RechargeDetails;

public class RechargeActivity extends AppCompatActivity {
    private AppCompatEditText mOperatorName;
    private AppCompatEditText mMobileNumber;
    private AppCompatEditText mAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_payment);
        init();
    }

    private void init() {
        mOperatorName = (AppCompatEditText) findViewById(R.id.operator_name);
        mMobileNumber = (AppCompatEditText) findViewById(R.id.mobile_number);
        mAmount = (AppCompatEditText) findViewById(R.id.amount);
        setRestrictions();
        findViewById(R.id.cancel_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        findViewById(R.id.pay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOperatorName.getText().toString().equals(Constants.EMPTY)) {
                    displayToast(getString(R.string.operator_name_error));
                } else {
                    String mobile = mMobileNumber.getText().toString();
                    if (mobile.equals(Constants.EMPTY) || mobile.length() < Constants
                            .MAX_LENGTH_MOBILE) {
                        displayToast(getString(R.string.invalid_mobile));
                    } else if (mAmount.getText().toString().equals(Constants.EMPTY)) {
                        displayToast(getString(R.string.amount_error));
                    } else {

                        navigateToCardDetailScreen(fetchRechargeDetails());
                    }
                }
            }
        });
    }

    public void displayToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG)
                .show();
    }

    @NonNull
    private RechargeDetails fetchRechargeDetails() {
        RechargeDetails rechargeDetails = new RechargeDetails();
        rechargeDetails.setOperatorName(mOperatorName.getText().toString());
        rechargeDetails.setMobileNumber(mMobileNumber.getText().toString());
        rechargeDetails.setAmount(mAmount.getText().toString());
        return rechargeDetails;
    }

    private void setRestrictions() {
        mMobileNumber.setKeyListener(new DigitsKeyListener());
        mAmount.setKeyListener(new DigitsKeyListener());
        mMobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(
                Constants.MAX_LENGTH_MOBILE)});
    }

    private void navigateToCardDetailScreen(RechargeDetails rechargeDetails) {
        Intent intent = new Intent(RechargeActivity.this, CardDetailsActivity.class);
        intent.putExtra(Constants.RECHARGE_DETAILS, rechargeDetails);
        startActivity(intent);
    }
}

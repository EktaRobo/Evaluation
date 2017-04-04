package com.example.ekta.evaluation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;

import com.example.ekta.evaluation.constants.Constants;
import com.example.ekta.evaluation.models.RechargeDetails;

public class RechargeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_payment);
        init();
    }

    private void init() {
        findViewById(R.id.cancel_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        findViewById(R.id.pay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToCardDetailScreen(fetchRechargeDetails());
            }
        });
    }

    @NonNull
    private RechargeDetails fetchRechargeDetails() {
        RechargeDetails rechargeDetails = new RechargeDetails();
        AppCompatEditText operatorName = (AppCompatEditText) findViewById(R.id.operator_name);
        rechargeDetails.setOperatorName(operatorName.getText().toString());
        AppCompatEditText mobileNumber = (AppCompatEditText) findViewById(R.id.mobile_number);
        rechargeDetails.setMobileNumber(mobileNumber.getText().toString());
        AppCompatEditText amount = (AppCompatEditText) findViewById(R.id.amount);
        rechargeDetails.setAmount(amount.getText().toString());
        return rechargeDetails;
    }

    private void navigateToCardDetailScreen(RechargeDetails rechargeDetails) {
        Intent intent = new Intent(RechargeActivity.this, CardDetailsActivity.class);
        intent.putExtra(Constants.RECHARGE_DETAILS, rechargeDetails);
        startActivity(intent);
    }
}

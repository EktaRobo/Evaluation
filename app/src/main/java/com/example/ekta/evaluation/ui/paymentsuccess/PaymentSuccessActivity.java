package com.example.ekta.evaluation.ui.paymentsuccess;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.ekta.evaluation.R;

public class PaymentSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success);
        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        init();
    }

    /*private void init() {
        Intent intent = getIntent();
        RechargeDetails rechargeDetails;
        if (intent != null) {
            rechargeDetails = intent.getParcelableExtra(Constants
                    .RECHARGE_DETAILS);

            String tokenId = intent.getStringExtra(Constants.TOKEN_ID);
                    TextView operatorName = (TextView) findViewById(R.id.operator_name);
            operatorName.setText(rechargeDetails.getOperatorName());
            TextView mobileNumber = (TextView) findViewById(R.id.mobile_number);
            mobileNumber.setText(rechargeDetails.getMobileNumber());
            TextView amount = (TextView) findViewById(R.id.amount);
            amount.setText(rechargeDetails.getAmount());
            TextView token = (TextView) findViewById(R.id.token_id);
            token.setText(tokenId);
        }

        initListeners();
    }

    private void initListeners() {

        findViewById(R.id.recharge_again).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rechargeIntent = new Intent(PaymentSuccessActivity.this, RechargeActivity
                        .class);
                rechargeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(rechargeIntent);
            }
        });
    }*/
}

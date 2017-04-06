package com.example.ekta.evaluation.ui.rechargescreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.text.InputFilter;
import android.text.method.DigitsKeyListener;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ekta.evaluation.R;
import com.example.ekta.evaluation.ui.carddetailscreen.CardDetailsActivity;
import com.example.ekta.evaluation.constants.Constants;
import com.example.ekta.evaluation.models.RechargeDetails;
import com.example.ekta.evaluation.ui.rechargehistory.RechargeHistoryActivity;

public class RechargeActivity extends AppCompatActivity implements AdapterView
        .OnItemSelectedListener {
    private AppCompatEditText mMobileNumberEditText;
    private AppCompatEditText mAmountEditText;
    private String mOperatorName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_payment);
        init();
    }

    private void init() {
        AppCompatSpinner operatorNameSpinner = (AppCompatSpinner) findViewById(R.id.operator_name);
// Create an ArrayAdapter using the string array and a default mOperatorNameSpinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.operators_array, android.R.layout.simple_spinner_item);
        operatorNameSpinner.setOnItemSelectedListener(this);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the mOperatorNameSpinner
        operatorNameSpinner.setAdapter(adapter);
        mMobileNumberEditText = (AppCompatEditText) findViewById(R.id.mobile_number);
        mAmountEditText = (AppCompatEditText) findViewById(R.id.amount);
        setRestrictions();
        findViewById(R.id.cancel_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        initClickListeners();
    }

    private void initClickListeners() {
        findViewById(R.id.pay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOperatorName.equals("") || mOperatorName == null) {
                    displayToast(getString(R.string.operator_name_error));
                } else {
                    String mobile = mMobileNumberEditText.getText().toString();
                    if (mobile.equals(Constants.EMPTY) || mobile.length() < Constants
                            .MAX_LENGTH_MOBILE) {
                        displayToast(getString(R.string.invalid_mobile));
                    } else if (mAmountEditText.getText().toString().equals(Constants.EMPTY)) {
                        displayToast(getString(R.string.amount_error));
                    } else {

                        navigateToCardDetailScreen(fetchRechargeDetails());
                    }
                }
            }
        });

        findViewById(R.id.previous_transaction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent previousTransactionHistory = new Intent(RechargeActivity.this,
                        RechargeHistoryActivity.class);
                startActivity(previousTransactionHistory);

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
        rechargeDetails.setOperatorName(mOperatorName);
        rechargeDetails.setMobileNumber(mMobileNumberEditText.getText().toString());
        rechargeDetails.setAmount(mAmountEditText.getText().toString());
        return rechargeDetails;
    }

    private void setRestrictions() {
        mMobileNumberEditText.setKeyListener(new DigitsKeyListener());
        mAmountEditText.setKeyListener(new DigitsKeyListener());
        mMobileNumberEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(
                Constants.MAX_LENGTH_MOBILE)});
    }

    private void navigateToCardDetailScreen(RechargeDetails rechargeDetails) {
        Intent intent = new Intent(RechargeActivity.this, CardDetailsActivity.class);
        intent.putExtra(Constants.RECHARGE_DETAILS, rechargeDetails);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TextView selectedText = (TextView) parent.getChildAt(0);
        if (selectedText != null) {
            selectedText.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
            selectedText.setGravity(Gravity.RIGHT);
        }
        mOperatorName = (String) parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

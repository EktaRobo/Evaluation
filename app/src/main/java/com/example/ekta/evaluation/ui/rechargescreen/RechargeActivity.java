package com.example.ekta.evaluation.ui.rechargescreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ekta.evaluation.R;
import com.example.ekta.evaluation.application.App;
import com.example.ekta.evaluation.constants.Constants;
import com.example.ekta.evaluation.models.RechargeDetails;
import com.example.ekta.evaluation.ui.carddetailscreen.CardDetailsActivity;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
public class RechargeActivity extends AppCompatActivity implements RechargeContract.View, View
        .OnClickListener {
    private static final String TAG = RechargeActivity.class.getSimpleName();
    private RechargeContract.Presenter mPresenter;
    private AppCompatEditText mMobileNumberEditText;
    private AppCompatEditText mAmountEditText;
    private AppCompatSpinner mOperatorNameSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_payment);
        mPresenter = new RechargePresenter(this, App.getDataRepository());
        mPresenter.start();
    }


    @Override
    public void initUI() {
        initSpinnerAdapter();
        initEditTextViews();
        initRecyclerView();
        initClickListeners();
    }

    private void initEditTextViews() {
        mAmountEditText = (AppCompatEditText) findViewById(R.id.amount);
        mMobileNumberEditText = (AppCompatEditText) findViewById(R.id.contact_number);
        mPresenter.setEditTextRestrictions(mMobileNumberEditText, mAmountEditText);
        mPresenter.addTextChangeListener(mMobileNumberEditText, mOperatorNameSpinner);
    }

    private void initSpinnerAdapter() {
        mOperatorNameSpinner = (AppCompatSpinner) findViewById(R.id.operator_name);
        // Create an ArrayAdapter using the string array and a default mOperatorNameSpinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.operators_array, android.R.layout.simple_spinner_item);
        mPresenter.setAdapterForSpinners(mOperatorNameSpinner, adapter);
    }


    private void initRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recharge_history_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mPresenter.setRecyclerViewAdapter(recyclerView, linearLayoutManager);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.refreshRechargeHistory();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.CONTACTS_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    mPresenter.setSelectedContactNumber(data, getContentResolver());
                } else {
                    Log.e(TAG, "onActivityResult: failed");
                }
                break;
        }
    }

    private void initClickListeners() {
        findViewById(R.id.recharge).setOnClickListener(this);
        findViewById(R.id.contacts).setOnClickListener(this);
    }

    @Override
    public void displayToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void displayToast(int stringId) {
        Toast.makeText(this, getString(stringId), Toast.LENGTH_LONG).show();
    }

    @Override
    public void navigateToCardDetailScreen(RechargeDetails rechargeDetails) {
        Intent intent = new Intent(RechargeActivity.this, CardDetailsActivity.class);
        intent.putExtra(Constants.RECHARGE_DETAILS, rechargeDetails);
        startActivity(intent);
    }

    @Override
    public void setSelectedOperator(TextView selectedText) {
        selectedText.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        selectedText.setGravity(Gravity.RIGHT);
    }

    @Override
    public void setMobileNumber(String mobileNumber) {
        mMobileNumberEditText.setText(mobileNumber);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.recharge:
                String mobile = mMobileNumberEditText.getText().toString();
                String amount = mAmountEditText.getText().toString();
                mPresenter.validateRechargeData(mobile, amount);
                break;

            case R.id.contacts:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                startActivityForResult(intent, Constants.CONTACTS_REQUEST_CODE);
                break;
        }

    }

}

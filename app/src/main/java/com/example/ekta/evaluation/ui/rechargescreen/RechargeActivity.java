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
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ekta.evaluation.R;
import com.example.ekta.evaluation.adapters.RechargeDetailAdapter;
import com.example.ekta.evaluation.application.App;
import com.example.ekta.evaluation.constants.Constants;
import com.example.ekta.evaluation.models.RechargeDetails;
import com.example.ekta.evaluation.ui.carddetailscreen.CardDetailsActivity;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
public class RechargeActivity extends AppCompatActivity implements RechargeContract.View, View
        .OnClickListener, AdapterView.OnItemSelectedListener {
    private static final String TAG = RechargeActivity.class.getSimpleName();
    private RechargeContract.Presenter mPresenter;
    private AppCompatEditText mMobileNumberEditText;
    private AppCompatEditText mAmountEditText;
    private AppCompatSpinner mOperatorNameSpinner;
    private RechargeDetailAdapter mRechargeDetailAdapter;
    private String mOperatorName;


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
        setEditTextRestrictions();
        addTextChangeListener();
    }

    private void initSpinnerAdapter() {
        mOperatorNameSpinner = (AppCompatSpinner) findViewById(R.id.operator_name);
        // Create an ArrayAdapter using the string array and a default mOperatorNameSpinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.operators_array, android.R.layout.simple_spinner_item);
        setAdapterForSpinners(adapter);
    }

    public void setAdapterForSpinners(ArrayAdapter<CharSequence> adapter) {

        mOperatorNameSpinner.setOnItemSelectedListener(this);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the mOperatorNameSpinner
        mOperatorNameSpinner.setAdapter(adapter);
    }


    private void initRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recharge_history_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        setRecyclerViewAdapter(recyclerView, linearLayoutManager);
    }

    public void setRecyclerViewAdapter(RecyclerView recyclerView, RecyclerView.LayoutManager
            linearLayoutManager) {
        if (mPresenter == null) {
            return;
        }
        mRechargeDetailAdapter = new RechargeDetailAdapter(mPresenter
                .getSuccessfulRechargeDetailList());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mRechargeDetailAdapter);
    }

    private void addTextChangeListener() {
        mMobileNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.e(TAG, "beforeTextChanged: s: " + s);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                Log.e(TAG, "onTextChanged: charSequence: " + charSequence);
                mPresenter.changeOperatorName(charSequence);
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e(TAG, "afterTextChanged: s: " + s);

            }
        });
    }

    @Override
    public void setSelectedOperator(int operatorCode) {
        mOperatorNameSpinner.setSelection(operatorCode);
    }

    private void setEditTextRestrictions() {
        mMobileNumberEditText.setKeyListener(new DigitsKeyListener());
        mAmountEditText.setKeyListener(new DigitsKeyListener());
        mMobileNumberEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(
                Constants.MAX_LENGTH_MOBILE)});
    }


    @Override
    protected void onResume() {
        super.onResume();
        refreshRechargeHistory();
    }

    private void refreshRechargeHistory() {
        if (mPresenter == null) {
            return;
        }
        mRechargeDetailAdapter.addToList(mPresenter.getSuccessfulRechargeDetailList());
        mRechargeDetailAdapter.notifyDataSetChanged();
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
    public void customizeSelectedOperator(TextView selectedText) {
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
                mPresenter.validateRechargeData(mobile, amount, mOperatorName);
                break;

            case R.id.contacts:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                startActivityForResult(intent, Constants.CONTACTS_REQUEST_CODE);
                break;
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TextView selectedText = (TextView) parent.getChildAt(0);
        if (selectedText != null) {
            customizeSelectedOperator(selectedText);
        }

        mOperatorName = (String) parent.getItemAtPosition(position);
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

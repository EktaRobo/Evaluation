package com.example.ekta.evaluation.ui.rechargescreen;

import android.content.ContentResolver;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ekta.evaluation.R;
import com.example.ekta.evaluation.adapters.RechargeDetailAdapter;
import com.example.ekta.evaluation.constants.Constants;
import com.example.ekta.evaluation.data.DataSource;
import com.example.ekta.evaluation.models.RechargeDetails;
import com.example.ekta.evaluation.utilities.RechargeUtils;

/**
 * Created by ekta on 17/4/17.
 */

public class RechargePresenter implements RechargeContract.Presenter, AdapterView
        .OnItemSelectedListener{
    private static final String TAG = RechargePresenter.class.getSimpleName();
    private RechargeContract.View mView;
    private DataSource mDataRepository;
    private RechargeDetailAdapter mRechargeDetailAdapter;
    private String mOperatorName;

    public RechargePresenter(RechargeContract.View view, DataSource dataRepository) {
        mView = view;
        mDataRepository = dataRepository;
    }


    @Override
    public void start() {
        if (mView != null) {
            mView.initUI();
        }
    }

    @Override
    public void setEditTextRestrictions(AppCompatEditText mobileEditText, AppCompatEditText
            amountEditText) {
        mobileEditText.setKeyListener(new DigitsKeyListener());
        amountEditText.setKeyListener(new DigitsKeyListener());
        mobileEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(
                Constants.MAX_LENGTH_MOBILE)});
    }

    public void setRecyclerViewAdapter(RecyclerView recyclerView, RecyclerView.LayoutManager
            linearLayoutManager) {
        if (mDataRepository == null) {
            return;
        }
        mRechargeDetailAdapter = new RechargeDetailAdapter(mDataRepository.getSuccessfulRechargeDetailList());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mRechargeDetailAdapter);
    }

    @Override
    public void addTextChangeListener(AppCompatEditText mobileEditText, final AppCompatSpinner operatorSpinner) {
        mobileEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.e(TAG, "beforeTextChanged: s: " + s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e(TAG, "onTextChanged: s: " + s);
                if (s.length() >= Constants.FOUR) {
                    String currentNumber = s.toString();
                    String firstFourDigits = currentNumber.substring(Constants.BEGIN_INDEX,
                            Constants.END_INDEX);
                    Log.e(TAG, "onTextChanged: firstFourDigits: " + firstFourDigits);
                    operatorSpinner.setSelection(RechargeUtils.getOperatorIndex
                            (firstFourDigits));
                } else {
                    operatorSpinner.setSelection(Constants.AIRTEL_CODE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e(TAG, "afterTextChanged: s: " + s);

            }
        });
    }

    @Override
    public void setAdapterForSpinners(AppCompatSpinner operatorNameSpinner,
                                      ArrayAdapter<CharSequence> adapter) {

        operatorNameSpinner.setOnItemSelectedListener(this);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the mOperatorNameSpinner
        operatorNameSpinner.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TextView selectedText = (TextView) parent.getChildAt(0);
        if (selectedText != null) {
            if (mView == null) {
                return;
            }
            mView.setSelectedOperator(selectedText);
        }

        mOperatorName = (String) parent.getItemAtPosition(position);
    }



    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void refreshRechargeHistory() {
        if (mDataRepository == null) {
            return;
        }
        mRechargeDetailAdapter.addToList(mDataRepository.getSuccessfulRechargeDetailList());
        mRechargeDetailAdapter.notifyDataSetChanged();
    }

    @Override
    public void setSelectedContactNumber(Intent data, ContentResolver contentProvider) {
        String phoneNo = mDataRepository.getMobileNumber(data, contentProvider);
        mView.setMobileNumber(phoneNo);
    }

    @Override
    public void validateRechargeData(String mobile, String amount) {
        if (mOperatorName.equals("") || mOperatorName == null) {
            mView.displayToast(R.string.operator_name_error);
        } else {
            if (mobile.equals(Constants.EMPTY) || mobile.length() < Constants
                    .MAX_LENGTH_MOBILE) {
                mView.displayToast(R.string.invalid_mobile);
            } else {
                if (amount.equals(Constants.EMPTY)) {
                    mView.displayToast(R.string.amount_error);
                } else {

                    mView.navigateToCardDetailScreen(fetchRechargeDetails(mobile, amount));
                }
            }
        }
    }

    @Override
    @NonNull
    public RechargeDetails fetchRechargeDetails(String mobile, String amount) {
        RechargeDetails rechargeDetails = new RechargeDetails();
        rechargeDetails.setOperatorName(mOperatorName);
        rechargeDetails.setMobileNumber(mobile);
        rechargeDetails.setAmount(amount);
        return rechargeDetails;
    }
}

package com.example.ekta.evaluation.ui.rechargescreen;

import android.content.ContentResolver;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.ekta.evaluation.R;
import com.example.ekta.evaluation.constants.Constants;
import com.example.ekta.evaluation.data.DataSource;
import com.example.ekta.evaluation.models.RechargeDetails;
import com.example.ekta.evaluation.utilities.RechargeUtils;

import java.util.ArrayList;

/**
 * Created by ekta on 17/4/17.
 */

public class RechargePresenter implements RechargeContract.Presenter {
    private static final String TAG = RechargePresenter.class.getSimpleName();
    private RechargeContract.View mView;
    private DataSource mDataRepository;

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
    public ArrayList<RechargeDetails> getSuccessfulRechargeDetailList() {
        if (mDataRepository == null) {
            return null;
        }
        return mDataRepository.getSuccessfulRechargeDetailList();
    }

    @Override
    public void changeOperatorName(CharSequence charSequence) {
        if (mView == null) {
            return;
        }
        if (charSequence.length() >= Constants.FOUR) {
            String currentNumber = charSequence.toString();
            String firstFourDigits = currentNumber.substring(Constants.BEGIN_INDEX,
                    Constants.END_INDEX);
            Log.e(TAG, "onTextChanged: firstFourDigits: " + firstFourDigits);
            mView.setSelectedOperator(RechargeUtils.getOperatorIndex
                    (firstFourDigits));
        } else {
            mView.setSelectedOperator(Constants.AIRTEL_CODE);
        }
    }

    @Override
    public void setSelectedContactNumber(Intent data, ContentResolver contentProvider) {
        String phoneNo = mDataRepository.getMobileNumber(data, contentProvider);
        mView.setMobileNumber(phoneNo);
    }

    @Override
    public void validateRechargeData(String mobile, String amount, String operatorName) {
        if (operatorName == null || operatorName.equals("")) {
            mView.displayToast(R.string.operator_name_error);
        } else {
            if (mobile.equals(Constants.EMPTY) || mobile.length() < Constants
                    .MAX_LENGTH_MOBILE) {
                mView.displayToast(R.string.invalid_mobile);
            } else {
                if (amount.equals(Constants.EMPTY)) {
                    mView.displayToast(R.string.amount_error);
                } else {

                    mView.navigateToCardDetailScreen(fetchRechargeDetails(mobile, amount,
                            operatorName));
                }
            }
        }
    }

    @Override
    @NonNull
    public RechargeDetails fetchRechargeDetails(String mobile, String amount, String operatorName) {
        RechargeDetails rechargeDetails = new RechargeDetails();
        rechargeDetails.setOperatorName(operatorName);
        rechargeDetails.setMobileNumber(mobile);
        rechargeDetails.setAmount(amount);
        return rechargeDetails;
    }
}

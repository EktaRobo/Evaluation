package com.example.ekta.evaluation.ui.rechargescreen;

import android.content.ContentResolver;
import android.content.Intent;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ekta.evaluation.models.RechargeDetails;

/**
 * Created by ekta on 17/4/17.
 */

public interface RechargeContract {

    interface View {
        void initUI();

        void setSelectedOperator(TextView selectedText);

        void setMobileNumber(String mobileNumber);

        void displayToast(String message);

        void displayToast(int stringId);

        void navigateToCardDetailScreen(RechargeDetails rechargeDetails);

    }

    interface Presenter {
        void start();

        void setEditTextRestrictions(AppCompatEditText mobileEditText, AppCompatEditText
                amountEditText);

        void addTextChangeListener(AppCompatEditText mobileEditText, AppCompatSpinner
                operatorSpinner);

        void setAdapterForSpinners(AppCompatSpinner operatorNameSpinner, ArrayAdapter<CharSequence> adapter);

        void setRecyclerViewAdapter(RecyclerView recyclerView, RecyclerView.LayoutManager
                linearLayoutManager);

        void refreshRechargeHistory();

        void setSelectedContactNumber(Intent data, ContentResolver contentResolver);

        void validateRechargeData(String mobile, String amount);

        RechargeDetails fetchRechargeDetails(String mobile, String amount);
    }
}

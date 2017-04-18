package com.example.ekta.evaluation.ui.rechargescreen;

import android.content.ContentResolver;
import android.content.Intent;
import android.widget.TextView;

import com.example.ekta.evaluation.models.RechargeDetails;

import java.util.ArrayList;

/**
 * Created by ekta on 17/4/17.
 */

public interface RechargeContract {

    interface View {
        void initUI();

        void setSelectedOperator(int operatorCode);

        void customizeSelectedOperator(TextView selectedText);

        void setMobileNumber(String mobileNumber);

        void displayToast(String message);

        void displayToast(int stringId);

        void navigateToCardDetailScreen(RechargeDetails rechargeDetails);

    }

    interface Presenter {
        void start();

        ArrayList<RechargeDetails> getSuccessfulRechargeDetailList();

        void changeOperatorName(CharSequence charSequence);

        void setSelectedContactNumber(Intent data, ContentResolver contentResolver);

        void validateRechargeData(String mobile, String amount, String operatorName);

        RechargeDetails fetchRechargeDetails(String mobile, String amount, String operatorName);
    }
}

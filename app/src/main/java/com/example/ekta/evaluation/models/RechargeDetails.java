package com.example.ekta.evaluation.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ekta on 4/4/17.
 */

public class RechargeDetails implements Parcelable {

    private String mOperatorName;
    private String mMobileNumber;
    private String mAmount;

    public RechargeDetails() {
    }

    protected RechargeDetails(Parcel in) {
        mOperatorName = in.readString();
        mMobileNumber = in.readString();
        mAmount = in.readString();
    }

    public static final Creator<RechargeDetails> CREATOR = new Creator<RechargeDetails>() {
        @Override
        public RechargeDetails createFromParcel(Parcel in) {
            return new RechargeDetails(in);
        }

        @Override
        public RechargeDetails[] newArray(int size) {
            return new RechargeDetails[size];
        }
    };

    public String getOperatorName() {
        return mOperatorName;
    }

    public void setOperatorName(String operatorName) {
        mOperatorName = operatorName;
    }

    public String getMobileNumber() {
        return mMobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        mMobileNumber = mobileNumber;
    }

    public String getAmount() {
        return mAmount;
    }

    public void setAmount(String amount) {
        mAmount = amount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mOperatorName);
        dest.writeString(mMobileNumber);
        dest.writeString(mAmount);
    }
}

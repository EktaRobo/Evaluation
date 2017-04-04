package com.example.ekta.evaluation.data.database.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by ekta on 4/4/17.
 */

public class SavedCard extends RealmObject {
    @Required
    @PrimaryKey
    private String mMaskedCardNumber;
    private String mTokenId;

    public String getMaskedCardNumber() {
        return mMaskedCardNumber;
    }

    public void setMaskedCardNumber(String maskedCardNumber) {
        mMaskedCardNumber = maskedCardNumber;
    }

    public String getTokenId() {
        return mTokenId;
    }

    public void setTokenId(String tokenId) {
        mTokenId = tokenId;
    }
}

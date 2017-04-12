package com.example.ekta.evaluation.data.database;

import com.example.ekta.evaluation.data.database.models.SavedCard;
import com.example.ekta.evaluation.models.RechargeDetails;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by ekta on 4/4/17.
 */

public class DatabaseHelper {

    public static void addSavedCardToDatabase(final SavedCard savedCard) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(savedCard);
            }
        });
    }

    public static ArrayList<SavedCard> getAllSavedCards() {
        Realm realm = Realm.getDefaultInstance();
        return new ArrayList<>(realm.where(SavedCard.class).findAll());

    }

    public static void addPaymentSuccessDetailsToDatabase(final RechargeDetails rechargeDetails) {
        Realm realm = Realm.getDefaultInstance();
        rechargeDetails.setId(getNextKey(realm));
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(rechargeDetails);
            }
        });
    }

    private static int getNextKey(Realm realm)
    {
        try {
            return realm.where(RechargeDetails.class).max("mId").intValue() + 1;
        } catch (ArrayIndexOutOfBoundsException e) {
            return 0;
        } catch (NullPointerException e) {
            return 0;
        }
    }

    public static ArrayList<RechargeDetails> getRechargeHistory() {
        Realm realm = Realm.getDefaultInstance();

        RealmResults<RechargeDetails> result = realm.where(RechargeDetails.class).findAll();
        result = result.sort("mId", Sort.DESCENDING);
        return new ArrayList<>(result);

    }
}

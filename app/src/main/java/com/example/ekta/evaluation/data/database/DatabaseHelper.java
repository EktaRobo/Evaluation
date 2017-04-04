package com.example.ekta.evaluation.data.database;

import com.example.ekta.evaluation.data.database.models.SavedCard;

import java.util.ArrayList;

import io.realm.Realm;

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
        ArrayList<SavedCard> savedCards = new ArrayList<>(realm.where(SavedCard.class).findAll());
        if (savedCards == null) {
            return null;
        }
        return savedCards;

    }
}

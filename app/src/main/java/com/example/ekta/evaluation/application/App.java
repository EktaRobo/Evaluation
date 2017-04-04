package com.example.ekta.evaluation.application;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by ekta on 4/4/17.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}

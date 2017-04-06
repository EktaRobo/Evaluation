package com.example.ekta.evaluation.application;

import android.app.Application;

import com.example.ekta.evaluation.data.DataRepository;
import com.example.ekta.evaluation.data.DataSource;

import io.realm.Realm;

/**
 * Created by ekta on 4/4/17.
 */

public class App extends Application {
    public static DataSource sDataRepository;

    public static DataSource getDataRepository() {
        return sDataRepository;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        sDataRepository = new DataRepository();
    }
}

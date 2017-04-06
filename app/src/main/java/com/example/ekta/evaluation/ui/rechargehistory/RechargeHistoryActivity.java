package com.example.ekta.evaluation.ui.rechargehistory;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.ekta.evaluation.R;
import com.example.ekta.evaluation.adapters.RechargeDetailAdapter;
import com.example.ekta.evaluation.application.App;

public class RechargeHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_history);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recharge_history_list);
        RechargeDetailAdapter rechargeDetailAdapter = new RechargeDetailAdapter(App
                .getDataRepository().getSuccessfulRechargeDetailList());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(rechargeDetailAdapter);
    }
}

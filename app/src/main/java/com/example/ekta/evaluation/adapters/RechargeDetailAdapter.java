package com.example.ekta.evaluation.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ekta.evaluation.R;
import com.example.ekta.evaluation.constants.Constants;
import com.example.ekta.evaluation.models.RechargeDetails;

import java.util.ArrayList;

/**
 * Created by ekta on 6/4/17.
 */

public class RechargeDetailAdapter extends RecyclerView.Adapter<RechargeDetailAdapter.ViewHolder> {
    private ArrayList<RechargeDetails> mRechargeDetails;

    public RechargeDetailAdapter(ArrayList<RechargeDetails> rechargeDetails) {
        mRechargeDetails = rechargeDetails;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.recharge_history_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RechargeDetails rechargeDetail = mRechargeDetails.get(position);
        if (rechargeDetail != null) {
            holder.mOperatorName.setText(rechargeDetail.getOperatorName());
            holder.mMobileNumber.setText(rechargeDetail.getMobileNumber());
            String amount = Constants.RS + rechargeDetail.getAmount();
            holder.mAmount.setText(amount);
        }

    }

    public void addToList(ArrayList<RechargeDetails> rechargeDetails) {
        mRechargeDetails = rechargeDetails;
    }

    @Override
    public int getItemCount() {
        return mRechargeDetails.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mOperatorName;
        private TextView mMobileNumber;
        private TextView mAmount;

        public ViewHolder(View itemView) {
            super(itemView);
            mOperatorName = (TextView) itemView.findViewById(R.id.operator_name);
            mMobileNumber = (TextView) itemView.findViewById(R.id.contact_number);
            mAmount = (TextView) itemView.findViewById(R.id.amount);
        }
    }
}

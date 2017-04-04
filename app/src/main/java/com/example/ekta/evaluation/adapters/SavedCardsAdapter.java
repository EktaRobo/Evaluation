package com.example.ekta.evaluation.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ekta.evaluation.R;
import com.example.ekta.evaluation.constants.Constants;
import com.example.ekta.evaluation.listeners.OnSavedCardClickedListener;
import com.example.ekta.evaluation.data.database.models.SavedCard;

import java.util.ArrayList;

/**
 * Created by ekta on 4/4/17.
 */

public class SavedCardsAdapter extends RecyclerView.Adapter<SavedCardsAdapter.SavedCardsViewHolder> {


    private ArrayList<SavedCard> mSavedCards;
    private OnSavedCardClickedListener mSavedCardClickedListener;

    public SavedCardsAdapter(ArrayList<SavedCard> savedCards, OnSavedCardClickedListener
            savedCardClickedListener) {
        mSavedCards = savedCards;
        mSavedCardClickedListener = savedCardClickedListener;
    }

    @Override
    public SavedCardsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new SavedCardsViewHolder(inflater.inflate(R.layout.saved_card_item, parent, false));
    }

    @Override
    public void onBindViewHolder(SavedCardsViewHolder holder, int position) {
        SavedCard savedCard = mSavedCards.get(position);
        String maskedCardNumber = Constants.MASK + savedCard.getMaskedCardNumber();
        holder.mMaskedCardNumber.setText(maskedCardNumber);

    }

    @Override
    public int getItemCount() {
        int size = 0;
        if (mSavedCards != null) {
            size = mSavedCards.size();
        }
        return size;
    }

    public class SavedCardsViewHolder extends RecyclerView.ViewHolder {
        private TextView mMaskedCardNumber;

        public SavedCardsViewHolder(View itemView) {
            super(itemView);
            mMaskedCardNumber = (TextView) itemView.findViewById(R.id.masked_card_number);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSavedCardClickedListener.onSavedCardClicked(mSavedCards.get(getAdapterPosition
                            ()));
                }
            });
        }
    }
}

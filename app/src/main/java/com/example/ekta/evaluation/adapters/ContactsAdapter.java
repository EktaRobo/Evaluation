package com.example.ekta.evaluation.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ekta.evaluation.R;
import com.example.ekta.evaluation.models.Contact;

import java.util.ArrayList;


/**
 *
 */
public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> {

    private ArrayList<Contact> mContacts;


    public ContactsAdapter(ArrayList<Contact> contacts) {
        mContacts = contacts;

    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int pos) {

        View listItemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_item, parent, false);

        return new ContactViewHolder(listItemView);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int pos) {
        // Extract info from cursor
        Contact contact = mContacts.get(pos);
        contactViewHolder.mContactName.setText(contact.getContactName());
        contactViewHolder.mContactNumber.setText(contact.getContactNumber());

        // Create contact model and bind to viewholder
       /* Contact c = new Contact();
        c.name = contactName;
        c.profilePic = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);

        contactViewHolder.bind(c);*/

    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        private TextView mContactName;
        private TextView mContactNumber;

        public ContactViewHolder(View itemView) {
            super(itemView);
            mContactName = (TextView) itemView.findViewById(R.id.contact_name);
            mContactNumber = (TextView) itemView.findViewById(R.id.contact_number);
        }
    }
}
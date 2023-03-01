package com.example.cryptochat.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cryptochat.R;
import com.example.cryptochat.activity.ChatActivity;
import com.example.cryptochat.databinding.ContactBinding;
import com.example.cryptochat.pojo.Contact;
import com.example.cryptochat.utils.CryptoChatConstants;

import java.util.ArrayList;
import java.util.List;


public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ContactHolder> {
    private List<Contact> contactList;

    public ContactListAdapter() {
    }

    public class ContactHolder extends RecyclerView.ViewHolder {
        private @NonNull ContactBinding binding;

        public ContactHolder(View view) {
            super(view);
            binding = ContactBinding.bind(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Intent intent = new Intent(view.getContext(), ChatActivity.class);
                    intent.putExtra(CryptoChatConstants.CONTACT, contactList.get(position));
                    view.getContext().startActivity(intent);
                }
            });
        }

        public void bind(Contact contact) {
            binding.name.setText(contact.getName());
            binding.contactNumber.setText(contact.getNumber());
        }
    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact, parent, false);
        return new ContactHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactHolder holder, int position) {
        holder.bind(contactList.get(position));
    }

    @Override
    public int getItemCount() {
        return contactList != null ? contactList.size() : 0;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void printContact(Contact contact) {
        if (contactList == null) {
            contactList = new ArrayList<>();
        }
        contactList.add(contact);
        notifyDataSetChanged();
    }
}

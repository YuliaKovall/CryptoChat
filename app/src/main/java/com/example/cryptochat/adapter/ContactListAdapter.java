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
import com.example.cryptochat.activity.ContactListActivity;
import com.example.cryptochat.activity.MainActivity;
import com.example.cryptochat.databinding.ContactItemBinding;
import com.example.cryptochat.databinding.UserItemBinding;
import com.example.cryptochat.pojo.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

import java.util.Date;
import java.util.List;


public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ContactHolder> {
    private List<User> userArrayList = new ArrayList<>();

    public ContactListAdapter() {
    }

    public ContactListAdapter(List<User> userArrayList) {
        this.userArrayList = userArrayList;
    }

    public class ContactHolder extends RecyclerView.ViewHolder {
        private @NonNull ContactItemBinding binding;

        public ContactHolder(View view) {
            super(view);
            binding = ContactItemBinding.bind(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), ChatActivity.class);
                    view.getContext().startActivity(intent);
                }
            });
        }

        public void bind(User user) {
            binding.name.setText(user.getUserName());
            binding.contactNumber.setText(user.getUserNumber());
        }
    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);
        return new ContactHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactHolder holder, int position) {
        holder.bind(userArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addUser(User user) {
        userArrayList.add(user);
        notifyDataSetChanged();
    }
}

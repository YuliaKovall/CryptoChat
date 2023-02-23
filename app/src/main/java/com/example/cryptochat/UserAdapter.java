package com.example.cryptochat;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cryptochat.databinding.UserItemBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {
    private List<User> userArrayList = new ArrayList<>();

    public UserAdapter() {
    }

    public UserAdapter(List<User> userArrayList) {

        this.userArrayList = userArrayList;
    }

    public class UserHolder extends RecyclerView.ViewHolder {
        private UserItemBinding binding;

        public UserHolder(View view) {
            super(view);
            binding = UserItemBinding.bind(view);
        }

        public void bind(User user) {
            binding.name.setText(user.getUserName());
            binding.message.setText(user.getMessage());
            binding.count.setText(String.valueOf(user.getCount()));
            binding.time.setText(String.valueOf(user.getTime()));
        }

    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UserHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        Collections.sort(userArrayList, Comparator.comparing(User::getTime));
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

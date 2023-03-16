package com.example.cryptochat.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cryptochat.R;
import com.example.cryptochat.activity.ChatActivity;
import com.example.cryptochat.controller.FileController;
import com.example.cryptochat.databinding.ChatItemBinding;
import com.example.cryptochat.pojo.ChatItem;
import com.example.cryptochat.utils.CryptoChatConstants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatItemHolder> {
    private List<ChatItem> chatItemList;

    public ChatListAdapter() {
    }

    public class ChatItemHolder extends RecyclerView.ViewHolder {
        private ChatItemBinding binding;

        public ChatItemHolder(View view) {
            super(view);
            binding = ChatItemBinding.bind(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int positionIndex = getAdapterPosition();
                    ChatItem selectedChatItem = chatItemList.get(positionIndex);
                    Intent intent = new Intent(view.getContext(), ChatActivity.class);
                    intent.putExtra(CryptoChatConstants.CONTACT, selectedChatItem.getContact());
                    view.getContext().startActivity(intent);
                }
            });
        }

        public void bind(ChatItem chatItem) {
            binding.name.setText(chatItem.getContactName());
            binding.message.setText(chatItem.getMessage());
            binding.count.setText(String.valueOf(chatItem.getNumberUnreadMessages()));
            binding.time.setText(String.valueOf(formatTimeDate(chatItem.getTime())));
        }
    }

    public String formatTimeDate(Date d) {
        String result = "";
        Date now = new Date();
        if (now.getTime() - d.getTime() < 86400000 && now.getDay() == d.getDay()) {
            result = new SimpleDateFormat("HH:mm:ss").format(d);
        } else if (now.getTime() - d.getTime() < 172800000 && now.getDay() != d.getDay()) {
            result = "учора " + new SimpleDateFormat("HH:mm").format(d);
        } else if (now.getTime() - d.getTime() < 604800000 && now.getDay() != d.getDay()) {
            result = new SimpleDateFormat("E HH:mm").format(d);
        } else {
            result = new SimpleDateFormat("dd.MM.yyyy").format(d);
        }
        return result;
    }

    @NonNull
    @Override
    public ChatItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
        return new ChatItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatItemHolder holder, int position) {
        Collections.sort(chatItemList, (chatItem1, chatItem2) -> chatItem2.getTime().compareTo(chatItem1.getTime()));
        holder.bind(chatItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return chatItemList == null ? 0 : chatItemList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addChatItem(ChatItem chatItem) {
        if (chatItemList == null) {
            chatItemList = new ArrayList<>();
        }
        chatItemList.add(chatItem);
        notifyDataSetChanged();
    }

    public void removeItem(Context context, int position) {
        FileController.removeContactKeyMap(context, chatItemList.remove(position).getContactNumber());
        notifyItemRemoved(position);
    }
}

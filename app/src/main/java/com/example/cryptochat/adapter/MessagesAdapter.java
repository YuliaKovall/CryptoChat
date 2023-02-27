package com.example.cryptochat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cryptochat.R;
import com.example.cryptochat.pojo.Message;

import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter {

    Context context;
    List<Message> messagesList;

    int ITEM_SEND=1;
    int ITEM_RECIEVE=2;

    public MessagesAdapter(Context context, List<Message> messagesList) {
        this.context = context;
        this.messagesList = messagesList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==ITEM_SEND) {
            View view= LayoutInflater.from(context).inflate(R.layout.sender_chat_layout,parent,false);
            return new SenderViewHolder(view);
        } else {
            View view= LayoutInflater.from(context).inflate(R.layout.receiver_chat_layout,parent,false);
            return new RecieverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message messages=messagesList.get(position);
        if(holder.getClass()==SenderViewHolder.class)
        {
            SenderViewHolder viewHolder=(SenderViewHolder)holder;
            viewHolder.textViewMessaage.setText(messages.getMessage());
            viewHolder.timeOfMessage.setText(messages.getFormattedTime());
        }
        else
        {
            RecieverViewHolder viewHolder=(RecieverViewHolder)holder;
            viewHolder.textViewMessaage.setText(messages.getMessage());
            viewHolder.timeOfMessage.setText(messages.getFormattedTime());
        }

    }


    @Override
    public int getItemViewType(int position) {
        Message message =messagesList.get(position);
        if(message.isSentByCurrentUser()) {
            return  ITEM_SEND;
        } else {
            return ITEM_RECIEVE;
        }
    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }


    class SenderViewHolder extends RecyclerView.ViewHolder
    {

        TextView textViewMessaage;
        TextView timeOfMessage;


        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMessaage=itemView.findViewById(R.id.sendermessage);
            timeOfMessage=itemView.findViewById(R.id.timeofmessage);
        }
    }

    class RecieverViewHolder extends RecyclerView.ViewHolder
    {

        TextView textViewMessaage;
        TextView timeOfMessage;


        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMessaage=itemView.findViewById(R.id.sendermessage);
            timeOfMessage=itemView.findViewById(R.id.timeofmessage);
        }
    }

}

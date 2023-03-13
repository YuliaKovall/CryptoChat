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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter {

    Context context;
    List<Message> messagesList;
    String key;

    int ITEM_SEND=1;
    int ITEM_RECIEVE=2;
    SimpleDateFormat todayFormat = new SimpleDateFormat("HH:mm");
    SimpleDateFormat weekFormat = new SimpleDateFormat("E HH:mm");
    SimpleDateFormat yearFormat = new SimpleDateFormat("dd.MM.yyyy");

    public MessagesAdapter(Context context, List<Message> messagesList, String key) {
        this.context = context;
        this.messagesList = messagesList;
        this.key = key;
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

    public String formatTimeDate(Date d) {
        String result = "";
        Date now = new Date();
        if (now.getTime() - d.getTime() < 86400000 && now.getDay() == d.getDay()) {
            result = todayFormat.format(d);
        } else if (now.getTime() - d.getTime() < 172800000 && now.getDay() != d.getDay()) {
            result = "учора " + todayFormat.format(d);
        } else if (now.getTime() - d.getTime() < 604800000 && now.getDay() != d.getDay()) {
            result = weekFormat.format(d);
        } else {
            result = yearFormat.format(d);
        }
        return result;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messagesList.get(position);
        if(holder.getClass()==SenderViewHolder.class) {
            SenderViewHolder viewHolder=(SenderViewHolder)holder;
            viewHolder.textViewMessaage.setText(message.getMessage());
            viewHolder.timeOfMessage.setText(formatTimeDate(message.getTime()));
            viewHolder.textViewMessaage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Sending message click handling
                     message.setMessage("Decrypted by " + key);
                     notifyDataSetChanged();
                }
            });
        } else {
            RecieverViewHolder viewHolder=(RecieverViewHolder)holder;
            viewHolder.textViewMessaage.setText(message.getMessage());
            viewHolder.timeOfMessage.setText(formatTimeDate(message.getTime()));
            viewHolder.textViewMessaage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Receiving message click handling
                    message.setMessage("Decrypted by " + key);
                    notifyDataSetChanged();
                }
            });
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


    class SenderViewHolder extends RecyclerView.ViewHolder {

        TextView textViewMessaage;
        TextView timeOfMessage;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMessaage=itemView.findViewById(R.id.sendermessage);
            timeOfMessage=itemView.findViewById(R.id.timeofmessage);
        }
    }

    class RecieverViewHolder extends RecyclerView.ViewHolder {

        TextView textViewMessaage;
        TextView timeOfMessage;

        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMessaage=itemView.findViewById(R.id.sendermessage);
            timeOfMessage=itemView.findViewById(R.id.timeofmessage);
        }
    }

}

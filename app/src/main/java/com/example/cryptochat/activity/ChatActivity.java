package com.example.cryptochat.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cryptochat.R;
import com.example.cryptochat.adapter.MessagesAdapter;
import com.example.cryptochat.pojo.Message;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {
    ImageButton backButton, sendButton;
    TextView contactName, contactNumber, backWord, readyNote1, readyNote2;
    String contactNumberStr, sendingMessage;
    EditText messageBox;
    List<Message> messageList;
    RecyclerView messagesRecyclerView;
    MessagesAdapter messagesAdapter;
    Date date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        contactName = findViewById(R.id.contactName);
        contactNumber = findViewById(R.id.contactNumber);
        backWord = findViewById(R.id.chat_back_word);
        backButton = findViewById(R.id.chat_back_button);
        sendButton = findViewById(R.id.sendButton);
        messageBox = findViewById(R.id.messageBox);
        readyNote1 = findViewById(R.id.note_you_can_start_chatting);
        readyNote2 = findViewById(R.id.note_you_can_start_chatting2);
        readyNote2 = findViewById(R.id.note_you_can_start_chatting3);

        Intent intent = getIntent();
        contactName.setText(intent.getStringExtra("USER_NAME"));
        contactNumber.setText(intent.getStringExtra("USER_NUMBER"));
        contactNumberStr = contactNumber.getText().toString();
        backWord.setOnClickListener(this);
        backButton.setOnClickListener(this);
        sendButton.setOnClickListener(this);

        // Recycler view handling
        messageList = new ArrayList<>();
        messagesRecyclerView = findViewById(R.id.recyclerView_chat_log);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        messagesRecyclerView.setLayoutManager(linearLayoutManager);
        messagesAdapter = new MessagesAdapter(ChatActivity.this, messageList);
        messagesRecyclerView.setAdapter(messagesAdapter);

        //receiveMessages
        date = new Date();
        messageList.add(new Message("Привіт", true, date.getTime()));
        messageList.add(new Message("Вітаю!", false, date.getTime()));
        messageList.add(new Message("Як твої справи?", false, date.getTime()));
        messageList.add(new Message("Збираю тобі на дрон", true, date.getTime()));
        messageList.add(new Message("Чекаємо!!!", false, date.getTime()));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chat_back_word:
            case R.id.chat_back_button:
                finish();
                break;
            case R.id.sendButton:
                sendMessage();
                break;
        }
    }

    public void sendMessage() {
        sendingMessage = messageBox.getText().toString();
        date = new Date();
        messageList.add(new Message(sendingMessage, true, date.getTime()));
        messagesAdapter.notifyItemInserted(messageList.size() - 1);
        messageBox.setText(null);
        // Making empty message box
        if (messageList.size() > 0) {
            readyNote1.setVisibility(View.GONE);
            readyNote2.setVisibility(View.GONE);
        }
    }


    public void receiveMessage(String message) {
        date = new Date();
        messageList.add(new Message(message, false, date.getTime()));
        messagesAdapter.notifyItemInserted(messageList.size() - 1);
    }

    @Override
    public void onStart() {
        super.onStart();
        messagesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (messagesAdapter != null) {
            messagesAdapter.notifyDataSetChanged();
        }
    }


}

package com.example.cryptochat.activity;

import android.content.DialogInterface;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cryptochat.R;
import com.example.cryptochat.adapter.MessagesAdapter;
import com.example.cryptochat.pojo.Contact;
import com.example.cryptochat.pojo.Message;
import com.example.cryptochat.utils.CryptoChatConstants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {
    ImageButton backButton, sendButton;
    TextView contactName, contactNumber, backWord;
    String contactNumberStr, sendingMessage;
    EditText messageBox;
    List<Message> messageList;
    RecyclerView messagesRecyclerView;
    MessagesAdapter messagesAdapter;

    SmsManager smsManager;
    IntentFilter intentFilter;
    BroadcastReceiver intentReceiver;
    LinearLayout uniqueKeyOkNote;


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
        uniqueKeyOkNote = findViewById(R.id.unique_key_is_ok);


        Intent intent = getIntent();
        Contact contact = (Contact) intent.getSerializableExtra(CryptoChatConstants.CONTACT);
        contactName.setText(contact.getName());
        contactNumber.setText(contact.getNumber());
        contactNumberStr = contactNumber.getText().toString();
        backWord.setOnClickListener(this);
        backButton.setOnClickListener(this);
        sendButton.setOnClickListener(this);

        //TODO
        messageList = new ArrayList<>();
        hideReadyNote();

        // Recycler view handling
        messagesRecyclerView = findViewById(R.id.recyclerView_chat_log);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        messagesRecyclerView.setLayoutManager(linearLayoutManager);
        messagesAdapter = new MessagesAdapter(ChatActivity.this, messageList);
        messagesRecyclerView.setAdapter(messagesAdapter);


        //Receiving and sending messages handling
        smsManager = SmsManager.getDefault();
        intentFilter = new IntentFilter();
        intentFilter.addAction("SMS_RECEIVED_ACTION");
        intentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(getApplicationContext(), "Message Get!", Toast.LENGTH_LONG).show();
                messageList.add(new Message((intent.getExtras().getString("message")), false, new Date()));
                messagesAdapter.notifyItemInserted(messageList.size() - 1);
                hideReadyNote();
            }
        };

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
   //     try {
   //         smsManager.sendTextMessage(contactNumberStr, null, sendingMessage, null, null);//
        messageList.add(new Message(sendingMessage, true, new Date()));
        messagesAdapter.notifyItemInserted(messageList.size() - 1);
        messageBox.setText(null);
        hideReadyNote();
  //          Toast.makeText(getApplicationContext(), "Message Sent!", Toast.LENGTH_LONG).show();
  //       } catch (Exception e) {
 //           Toast.makeText(getApplicationContext(), "Message failed to send.", Toast.LENGTH_LONG).show();
 //           e.printStackTrace();
 //       }
    }

    public void hideReadyNote() {
        if (messageList.size() > 0) {
            uniqueKeyOkNote.setVisibility(View.GONE);
        }
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

    @Override
    protected void onResume() {
        //register the receiver
        registerReceiver(intentReceiver, intentFilter);
        super.onResume();
    }

    @Override
    protected void onPause() {
        //unregister the receiver
        unregisterReceiver(intentReceiver);
        super.onPause();
    }


}

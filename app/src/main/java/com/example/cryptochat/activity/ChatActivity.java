package com.example.cryptochat.activity;

import static android.content.ContentValues.TAG;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.MergeCursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cryptochat.R;
import com.example.cryptochat.adapter.MessagesAdapter;
import com.example.cryptochat.controller.FileController;
import com.example.cryptochat.pojo.Contact;
import com.example.cryptochat.pojo.Message;
import com.example.cryptochat.utils.CryptoChatConstants;
import com.example.cryptochat.utils.EasyEncryption;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {
    ImageButton backButton, sendButton, encryptButton;
    Drawable encryptButtonBackground;
    TextView contactName, contactNumber, backWord;
    String contactNumberStr, sendingMessage, contactNameStr, uniquePassword;
    EditText messageBox;
    List<Message> messageList;
    RecyclerView messagesRecyclerView;
    MessagesAdapter messagesAdapter;
    SmsManager smsManager;
    IntentFilter intentFilter;
    BroadcastReceiver intentReceiver;
    LinearLayout uniqueKeyOkNote;
    EasyEncryption easyEncryption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Initialize UI components
        contactName = findViewById(R.id.contactName);
        contactNumber = findViewById(R.id.contactNumber);
        backWord = findViewById(R.id.chat_back_word);
        backButton = findViewById(R.id.chat_back_button);
        encryptButton = findViewById(R.id.encryptButton);
        sendButton = findViewById(R.id.sendButton);
        messageBox = findViewById(R.id.messageBox);
        uniqueKeyOkNote = findViewById(R.id.unique_key_is_ok);

        // Set contact's data
        Intent intent = getIntent();
        Contact contact = (Contact) intent.getSerializableExtra(CryptoChatConstants.CONTACT);
        contactName.setText(contact.getName());
        contactNumber.setText(contact.getNumber());
        contactNumberStr = contactNumber.getText().toString();
        contactNameStr = contact.getName();
        Log.d(TAG, "Contact Number: " + contactNumberStr);
        uniquePassword = "key";


        // Set click listeners
        backWord.setOnClickListener(this);
        backButton.setOnClickListener(this);
        encryptButton.setOnClickListener(this);
        sendButton.setOnClickListener(this);
        messageList = new ArrayList<>();

        //Calling PopUp
        if (!FileController.openContactKeyMap(this).containsKey(contactNumberStr)) {
            PopUpFragment popUpFragment = new PopUpFragment(this, contactNumberStr, contactNameStr, true, null);
            popUpFragment.setOnDismissListener(dialogInterface -> {
                uniquePassword = (Objects.requireNonNull(FileController.openContactKeyMap(getBaseContext()).get(contactNumberStr))).get(0);
                easyEncryption = new EasyEncryption(Objects.requireNonNull(uniquePassword));
                messagesAdapter.setKey(uniquePassword);
            });
            popUpFragment.show();
        }else{
            uniquePassword = (Objects.requireNonNull(FileController.openContactKeyMap(getBaseContext()).get(contactNumberStr))).get(0);
            easyEncryption = new EasyEncryption(Objects.requireNonNull(uniquePassword));
        }





        // Hide ready note when tap on message box
        messageBox.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    uniqueKeyOkNote.setVisibility(View.GONE);
                }
            }
        });

        // Change the encryption button background color when the message box is typed in
        encryptButtonBackground = encryptButton.getBackground();
        messageBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    encryptButtonBackground.setColorFilter(-15132391, PorterDuff.Mode.SRC_IN);
                    encryptButton.setBackground(encryptButtonBackground);
                } else {
                    encryptButtonBackground.setColorFilter(-10066330, PorterDuff.Mode.SRC_IN);
                    encryptButton.setBackground(encryptButtonBackground);
                    encryptButtonUp();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Set up the recycler view for displaying messages
        messagesRecyclerView = findViewById(R.id.recyclerView_chat_log);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        messagesRecyclerView.setLayoutManager(linearLayoutManager);
        messagesAdapter = new MessagesAdapter(ChatActivity.this, messageList, uniquePassword);
        messagesRecyclerView.setAdapter(messagesAdapter);

        // Load all previous messages from the contact into the messageList
        loadMessagesFromContact();
        hideReadyNoteIfNeeded();

        // Set up a BroadcastReceiver for receiving new messages
        smsManager = SmsManager.getDefault();
        intentFilter = new IntentFilter();
        intentFilter.addAction("SMS_RECEIVED_ACTION");
        intentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getExtras().getString("number").equals(contactNumberStr)) {
                    messageList.add(new Message((intent.getExtras().getString("message")), false, new Date()));
                    messagesAdapter.notifyItemInserted(messageList.size() - 1);
                    messagesRecyclerView.scrollToPosition(messageList.size() - 1);
                }
            }
        };
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chat_back_word:
            case R.id.chat_back_button:
                Intent intent = new Intent(this, MainActivity.class);
                view.getContext().startActivity(intent);
                break;
            case R.id.encryptButton:
                String messageBoxText = messageBox.getText().toString();
                if (!messageBoxText.equals("")) {
                    // Here should be called method to encrypt massage
                    messageBoxText = easyEncryption.encrypt(messageBoxText);
                    messageBox.setText(messageBoxText);
                    sendButtonUp();
                }
                break;
            case R.id.sendButton:
                sendMessage();
                encryptButtonUp();
                break;
        }
    }

    // Uncomment the code for sending SMS through a mobile operator
    public void sendMessage() {
        sendingMessage = messageBox.getText().toString();
        ArrayList<String> messageParts = smsManager.divideMessage(sendingMessage);
        int messageCount = messageParts.size();
        ArrayList<PendingIntent> sentIntents = new ArrayList<>(messageCount);
        ArrayList<PendingIntent> deliveredIntents = new ArrayList<>(messageCount);

        for (int i = 0; i < messageCount; i++) {
            Intent sentIntent = new Intent("SMS_SENT");
            Intent deliveredIntent = new Intent("SMS_DELIVERED");
            sentIntents.add(PendingIntent.getBroadcast(this, 0, sentIntent, 0));
            deliveredIntents.add(PendingIntent.getBroadcast(this, 0, deliveredIntent, 0));
        }

        try {
            smsManager.sendMultipartTextMessage(
                    contactNumberStr,
                    null,
                    messageParts,
                    sentIntents,
                    deliveredIntents
            );

        messageList.add(new Message(sendingMessage, true, new Date()));
        messagesAdapter.notifyItemInserted(messageList.size() - 1);
        messagesRecyclerView.scrollToPosition(messageList.size() - 1);
        messageBox.setText(null);
        Toast.makeText(getApplicationContext(), "Повідомлення надіслано!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Не вдалося надіслати повідомлення. Будь ласка, спробуйте пізніше.", Toast.LENGTH_LONG).show();
        }

    }

    private void loadMessagesFromContact() {
        Uri inboxUri = Uri.parse("content://sms/");
        String[] projection = new String[]{"_id", "address", "body", "date", "type"};
        Cursor cursor = getContentResolver().query(inboxUri, projection, "address=?", new String[]{contactNumberStr}, "date ASC");

        if (cursor != null) {
            int addressIndex = cursor.getColumnIndex("address");
            int bodyIndex = cursor.getColumnIndex("body");
            int typeIndex = cursor.getColumnIndex("type");
            int dateIndex = cursor.getColumnIndex("date");
            while (cursor.moveToNext()) {
                String messageBody = cursor.getString(bodyIndex);
                boolean isSentByUser = cursor.getInt(typeIndex) == 2;
                long timestamp = cursor.getLong(dateIndex);
                String address = cursor.getString(addressIndex);
                if (address.equals(contactNumberStr)) {
                    messageList.add(new Message(messageBody, isSentByUser, new Date(timestamp)));
                }
            }
            cursor.close();
        }

        messagesAdapter.notifyDataSetChanged();
    }

    public void hideReadyNoteIfNeeded() {
        if (messageList.size() > 0) {
            uniqueKeyOkNote.setVisibility(View.GONE);
        }
    }

    public void encryptButtonUp() {
        encryptButton.setElevation(1);
        sendButton.setElevation(0);
    }

    public void sendButtonUp() {
        encryptButton.setElevation(0);
        sendButton.setElevation(1);
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

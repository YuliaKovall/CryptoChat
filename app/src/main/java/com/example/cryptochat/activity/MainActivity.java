package com.example.cryptochat.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Telephony;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.cryptochat.adapter.ChatListAdapter;
import com.example.cryptochat.controller.FileController;
import com.example.cryptochat.databinding.ActivityMainBinding;
import com.example.cryptochat.pojo.ChatItem;
import com.example.cryptochat.pojo.Contact;
import com.example.cryptochat.pojo.Message;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ChatListAdapter adapter = new ChatListAdapter();
    private List<ChatItem> chatItemList;
    private BroadcastReceiver intentReceiver;
    private IntentFilter intentFilter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        grantPermissions();
        init();
        printChatItems();
    }

    public void createNewChat(View view) {
        Intent intent = new Intent(getApplicationContext(), ContactListActivity.class);
        startActivity(intent);
    }

    private void printChatItems() {
        for (ChatItem chatItem: chatItemList) {
            adapter.addChatItem(chatItem);
        }
    }

    private void init() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.recyclerView.setAdapter(adapter);
        chatItemList = formChatItemList();
        // Set up a BroadcastReceiver for receiving new messages
        intentFilter = new IntentFilter();
        intentFilter.addAction("SMS_RECEIVED_ACTION");
        intentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String number = intent.getExtras().getString("number");
                String message = intent.getExtras().getString("message");
                for (ChatItem item : chatItemList) {
                    if (item.getContactNumber().equals(number)) {
                        item.setMessage(message);
                        item.setTime(new Date());
                        item.setNumberUnreadMessages(item.getNumberUnreadMessages() + 1);
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }
            }
        };
    }

    private List<ChatItem> formChatItemList() {
        Date time;
        Cursor cursor;
        Contact contact;
        String[] projection, selectionArgs;
        String number, selection, sortOrder, message, password;
        List<ChatItem> result = new ArrayList<>();
        Map<String, List<String>> contactKeyMap = FileController.openContactKeyMap(this);
        ContentResolver contentResolver = getContentResolver();

        for (String key : contactKeyMap.keySet()) {
            number = key;
            projection = new String[]{"_id", "address", "date", "body", "type", "read"};
            selection = "address=? AND date=(SELECT MAX(date) FROM sms WHERE address=?)";
            selectionArgs = new String[]{number, number};
            sortOrder = "date DESC";
            cursor = contentResolver.query(Uri.parse("content://sms/"), projection, selection, selectionArgs, sortOrder);

            // Form item if chat history already exist
            if (cursor != null && cursor.moveToFirst()) {
                message = "";
                int bodyIndex = cursor.getColumnIndex("body");
                if (bodyIndex >= 0) {
                    message = cursor.getString(bodyIndex);
                }
                long timeInMillis = 0;
                int dateIndex = cursor.getColumnIndex("date");
                if (dateIndex >= 0) {
                    timeInMillis = cursor.getLong(dateIndex);
                }
                time = new Date(timeInMillis);
                int typeIndex = cursor.getColumnIndex("type");
                int numberUnreadMessages = 0;
                if (typeIndex >= 0 && cursor.getInt(typeIndex) == Telephony.Sms.MESSAGE_TYPE_INBOX) {
                    int readIndex = cursor.getColumnIndex("read");
                    if (readIndex >= 0 && cursor.getInt(readIndex) == 0) {
                        numberUnreadMessages = 1;
                    }
                }
                password = contactKeyMap.get(key).get(0);
                contact = new Contact(contactKeyMap.get(key).get(1), key);
                result.add(new ChatItem(contact, password, message, time, numberUnreadMessages));
            } else {
                // Form item if there is no chat history
                password = contactKeyMap.get(key).get(0);
                contact = new Contact(contactKeyMap.get(key).get(1), key);
                try {
                    time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(contactKeyMap.get(key).get(2));
                }catch (ParseException e) {time = new Date();}
                result.add(new ChatItem(contact, password, "Немає повідомлень", time, 0));
            }
        }
        return result;
    }



    protected void grantPermissions() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS}, 34);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS}, 34);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 34 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
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
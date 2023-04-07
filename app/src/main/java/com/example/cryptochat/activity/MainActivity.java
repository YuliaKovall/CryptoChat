package com.example.cryptochat.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Telephony;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cryptochat.R;
import com.example.cryptochat.adapter.ChatListAdapter;
import com.example.cryptochat.controller.FileController;
import com.example.cryptochat.databinding.ActivityMainBinding;
import com.example.cryptochat.pojo.ChatItem;
import com.example.cryptochat.pojo.Contact;
import com.example.cryptochat.pojo.Message;
import com.example.cryptochat.utils.SwipeToDeleteCallback;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ChatListAdapter adapter;
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

        ImageView settingsBackButton = findViewById(R.id.right_button_background);
        settingsBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSettings();
            }
        });
        enableSwipeToDelete();
    }

    private void enableSwipeToDelete() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                int position = viewHolder.getAdapterPosition();
                adapter.removeItem(MainActivity.this, position);
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(binding.recyclerView);
    }

    public void createNewChat(View view) {
        Intent intent = new Intent(getApplicationContext(), ContactListActivity.class);
        startActivity(intent);
    }

    private void printChatItems() {
        for (ChatItem chatItem : chatItemList) {
            adapter.addChatItem(chatItem);
        }
    }

    private void init() {
        adapter = new ChatListAdapter();
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        chatItemList = formChatItemList();
        LinearLayout emptyChatListNote = findViewById(R.id.welcome_screen_main_text);
        if (formChatItemList().size() > 0) {
            emptyChatListNote.setVisibility(View.GONE);
        }
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
            selection = "address=?";
            selectionArgs = new String[]{number};
            sortOrder = "date DESC";
            try {
                cursor = contentResolver.query(Uri.parse("content://sms/"), projection, selection, selectionArgs, sortOrder);

                // Form item if chat history already exist
                if (cursor.moveToFirst()) {
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
                    cursor.close();
                } else {
                    // Form item if there is no chat history
                    password = contactKeyMap.get(key).get(0);
                    contact = new Contact(contactKeyMap.get(key).get(1), key);
                    time = new Date(0);
                    result.add(new ChatItem(contact, password, "Немає повідомлень", time, 0));
                }
            } catch (SecurityException e) {
                e.printStackTrace();
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

    public void goToSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

}
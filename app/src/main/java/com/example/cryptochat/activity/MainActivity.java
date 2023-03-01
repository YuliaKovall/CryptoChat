package com.example.cryptochat.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.cryptochat.adapter.ChatListAdapter;
import com.example.cryptochat.databinding.ActivityMainBinding;
import com.example.cryptochat.pojo.ChatItem;
import com.example.cryptochat.pojo.Contact;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ChatListAdapter adapter = new ChatListAdapter();
    private Map<Integer, ChatItem> chatItemMap = new HashMap<>();


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
        for (Map.Entry<Integer, ChatItem> entry : chatItemMap.entrySet()) {
            adapter.addChatItem(entry.getValue());
        }
    }

    private void init() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.recyclerView.setAdapter(adapter);
        setChatItemInfo();
    }

    private void setChatItemInfo() {
        chatItemMap.put(1, new ChatItem(new Contact("1", "Jhon", "+38067382333"), "Ok!", new Date(1677229871000L), 2));
        chatItemMap.put(2, new ChatItem(new Contact("2", "Bili", "+38067382335"), "Hi!", new Date(1677362266064L), 3));
        chatItemMap.put(3, new ChatItem(new Contact("3", "Kolia", "+38067356334"), "How are you?", new Date(1677362166064L), 1));
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

}
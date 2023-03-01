package com.example.cryptochat.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;

import com.example.cryptochat.R;

import com.example.cryptochat.adapter.ContactListAdapter;
import com.example.cryptochat.databinding.ActivityContactListBinding;
import com.example.cryptochat.databinding.ActivityMainBinding;
import com.example.cryptochat.pojo.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ContactListActivity extends AppCompatActivity {
    private ActivityContactListBinding binding;
    private ContactListAdapter adapter = new ContactListAdapter();
    private Map<Integer, User> userMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContactListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        printUsers();
    }

    public void chatMassage(View view) {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    private void printUsers() {
        for (Map.Entry<Integer, User> entry : userMap.entrySet()) {
            adapter.addUser(entry.getValue());
        }
    }

    private void init() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.recyclerView.setAdapter(adapter);
        //setUserInfo();
        setUserMapFromContacts();
    }

    private void setUserMapFromContacts() {
        Map<String, String> contacts = getContactMap(this);
        int counter = 1;
        for (Map.Entry<String, String> entry : contacts.entrySet()) {
            userMap.put(counter, new User(entry.getKey(), entry.getValue(), entry.getValue(), new Date(), counter));
            counter++;
        }
    }

    @SuppressLint("Range")
    private Map<String, String> getContactMap(Context context) {
        Map<String, String> map = new HashMap<>();
        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));
                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        map.put(name, phoneNo);
                    }
                    pCur.close();
                }
            }
        }
        if (cur != null) {
            cur.close();
        }
        return map;
    }
}
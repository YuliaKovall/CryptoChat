package com.example.cryptochat.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.cryptochat.adapter.UserAdapter;
import com.example.cryptochat.databinding.ActivityMainBinding;
import com.example.cryptochat.pojo.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private UserAdapter adapter = new UserAdapter();

    private Map<Integer, User> userMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        printUsers();
    }

    private void printUsers() {
        for (Map.Entry<Integer, User> entry : userMap.entrySet()) {
            adapter.addUser(entry.getValue());
        }
    }

    private void init() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.recyclerView.setAdapter(adapter);
        setUserInfo();
    }

    private void setUserInfo() {
        userMap.put(1, new User("Jhon", "+38067382333", "Ok!", new Date(1677229871000L), 2));
        userMap.put(2, new User("Bili", "+38067382335", "Hi!", new Date(1677362266064L), 3));
        userMap.put(3, new User("Kolia", "+38067356334", "How are you?", new Date(1677362166064L), 1));
        userMap.put(4, new User("Lara", "+38067382344", "Hello!", new Date(1677361466064L), 4));
        userMap.put(5, new User("Jhon", "+38067384", "Ok!", new Date(1677102466064L), 2));
        userMap.put(6, new User("Bili", "+38067342334", "Hi!", new Date(1677062466064L), 3));
        userMap.put(7, new User("Kolia","+380652334",  "How are you?", new Date(1677363997487L), 1));
        userMap.put(8, new User("Lara", "+38066382334", "Hello!", new Date(1670362466064L), 4));
        userMap.put(9, new User("Jhon", "+38067382333", "Ok!", new Date(1607362466064L), 2));
        userMap.put(10, new User("Bili", "+38067382332", "Hi!", new Date(1677316271000L), 3));
        userMap.put(11, new User("Kolia","+38067382331",  "How are you?", new Date(1677143471000L), 1));
        userMap.put(12, new User("Lara", "+38067382330", "Hello!", new Date(1677365435819L), 4));
    }
}
package com.example.cryptochat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.cryptochat.databinding.ActivityMainBinding;

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
        userMap.put(1, new User("Jhon", "Ok!", "13:22", 2));
        userMap.put(2, new User("Bili", "Hi!", "15:00", 3));
        userMap.put(3, new User("Kolia", "How are you?", "18:00", 1));
        userMap.put(4, new User("Lara", "Hello!", "19:00", 4));
        userMap.put(5, new User("Jhon", "Ok!", "13:22", 2));
        userMap.put(6, new User("Bili", "Hi!", "15:00", 3));
        userMap.put(7, new User("Kolia", "How are you?", "18:00", 1));
        userMap.put(8, new User("Lara", "Hello!", "19:00", 4));
        userMap.put(9, new User("Jhon", "Ok!", "13:22", 2));
        userMap.put(10, new User("Bili", "Hi!", "15:00", 3));
        userMap.put(11, new User("Kolia", "How are you?", "18:00", 1));
        userMap.put(12, new User("Lara", "Hello!", "19:00", 4));
    }
}
package com.example.cryptochat.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.cryptochat.R;
import com.example.cryptochat.controller.FileController;

public class SettingsActivity extends AppCompatActivity {
    private ImageView chatListBackButton;
    Switch switchThemes;
    SharedPreferences sharedPreferences = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        chatListBackButton = findViewById(R.id.left_button_background);
        chatListBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToChatsList();
            }
        });

        switchThemes = findViewById(R.id.switch_theme);
        switchThemes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!switchThemes.isChecked()) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });

    }



    public void goToChatsList() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void deleteAllChats(View view){
        FileController.deleteAllChats(view.getContext());
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(intent);
    }
}

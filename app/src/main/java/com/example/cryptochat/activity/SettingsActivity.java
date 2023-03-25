package com.example.cryptochat.activity;

import android.annotation.SuppressLint;
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

import java.io.FileNotFoundException;

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

        sharedPreferences = getSharedPreferences("color_theme", 0);;
        Boolean booleanValue = sharedPreferences.getBoolean("night_mode",true);

        if (booleanValue){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            switchThemes.setChecked(true);
        }

        switchThemes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!switchThemes.isChecked()) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    switchThemes.setChecked(false);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("night_mode",false);
                    editor.apply();
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    switchThemes.setChecked(true);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("night_mode",true);
                    editor.apply();
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

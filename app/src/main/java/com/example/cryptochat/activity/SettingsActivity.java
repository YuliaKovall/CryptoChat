package com.example.cryptochat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cryptochat.R;

public class SettingsActivity extends AppCompatActivity {
    private ImageView chatListBackButton;
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
    }

    public void goToChatsList() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

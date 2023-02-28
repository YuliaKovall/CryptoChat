package com.example.cryptochat.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.example.cryptochat.R;


public class PopUpFragment extends Dialog {
    private final Context mContext;
    private String pin;
    private Drawable buttonDrawable;


    protected PopUpFragment(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext = context;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        TextView numberOfCharacters;
        ConstraintLayout constraintLayout;
        int myRedColor;
        int myBlackColor;
        int myGrey75;
        EditText editText;
        Button button;
        ImageButton imageButton;
        ImageView trailingIcon;
        ImageView caret;
        TextView textViewUnderEditText;
        //
        setContentView(R.layout.pop_up_elements);
        getWindow().setBackgroundDrawableResource(R.drawable.pop_up_background);
        //
        constraintLayout = findViewById(R.id.constraintLayoutPopUp);
        numberOfCharacters = findViewById(R.id.textView5);
        button = findViewById(R.id.button2);
        imageButton = findViewById(R.id.imageButton);
        editText = findViewById(R.id.editText);
        trailingIcon = findViewById(R.id.trailingIcon);
        caret = findViewById(R.id.caret);
        //
        myRedColor = mContext.getResources().getColor(R.color.universal_state_error);
        myBlackColor = mContext.getResources().getColor(R.color.black);
        myGrey75 = mContext.getResources().getColor(R.color.universal_text_grey_75);
        editText.setTextColor(myBlackColor);
        editText.setBackgroundResource(R.drawable.pop_up_edit_text);
        textViewUnderEditText = findViewById(R.id.textView4);
        //
        View.OnClickListener errorOnClick = view -> {
            if (pin.length() < 16) {
                editText.setBackgroundResource(R.drawable.pop_up_edit_text_error);
                textViewUnderEditText.setText(R.string.notEnoughCharsError);
                textViewUnderEditText.setTextColor(myRedColor);
                caret.setVisibility(View.VISIBLE);
                trailingIcon.setVisibility(View.VISIBLE);
            }
        };
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pin = editText.getText().toString().trim();
                numberOfCharacters.setText("* Введено символів" + " " + pin.length());
                imageButton.setVisibility(View.VISIBLE);
                imageButton.setOnClickListener(view -> editText.setText(""));
                caret.setVisibility(View.INVISIBLE);
                trailingIcon.setVisibility(View.INVISIBLE);
                textViewUnderEditText.setText("* Щонайменше 16 символів");
                editText.setBackgroundResource(R.drawable.pop_up_edit_text);

                if (pin.length() >= 16) {
                    buttonDrawable = ContextCompat.getDrawable(button.getContext(), R.drawable.pop_up_button);
                    button.setBackground(buttonDrawable);
                    button.setOnClickListener(view -> dismiss());
                    textViewUnderEditText.setTextColor(myGrey75);

                } else {
                    buttonDrawable = ContextCompat.getDrawable(button.getContext(), R.drawable.pop_up_button_disabled);
                    button.setBackground(buttonDrawable);
                    textViewUnderEditText.setTextColor(myGrey75);
                    button.setOnClickListener(errorOnClick);
                    constraintLayout.setOnClickListener(errorOnClick);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }
}
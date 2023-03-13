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
import com.example.cryptochat.controller.FileController;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PopUpFragment extends Dialog {

    private final Context mContext;
    private final String contactNumber;
    private final String contactName;
    private String pin;

    private TextView numberOfCharacters;
    private ConstraintLayout constraintLayout;
    private int myRedColor;
    private int myGrey75;
    private EditText editText;
    private Button button;
    private ImageButton imageButton;
    private ImageView trailingIcon;
    private ImageView caret;
    private TextView textViewUnderEditText;
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            View.OnClickListener errorOnClick = view -> {
                if (pin.length() < 16) {
                    editText.setBackgroundResource(R.drawable.pop_up_edit_text_error);
                    textViewUnderEditText.setText(R.string.notEnoughCharsError);
                    textViewUnderEditText.setTextColor(myRedColor);
                    caret.setVisibility(View.VISIBLE);
                    trailingIcon.setVisibility(View.VISIBLE);
                }
            };
            pin = editText.getText().toString().trim();
            numberOfCharacters.setText("* Введено символів " + pin.length());
            imageButton.setVisibility(View.VISIBLE);
            imageButton.setOnClickListener(view -> editText.setText(""));
            caret.setVisibility(View.INVISIBLE);
            trailingIcon.setVisibility(View.INVISIBLE);
            textViewUnderEditText.setText(R.string.hintForTheUserAboutTheKey2);
            editText.setBackgroundResource(R.drawable.pop_up_edit_text);

            Drawable buttonDrawable;
            if (pin.length() >= 16) {
                buttonDrawable = ContextCompat.getDrawable(button.getContext(), R.drawable.pop_up_button);
                button.setBackground(buttonDrawable);
                button.setOnClickListener(view -> {
                    FileController.addAndEditContactKeyMap(getContext(), contactNumber, pin, contactName,
                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    dismiss();
                });

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
    };

    public PopUpFragment(@NonNull Context context, String contactNumber, String contactName, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.contactNumber = contactNumber;
        this.contactName = contactName;
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_up_elements);
        getWindow().setBackgroundDrawableResource(R.drawable.pop_up_background);

        constraintLayout = findViewById(R.id.constraintLayoutPopUp);
        numberOfCharacters = findViewById(R.id.textView5);
        button = findViewById(R.id.button2);
        imageButton = findViewById(R.id.imageButton);
        editText = findViewById(R.id.editText);
        trailingIcon = findViewById(R.id.trailingIcon);
        caret = findViewById(R.id.caret);

        myRedColor = mContext.getResources().getColor(R.color.universal_state_error);
        int myBlackColor = mContext.getResources().getColor(R.color.black);
        myGrey75 = mContext.getResources().getColor(R.color.universal_text_grey_75);
        editText.setTextColor(myBlackColor);
        editText.setBackgroundResource(R.drawable.pop_up_edit_text);
        textViewUnderEditText = findViewById(R.id.textView4);

        editText.addTextChangedListener(textWatcher);

    }
}
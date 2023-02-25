package com.example.cruptoch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.view.View;
import java.util.ArrayList;

public class ActivityAddChat extends AppCompatActivity {

    private static final int REQUEST_CODE_READ_CONTACTS=1;
    private static boolean READ_CONTACTS_GRANTED =false;
    ArrayList<String> contacts = new ArrayList<>();
    Button addBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_chat);



        addBtn = findViewById(R.id.addBtn);
        // отримуємо дозвіл
        int hasReadContactPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        // якщо пристрій до API 23, встановлюємо дозвіл
        if(hasReadContactPermission == PackageManager.PERMISSION_GRANTED){
            READ_CONTACTS_GRANTED = true;
        }
        else{
            // викликаемо діалоговое вікно для встановлення дозволів
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS}, REQUEST_CODE_READ_CONTACTS);
        }
        // якщо дозвіл встановлено, загружаемо контакти
        if (READ_CONTACTS_GRANTED){
            loadContacts();
        }

        addBtn.setEnabled(READ_CONTACTS_GRANTED);
    }
    public void buttonGoBack(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                READ_CONTACTS_GRANTED = true;
            }
            addBtn.setEnabled(READ_CONTACTS_GRANTED);
        }
        if(READ_CONTACTS_GRANTED){
            loadContacts();
        }
        else{
            Toast.makeText(this, "Треба встановити дозвіл", Toast.LENGTH_LONG).show();
        }
    }
    public void onAddContact(View v) {
        ContentValues contactValues = new ContentValues();
        EditText contactText = findViewById(R.id.newContact);
        String newContact = contactText.getText().toString();
        contactText.setText("");
        contactValues.put(ContactsContract.RawContacts.ACCOUNT_NAME, newContact);
        contactValues.put(ContactsContract.RawContacts.ACCOUNT_TYPE, newContact);
        Uri newUri = getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, contactValues);
        long rawContactsId = ContentUris.parseId(newUri);
        contactValues.clear();
        contactValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactsId);
        contactValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        contactValues.put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, newContact);
        getContentResolver().insert(ContactsContract.Data.CONTENT_URI, contactValues);
        Toast.makeText(getApplicationContext(), newContact + " додано в список контактів", Toast.LENGTH_LONG).show();
        loadContacts();
    }
    private void loadContacts(){
        contacts.clear();
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if(cursor!=null){
            while (cursor.moveToNext()) {

                // отримуємо кожен контакт
                //ТУТ ПОМИЛКА, ТРЕБА ВИРІШИТИ

                String s1= ContactsContract.Contacts.DISPLAY_NAME_PRIMARY;
                int index = cursor.getColumnIndex(s1);
                String contact = cursor.getString(index);

                // додаємо контакт в список
                contacts.add(contact);
            }
            cursor.close();
        }
        // створюємо адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, contacts);
        // встановлюємо для списка адаптер
        ListView contactList = findViewById(R.id.contactList);
        contactList.setAdapter(adapter);

    }
}
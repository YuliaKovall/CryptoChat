package com.example.cryptochat.controller;

import static com.example.cryptochat.utils.CryptoChatConstants.CHAT_ITEM_FILE_NAME;
import static com.example.cryptochat.utils.CryptoChatConstants.CONTACT_KEY_FILE_NAME;
import static com.example.cryptochat.utils.CryptoChatConstants.SETTING;

import android.content.Context;
import android.widget.Toast;

import com.example.cryptochat.pojo.ChatItem;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileController {
    private static Object openFile(Context context, String fileName) {
        Object output = null;
        try {
            InputStream inStream = context.openFileInput(fileName);
            if (inStream != null) {
                ObjectInputStream reader = new ObjectInputStream(inStream);
                output = reader.readObject();
                inStream.close();
            }
        }
        catch (Throwable t) { }
        return output;
    }

    private static void saveFile(Context context, String fileName, Object data) {
        try {
            FileOutputStream outStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream writer = new ObjectOutputStream(outStream);
            writer.reset();
            writer.writeObject(data);
            writer.close();
        }
        catch (Throwable t) { }
    }

    private static boolean deleteFile(Context context, String fileName) {
        return context.deleteFile(fileName);
    }

    public static void deleteAllChats(Context context){
        deleteFile(context,CONTACT_KEY_FILE_NAME);
    }

    public static void addAndEditContactKeyMap (Context context, String key, String pin,
                                                String nameContact, String creatingDate){
        Map<String, List<String>> m = new HashMap<>();
        List<String> l = new ArrayList<>();
        l.add(pin);
        l.add(nameContact);
        l.add(creatingDate);
        if (openFile(context, CONTACT_KEY_FILE_NAME) != null) {
            m = (HashMap<String, List<String>>) openFile(context, CONTACT_KEY_FILE_NAME);
        }
        m.put(key, l);
        saveFile(context, CONTACT_KEY_FILE_NAME, m);
    }


    public static Map<String, List<String>> openContactKeyMap (Context context){
        Map<String, List<String>> m = new HashMap<>();
        if (openFile(context, CONTACT_KEY_FILE_NAME) != null) {
            m = (HashMap<String, List<String>>) openFile(context, CONTACT_KEY_FILE_NAME);
        }
        return m;
    }

    public static void removeContactKeyMap (Context context, String key){
        Map<String, String> m = new HashMap<>();
        if (openFile(context, CONTACT_KEY_FILE_NAME) != null) {
            m = (HashMap<String, String>) openFile(context, CONTACT_KEY_FILE_NAME);
            m.remove(key);
        }
        saveFile(context, CONTACT_KEY_FILE_NAME, m);
    }

    public static void editChatItem (Context context, Map<Integer, ChatItem> chatItemMap) {
        saveFile(context, CHAT_ITEM_FILE_NAME, chatItemMap);
    }

    public static Map<Integer, ChatItem> openChatItem (Context context){
        Map<Integer, ChatItem> m = new HashMap<>();
        if (openFile(context, CHAT_ITEM_FILE_NAME) != null) {
            m = (HashMap<Integer, ChatItem>) openFile(context, CHAT_ITEM_FILE_NAME);
        }
        return m;
    }
    
    public static void settingFileSave(Context context, String setting, String value){
        Map<String, String> s = new HashMap<>();
        if (openFile(context, SETTING) != null){
            s = (HashMap<String, String>) openFile(context, SETTING);
        }
        s.put(setting, value);
        saveFile(context, SETTING, s);
    }
    
    public static String settingFileOpen(Context context, String setting){
        String value = "0";
        try {
            if (openFile(context, SETTING) != null) {
                value = ((HashMap<String, String>) openFile(context, SETTING)).get(setting);
            }
        } catch (Exception e){
            return value;
        }
        return value;
    }

}

package com.example.cryptochat.controller;

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


    public static void addAndEditContactKeyMap (Context context, String key, String pin,
                                                String nameContact, String creatingDate){
        Map<String, List<String>> m = new HashMap<>();
        String fileName = "contactkeymap";
        List<String> l = new ArrayList<>();
        l.add(pin);
        l.add(nameContact);
        l.add(creatingDate);
        if (openFile(context, fileName) != null) {
            m = (HashMap<String, List<String>>) openFile(context, fileName);
        }
        m.put(key, l);
        saveFile(context, fileName, m);
    }


    public static Map<String, List<String>> openContactKeyMap (Context context){
        Map<String, List<String>> m = new HashMap<>();
        String fileName = "contactkeymap";
        if (openFile(context, fileName) != null) {
            m = (HashMap<String, List<String>>) openFile(context, fileName);
        }
        return m;
    }

    public static void removeContactKeyMap (Context context, String key){
        Map<String, String> m = new HashMap<>();
        String fileName = "contactkeymap";
        if (openFile(context, fileName) != null) {
            m = (HashMap<String, String>) openFile(context, fileName);
            m.remove(key);
        }
        saveFile(context, fileName, m);
    }

    public static void editChatItem (Context context, Map<Integer, ChatItem> chatItemMap) {
        String fileName = "chatitemmap";
        saveFile(context, fileName, chatItemMap);
    }

    public static Map<Integer, ChatItem> openChatItem (Context context){
        Map<Integer, ChatItem> m = new HashMap<>();
        String fileName = "chatitemmap";
        if (openFile(context, fileName) != null) {
            m = (HashMap<Integer, ChatItem>) openFile(context, fileName);
        }
        return m;
    }

}

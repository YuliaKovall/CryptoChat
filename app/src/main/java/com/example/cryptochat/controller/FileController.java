package com.example.cryptochat.controller;

import android.content.Context;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class FileController {
    public static Object openFile(Context context, String fileName) {
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

    public static void saveFile(Context context, String fileName, Object data) {
        try {
            FileOutputStream outStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream writer = new ObjectOutputStream(outStream);
            writer.reset();
            writer.writeObject(data);
            writer.close();
        }
        catch (Throwable t) { }
    }


    public static void addAndEditContactKeyMap (Context context, String key, String value){
        Map<String, String> m = new HashMap<>();
        String fileName = "contactkeymap";

        if (openFile(context, fileName) != null) {
            m = (HashMap<String, String>) openFile(context, fileName);
        }
        m.put(key, value);
        saveFile(context, fileName, m);
    }


    public static Map<String, String> openContactKeyMap (Context context){
        Map<String, String> m = new HashMap<>();
        String fileName = "contactkeymap";
        if (openFile(context, fileName) != null) {
            m = (HashMap<String, String>) openFile(context, fileName);
        }
        return m;
    }


    public static void removeContactKeyMap (Context context, String fileName, String key){
        Map<String, String> m = new HashMap<>();
        if (openFile(context, fileName) != null) {
            m = (HashMap<String, String>) openFile(context, fileName);
            m.remove(key);
        }
        saveFile(context, fileName, m);
    }
}

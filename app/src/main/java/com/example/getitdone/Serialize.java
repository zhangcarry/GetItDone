package com.example.getitdone;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * For saving and loading to-dos.
 * @author Zijing Que (u6469732)
 * @author Chan Tun Aung (u6777573)
 */
public class Serialize {

    public static void saveTodos(List<Todo> todos, Context appContext) {
        HelperMethods helpers = new HelperMethods();
        String filename = helpers.getDataFile(appContext);
        // If the file doesn't exists yet, then create it
        File file = new File(helpers.getDataFile(appContext));
        if (!file.exists()) {
            Log.d("Serialize", file.getName() + " doesn't exist yet");
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            FileOutputStream outputStream = appContext.openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(outputStream);
            oos.writeObject(todos);
            oos.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Todo> loadTodos(Context appContext) {
        HelperMethods helpers = new HelperMethods();
        String filename = helpers.getDataFile(appContext);
        // If the file doesn't exists yet, then create it
        File file = new File(helpers.getDataFile(appContext));
        if (!file.exists()) {
            Log.d("Serialize", file.getName() + " doesn't exist yet");
            try {
                file.createNewFile();
                saveTodos(new ArrayList<Todo>(), appContext);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        List<Todo> todos = null;
        try {
            FileInputStream fin = appContext.openFileInput(filename);
            ObjectInputStream ois = new ObjectInputStream(fin);

            todos = (List<Todo>) ois.readObject();

            ois.close();
            fin.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return todos;
    }
}

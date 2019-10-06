package com.example.getitdone;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * For saving and loading to-dos.
 * @author Zijing Que (u6469732)
 * @author Chan Tun Aung (u6777573)
 */
public class Serialize {

    public static void saveTodos(String filename, List<Todo> todos, Context appContext) {
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

    public static List<Todo> loadTodos(String filename, Context appContext) {
        List<Todo> todos = null;
        try {
            FileInputStream fin = appContext.openFileInput(filename);
            ObjectInputStream ois = new ObjectInputStream(fin);

            todos = (List<Todo>) ois.readObject();

            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return todos;
    }
}

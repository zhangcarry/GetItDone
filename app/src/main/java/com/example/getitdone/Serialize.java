package com.example.getitdone;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

    /**
     * Saving the to-do list to the data file.
     * @param todos list of to-dos
     * @param appContext
     */
    public static void saveTodos(List<Todo> todos, Context appContext) {
        HelperMethods helpers = new HelperMethods();
        File dataFile = helpers.getDataFile(appContext);

        try {
            FileOutputStream outputStream = appContext.openFileOutput(dataFile.getName(), Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(outputStream);
            oos.writeObject(todos);
            oos.close();
            outputStream.close();
        } catch (FileNotFoundException e) {
            Log.d("GetItDone Log", "file not found");
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loading the to-do list from the data file.
     * @param appContext
     * @return list of to-dos
     */
    public static List<Todo> loadTodos(Context appContext) {
        HelperMethods helpers = new HelperMethods();
        File dataFile = helpers.getDataFile(appContext);
        List<Todo> todos = null;

        try {
            FileInputStream fin = appContext.openFileInput(dataFile.getName());
            ObjectInputStream ois = new ObjectInputStream(fin);

            todos = (List<Todo>) ois.readObject();

            ois.close();
            fin.close();
        } catch (FileNotFoundException e) {
            Log.d("GetItDone Log", "file not found");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (todos == null) return new ArrayList<>();
        return todos;
    }
}

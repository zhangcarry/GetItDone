package com.example.getitdone;
import android.content.Context;
import android.util.Log;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Group of helper methods to keep things like the data file name consistent.
 * @author Chan Tun Aung
 * @author Zijing Que
 * @author Carry Zhang
 */
public class HelperMethods {

    /**
     * Return filtered list of to-dos.
     * @param filter
     * @param context
     * @return filtered list of to-dos
     */
    public List<Todo> getTodoList(Filter filter, Context context) {
        List<Todo> todos = Serialize.loadTodos(context);
        List<Todo> filteredTodo = new ArrayList<>();

        switch (filter) {
            // for displaying only the completed to-dos
            case Completed:
                for (Todo t : todos) {
                    if (t.isCompleted()) filteredTodo.add(t);
                }
                break;
            // for displaying only the remaining to-dos
            case Uncompleted:
                for (Todo t : todos) {
                    if (!t.isCompleted()) filteredTodo.add(t);
                }
                break;
            default:
                filteredTodo = todos;
                break;
        }

        return filteredTodo;
    }

    /**
     * Returns the data file where the serialized list of to-dos are stored.
     * Please use this method to get the name of the data file instead of directly referencing
     * the path to the data file.
     * @param context
     * @return to-do data file name
     */
    public File getDataFile(Context context) {
        String filename = context.getResources().getString(R.string.todos_data_file);
        File privateFolder = context.getFilesDir();
        File dataFile = new File(privateFolder, filename);
        try {
            if (dataFile.createNewFile()) {
                createEmptyDataFile(dataFile, context);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataFile;
    }

    /**
     * Create empty data file if it hasn't been created yet.
     * @param dataFile
     * @param context
     */
    private void createEmptyDataFile(File dataFile, Context context) {
        try {
            FileOutputStream outputStream = context.openFileOutput(dataFile.getName(), Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(outputStream);
            List<Todo> todos = new ArrayList<>();
            Todo sample_completed = new Todo("sample completed todo");
            sample_completed.setComplete();
            todos.add(sample_completed);
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
     * Method for saving the edited to-do.
     * @param edit the edited to-do
     * @param pos position
     * @param context
     */
    public void editTodo(Todo edit, int pos, Context context){
        List<Todo> todos = Serialize.loadTodos(context);
        todos.set(pos, edit);
        Serialize.saveTodos(todos, context);
    }

    /**
     * Delete a to-do from the data file.
     * @param todo
     * @param context
     */
    public void deleteTodo(Todo todo, Context context) {
        List<Todo> todos = Serialize.loadTodos(context);
        if (todos.contains(todo)) {
            todos.remove(todos.indexOf(todo));
        }
        Serialize.saveTodos(todos, context);
    }

    /**
     * change complete status to opposite
     * @param todo
     * @param context
     */
    public void setTodoToOpposite(Todo todo, Context context) {
        List<Todo> todos = Serialize.loadTodos(context);
        if (todos.contains(todo)) {
            if (!todo.isCompleted()) {
                todos.get(todos.indexOf(todo)).setComplete();
            }
            if (todo.isCompleted()) {
                todos.get(todos.indexOf(todo)).setunComplete();
            }
            Serialize.saveTodos(todos, context);
        }

    }
}

package com.example.getitdone;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Group of helper methods to keep things like the data file name consistent.
 * @author Chan Tun Aung
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
            case Completed:
                for (Todo t : todos) {
                    if (t.isCompleted()) filteredTodo.add(t);
                }
                break;

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
        String dirName = context.getResources().getString(R.string.data_file_directory);
        File directory = context.getDir(dirName, Context.MODE_PRIVATE);
        File dataFile = new File(directory, filename);
        try {
            dataFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataFile;
    }

    /**
     * Add a new to-do to the data file.
     * @param newTodo
     * @param context
     */
    public void addNewTodo(Todo newTodo, Context context) {
        List<Todo> todos = Serialize.loadTodos(context);
        todos.add(newTodo);
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

    public void setTodoAsCompleted(Todo todo, Context context) {
        List<Todo> todos = Serialize.loadTodos(context);
        if (todos.contains(todo)) {
            todos.get(todos.indexOf(todo)).setComplete();
        }
        Serialize.saveTodos(todos, context);
    }
}

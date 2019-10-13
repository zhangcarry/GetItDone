package com.example.getitdone;

import android.content.Context;

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
        List<Todo> todos = Serialize.loadTodos(getDataFile(context), context);
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
     * Returns the name of the data file where the serialized list of to-dos are stored.
     * Please use this method to get the name of the data file instead of directly referencing
     * the name of the data file.
     * @param context
     * @return to-do data file name
     */
    public String getDataFile(Context context) {
        return context.getResources().getString(R.string.todos_data_file);
    }
}

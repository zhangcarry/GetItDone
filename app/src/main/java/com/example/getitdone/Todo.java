package com.example.getitdone;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Class that represents a single to-do.
 * @author Chan Tun Aung (u6777573)
 * @author Zijing Que (u6469732)
 */
public class Todo implements Serializable {

    private static final long serialVersionUID = 14102019540L;

    /**
     * Various information about the to-do.
     */
    private String todoName;
    private String dueDate;
    private String dueTime;

    private boolean completed = false;

    public Todo(String todoName, String dueDate, String dueTime, int priority, Calendar calendar) {
        this.todoName = todoName;
        this.dueDate = dueDate;
        this.dueTime = dueTime;
        this.completed = false;
    }

    /**
     * For to-dos without due date and no priority set.
     * Priority will default to lowest (i.e. 5).
     * @param todoName name of the to-do
     */
    public Todo(String todoName) {
        this.todoName = todoName;
        this.dueDate = null;  // no due-date
        this.dueTime = null;
    }

    public Todo() {}

    public boolean isCompleted() {return  this.completed; }

    public String getName() {
        return this.todoName;
    }


    /**
     * Returns the due date.
     * This method could return null so caller function should always
     * check the return value.
     * @return due-date of to-do
     */
    public String getDueDate() {
        return this.dueDate;
    }
    public String getDueTime() {
        return this.dueTime;
    }


    public void setComplete() { this.completed = true; }
    public void setunComplete() { this.completed = false; }

    public void setName(String newName) {
        this.todoName = newName;
    }

    public void setDueDate(String newDate) {
        this.dueDate = newDate;
    }

    public void setDueTime(String newTime) {
        this.dueTime = newTime;
    }



    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        Todo otherTodo = (Todo) obj;
        return this.todoName.equals(otherTodo.todoName) &&
                (this.dueDate.equals(otherTodo.dueDate)) || (this.dueDate == null && otherTodo.dueDate == null) &&
                (this.dueTime.equals(otherTodo.dueTime)) || (this.dueTime == null && otherTodo.dueTime == null);
    }

    @Override
    public int hashCode() {
        StringBuilder hashStr = new StringBuilder();
        hashStr.append(this.todoName);
        hashStr.append(this.dueDate);
        hashStr.append(this.dueTime);
        hashStr.append(this.completed);
        return hashStr.hashCode();
    }

    @Override
    public String toString() {
        return this.todoName;
    }
}

package com.example.getitdone;

import java.io.Serializable;
import java.util.Date;

public class Todo implements Serializable {

    private static final long serialVersionUID = 6102019300L;

    /**
     * Various information about the to-do.
     */
    private String todoName;
    private Date dueDate;
    private int priority;  // 1 - highest priority, 5 - lowest priority

    public Todo(String todoName, Date dueDate, int priority) {
        this.todoName = todoName;
        this.dueDate = dueDate;
        this.priority = priority;
    }

    /**
     * For to-dos without due date and no priority set.
     * Priority will default to lowest (i.e. 5).
     * @param todoName name of the to-do
     */
    public Todo(String todoName) {
        this.todoName = todoName;
        this.dueDate = null;  // no due-date
        this.priority = 5;
    }

    public Todo() {}

    public String getName() {
        return this.todoName;
    }

    public int getPriority() {
        return this.priority;
    }

    /**
     * Returns the due date.
     * This method could return null so caller function should always
     * check the return value.
     * @return due-date of to-do
     */
    public Date getDueDate() {
        return this.dueDate;
    }

    public void setName(String newName) {
        this.todoName = newName;
    }

    public void setDueDate(Date newDate) {
        this.dueDate = newDate;
    }

    public void setPriority(int newPriority) {
        this.priority = newPriority;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        Todo otherTodo = (Todo) obj;
        return this.todoName.equals(otherTodo.todoName) &&
                this.dueDate.equals(otherTodo.dueDate) &&
                this.priority == otherTodo.priority;
    }

    @Override
    public int hashCode() {
        StringBuilder hashStr = new StringBuilder();
        hashStr.append(this.todoName);
        hashStr.append(this.dueDate.toString());
        hashStr.append(Integer.toString(this.priority));
        return hashStr.hashCode();
    }
}

package com.example.getitdone;

import java.io.Serializable;
import java.util.Date;

/**
 * Class that represents a single to-do.
 * @author Chan Tun Aung (u6777573)
 * @author future author, put your name here
 */
public class Todo implements Serializable {

    private static final long serialVersionUID = 6102019300L;

    /**
     * Various information about the to-do.
     */
    private String todoName;
    private Date dueDate;
    private Date dueTime;
    private int priority;  // 1 - highest priority, 5 - lowest priority

    public Todo(String todoName, Date dueDate, Date dueTime, int priority) {
        this.todoName = todoName;
        this.dueDate = dueDate;
        this.dueTime = dueTime;
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
        this.dueTime = null;
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
    public Date getDueTime() {
        return this.dueTime;
    }

    public void setName(String newName) {
        this.todoName = newName;
    }

    public void setDueDate(Date newDate) {
        this.dueDate = newDate;
    }

    public void setDueTime(Date newTime) {
        this.dueTime = newTime;
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
                this.dueTime.equals(otherTodo.dueTime) &&
                this.priority == otherTodo.priority;
    }

    @Override
    public int hashCode() {
        StringBuilder hashStr = new StringBuilder();
        hashStr.append(this.todoName);
        hashStr.append(this.dueDate.toString());
        hashStr.append(this.dueTime.toString());
        hashStr.append(this.priority);
        return hashStr.hashCode();
    }

    @Override
    public String toString() {
        return this.todoName;
    }
}
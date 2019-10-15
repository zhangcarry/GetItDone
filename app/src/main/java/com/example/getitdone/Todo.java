package com.example.getitdone;

import java.io.Serializable;
import java.util.Date;

/**
 * Class that represents a single to-do.
 * @author Chan Tun Aung (u6777573)
 * @author future author, put your name here
 */
public class Todo implements Serializable {

    private static final long serialVersionUID = 14102019540L;

    /**
     * Various information about the to-do.
     */
    private String todoName;
    private String dueDate;
    private String dueTime;
    private int priority;  // 1 - highest priority, 5 - lowest priority
    private boolean completed = false;

    public Todo(String todoName, String dueDate, String dueTime, int priority) {
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

    public boolean isCompleted() {return  this.completed; }

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
                this.priority == otherTodo.priority &&
                this.isCompleted() == otherTodo.isCompleted();
    }

    @Override
    public int hashCode() {
        StringBuilder hashStr = new StringBuilder();
        hashStr.append(this.todoName);
        hashStr.append(this.dueDate);
        hashStr.append(this.dueTime);
        hashStr.append(this.priority);
        hashStr.append(this.completed);
        return hashStr.hashCode();
    }

    @Override
    public String toString() {
        return this.todoName;
    }
}

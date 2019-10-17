package com.example.getitdone;

import org.junit.Test;
import static org.junit.Assert.*;

public class testTodo {

    String name = "test";
    String date = "17/10/19";
    String time = "11:29 pm";

    public Todo createTodo(){
        Todo td = new Todo();
        td.setName(name);
        td.setDueDate(date);
        td.setDueTime(time);
        td.setComplete();
        return td;
    }
    public Todo createAnotherTodo(){
        Todo td = new Todo();
        td.setName("differentName");
        td.setDueDate(date);
        td.setDueTime(time);
        td.setComplete();
        return td;
    }

    @Test
    public void getNameTest(){
        Todo toTest = createTodo();
        String getName = toTest.getName();
        assertEquals(getName, name);
    }

    @Test
    public void getDateTest(){
        Todo toTest = createTodo();
        String getDate = toTest.getDueDate();
        assertEquals(date, getDate);
    }
    @Test
    public void getTimeTest(){
        Todo toTest = createTodo();
        String getTime = toTest.getDueTime();
        assertEquals(time, getTime);
    }

    @Test
    public void setName(){
        Todo toTest = createTodo();
        String newName = "newName";
        toTest.setName(newName);
        String getName = toTest.getName();
        assertEquals(newName, getName);
    }

    @Test
    public void setDate(){
        Todo toTest = createTodo();
        String newDate = "20/07/20";
        toTest.setName(newDate);
        String getDate = toTest.getName();
        assertEquals(newDate, getDate);
    }

    @Test
    public void setTime(){
        Todo toTest = createTodo();
        String newTime = "6:30 pm";
        toTest.setName(newTime);
        String getTime = toTest.getName();
        assertEquals(newTime, getTime);
    }

    @Test
    public void compareTest(){
        Todo toTest = createTodo();
        Todo diffrentNameTodo = createAnotherTodo();
        assertFalse(toTest.equals(diffrentNameTodo));
    }

}

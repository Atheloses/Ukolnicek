package com.example.tasks.ui.tasks;

import java.util.Date;

public class TasksEntry {
    public final String Name;
    public final Integer ID;
    public final Date Time;

    public TasksEntry(Integer ID, String name, Date time) {
        this.Name = name;
        this.ID = ID;
        this.Time = time;
    }
}

package com.example.tasks;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.tasks.ui.tasks.TasksEntry;

import java.util.ArrayList;
import java.util.Date;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "DB.tasks";
    private static final String TASKS_TABLE = "tasks";
    private static final String TASKS_NAME = "name";
    private static final String TASKS_DEADLINE = "time";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+ TASKS_TABLE + "(id INTEGER PRIMARY KEY, "+ TASKS_NAME +" TEXT, "+ TASKS_DEADLINE +" TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TASKS_TABLE);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TASKS_TABLE);
        onCreate(db);
    }

    public int insertTasks(ArrayList<TasksEntry> input){
        int count = 0;
        for (TasksEntry entry : input)
            if(insertTask(entry.Name,entry.Time))
                count++;

        return count;
    }

    public boolean insertTask(String name, Date time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASKS_NAME, name);
        contentValues.put(TASKS_DEADLINE, time.getTime());
        long insertedId = db.insert(TASKS_TABLE, null, contentValues);
        return insertedId != -1;
    }

    public void deleteTasks(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TASKS_TABLE, "id = " + id, null);
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select * from "+ TASKS_TABLE +" where id=" + id + "", null);
    }

    public void updateTasks(Integer id, String name, Date time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASKS_NAME, name);
        contentValues.put(TASKS_DEADLINE,time.getTime());
        db.update(TASKS_TABLE, contentValues, "id = " + id, null);
    }

    public ArrayList<TasksEntry> getAllTasks() {
        ArrayList<TasksEntry> output = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from "+ TASKS_TABLE, null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            String name = res.getString(res.getColumnIndex(TASKS_NAME));
            String time = res.getString(res.getColumnIndex(TASKS_DEADLINE));
            int id = res.getInt(0);
            output.add(new TasksEntry(id, name,new Date(Long.parseLong(time))));
            res.moveToNext();
        }
        res.close();

        return output;
    }

    public int removeAllTasks() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TASKS_TABLE, "", null);
    }
}

package com.example.ukolnicek;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ukolnicek.ui.gallery.UkolEntry;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "DBTAMZ.ukolnicek";

    public static ArrayList<String> arrayList = new ArrayList<String>();

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE ukoly " + "(id INTEGER PRIMARY KEY, name TEXT, type INTEGER, cost INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS ukoly");
        onCreate(db);
    }

    public boolean insertUkol(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        long insertedId = db.insert("ukoly", null, contentValues);
        if (insertedId == -1) return false;
        return true;
    }

    public boolean deleteUkol(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("ukoly", "id = " + id, null);
        return true;
    }

    //Cursor representuje vracena data
    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from ukoly where id=" + id + "", null);
        return res;
    }

    public boolean updateUkol(Integer id, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        db.update("ukoly", contentValues, "id = " + id, null);
        return true;
    }

    public ArrayList<UkolEntry> getAllUkol() {
        ArrayList<UkolEntry> output = new ArrayList<UkolEntry>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from ukoly", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            String name = res.getString(res.getColumnIndex("name"));
            int id = res.getInt(0);
            output.add(new UkolEntry(id, name));
            res.moveToNext();
        }

        output.add(new UkolEntry(-1, "test1"));
        output.add(new UkolEntry(-2, "test2"));

        return output;
    }

    public int removeAllUkol() {
        SQLiteDatabase db = this.getWritableDatabase();
        int nRecordDeleted = db.delete("ukoly", "", null);
        return nRecordDeleted;
    }


}

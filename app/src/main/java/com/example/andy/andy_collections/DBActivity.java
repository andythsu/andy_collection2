package com.example.andy.andy_collections;

/**
 * Created by Andy on 2017-06-27.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.Settings;

public class DBActivity extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "andy_collections_db";
    public static final String TABLE_NAME = "collections";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "LOCATION";

    public DBActivity(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            sqLiteDatabase.execSQL("create table " + TABLE_NAME + "(" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_2 + " TEXT, " + COL_3 + " TEXT)");
            System.out.println("table created successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage().toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        try {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            System.out.println("Table dropped successfully");
            onCreate(sqLiteDatabase);
        } catch (SQLException e) {
            System.out.println(e.getMessage().toString());
        }
    }

    public boolean insertData(String name, String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, location);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }

    public boolean deleteById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, COL_1 + "=?", new String[]{Integer.toString(id)});
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean update(String name, String location, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, location);
        long result = db.update(TABLE_NAME, contentValues, COL_1 + " = ? " , new String[]{Integer.toString(id)});
        if (result > 0){
            return true;
        }else{
            return false;
        }
    }

    public Cursor getDataById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_1 + " = " + id + " LIMIT 1";
        System.out.println(sql);
        Cursor res = db.rawQuery(sql, null);
        return res;
    }


}

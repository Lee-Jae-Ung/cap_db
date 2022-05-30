package com.example.capstone.DB;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DDbOpenHelper {

    private static final String DATABASE_NAME = "Device.db";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase mDB;
    private DatabaseHelper mDBHelper;
    private Context mCtx;

    public static class DatabaseHelper extends SQLiteOpenHelper{

        public DatabaseHelper(Context context, String name, CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL(DataBases.CreateDDB._CREATE0);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+DataBases.CreateDDB._TABLENAME0);
            onCreate(db);
        }
    }

    public DDbOpenHelper(Context context){
        this.mCtx = context;
    }

    public DDbOpenHelper open() throws SQLException{
        mDBHelper = new DatabaseHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
        mDB = mDBHelper.getWritableDatabase();
        return this;
    }

    public void create(){
        mDBHelper.onCreate(mDB);
    }

    public void close(){
        mDB.close();
    }

    // Insert DB
    public long insertColumn(String section,String pointid,String pointnum,String devicename){
        ContentValues values = new ContentValues();
        values.put(DataBases.CreateDDB.SECTION, section);
        values.put(DataBases.CreateDDB.POINTID, pointid);
        values.put(DataBases.CreateDDB.POINTNUM, pointnum);
        values.put(DataBases.CreateDDB.DEVICENAME, devicename);
        return mDB.insert(DataBases.CreateDDB._TABLENAME0, null, values);
    }


    // Update DB
    public boolean updateColumn(long id, String section,String pointid,String pointnum,String devicename){
        ContentValues values = new ContentValues();
        values.put(DataBases.CreateDDB.SECTION, section);
        values.put(DataBases.CreateDDB.POINTID, pointid);
        values.put(DataBases.CreateDDB.POINTNUM, pointnum);
        values.put(DataBases.CreateDDB.DEVICENAME, devicename);
        return mDB.update(DataBases.CreateDDB._TABLENAME0, values, "_id=" + id, null) > 0;
    }

    // Delete All
    public void deleteAllColumns() {
        mDB.delete(DataBases.CreateDDB._TABLENAME0, null, null);
    }

    // Delete DB
    public boolean deleteColumn(long id){
        return mDB.delete(DataBases.CreateDDB._TABLENAME0, "_id="+id, null) > 0;
    }
    // Select DB
    public Cursor selectColumns(){
        return mDB.query(DataBases.CreateDDB._TABLENAME0, null, null, null, null, null, null);
    }


    // sort by column
    public Cursor sortColumn(String sort){
        Cursor c = mDB.rawQuery( "SELECT * FROM device_list ORDER BY " + sort + ";", null);
        return c;
    }

    public Cursor sortColumnDist(){
        Cursor c = mDB.rawQuery( "SELECT DISTINCT section, location ,ip FROM device_list ORDER BY section;", null);
        return c;
    }

    public Cursor selectDevice(String section_str){
        Cursor c = mDB.rawQuery( "SELECT * FROM device_list WHERE section =" + section_str + " order by pointid;", null);
        return c;
    }


}


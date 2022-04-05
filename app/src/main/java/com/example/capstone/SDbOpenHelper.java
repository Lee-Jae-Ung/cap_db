package com.example.capstone;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SDbOpenHelper {

    private static final String DATABASE_NAME = "Section.db";
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
            db.execSQL(DataBases.CreateSDB._CREATE0);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+DataBases.CreateSDB._TABLENAME0);
            onCreate(db);
        }
    }

    public SDbOpenHelper(Context context){
        this.mCtx = context;
    }

    public SDbOpenHelper open() throws SQLException{
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
    public long insertColumn(String section, String location, String ip){
        ContentValues values = new ContentValues();
        values.put(DataBases.CreateSDB.SECTION, section);
        values.put(DataBases.CreateSDB.LOCATION, location);
        values.put(DataBases.CreateSDB.IP, ip);
        return mDB.insert(DataBases.CreateSDB._TABLENAME0, null, values);
    }


    // Update DB
    public boolean updateColumn(long id, String section, String location, String ip){
        ContentValues values = new ContentValues();
        values.put(DataBases.CreateSDB.SECTION, section);
        values.put(DataBases.CreateSDB.LOCATION, location);
        values.put(DataBases.CreateSDB.IP, ip);

        return mDB.update(DataBases.CreateSDB._TABLENAME0, values, "_id=" + id, null) > 0;
    }

    // Delete All
    public void deleteAllColumns() {
        mDB.delete(DataBases.CreateSDB._TABLENAME0, null, null);
    }

    // Delete DB
    public boolean deleteColumn(long id){
        return mDB.delete(DataBases.CreateSDB._TABLENAME0, "_id="+id, null) > 0;
    }
    // Select DB
    public Cursor selectColumns(){
        return mDB.query(DataBases.CreateSDB._TABLENAME0, null, null, null, null, null, null);
    }


    // sort by column
    public Cursor sortColumn(String sort){
        Cursor c = mDB.rawQuery( "SELECT * FROM section_list ORDER BY " + sort + ";", null);
        return c;
    }

    public Cursor sortColumnDist(){
        Cursor c = mDB.rawQuery( "SELECT DISTINCT section, location ,ip FROM section_list ORDER BY section;", null);
        return c;
    }

    public Cursor selectDevice(String section_str){
        Cursor c = mDB.rawQuery( "SELECT devicename FROM section_list WHERE section =" + section_str + ";", null);
        return c;
    }


}

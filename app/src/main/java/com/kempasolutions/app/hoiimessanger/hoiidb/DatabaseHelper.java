package com.kempasolutions.app.hoiimessanger.hoiidb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.Settings;

import com.kempasolutions.app.hoiimessanger.MessageType;


/**
 * Created by Archana on 8/9/2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 8;
    public static final String DATABASE_NAME = "hoiidb.db";
    private static final String NUMERIC_TYPE = " INTEGER";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + HoiiDbContract.Roaster.TABLE_NAME + " (" +
                    HoiiDbContract.Roaster.COLUMN_NAME_PHONE + TEXT_TYPE + " PRIMARY KEY" + COMMA_SEP +
                    HoiiDbContract.Roaster.COLUMN_NAME_STATUS + TEXT_TYPE + COMMA_SEP +
                    HoiiDbContract.Roaster.COLUMN_NAME_LAST_SEEN + TEXT_TYPE + COMMA_SEP +
                    HoiiDbContract.Roaster.COLUMN_NAME_PROFILE_PIC + TEXT_TYPE +
                    " );"
                    + " CREATE TABLE " + HoiiDbContract.Message.TABLE_NAME + " (" +
                    HoiiDbContract.Message._ID + " TEXT PRIMARY KEY," +
                    HoiiDbContract.Message.COLUMN_NAME_SENT_RECIEVE + NUMERIC_TYPE + COMMA_SEP +
                    HoiiDbContract.Message.COLUMN_NAME_FROM_TO + TEXT_TYPE + COMMA_SEP +
                    HoiiDbContract.Message.COLUMN_NAME_TYPE + NUMERIC_TYPE + COMMA_SEP +
                    HoiiDbContract.Message.COLUMN_NAME_BODY + TEXT_TYPE + COMMA_SEP +
                    HoiiDbContract.Message.COLUMN_NAME_DATE_TIME + TEXT_TYPE +
                    " )";


    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + HoiiDbContract.Roaster.TABLE_NAME + ";" +
                    "DROP TABLE IF EXISTS " + HoiiDbContract.Message.TABLE_NAME + ";";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println(SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println(SQL_CREATE_ENTRIES);
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public boolean insertMessage(String msgId, int sent_rcvd, String from_to, int type, String body, String dateTime) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        /*ContentValues values = new ContentValues();
        values.put(HoiiDbContract.Message._ID, msgId);
        values.put(HoiiDbContract.Message.COLUMN_NAME_SENT_RECIEVE, sent_rcvd);
        values.put(HoiiDbContract.Message.COLUMN_NAME_FROM_TO, from_to);
        values.put(HoiiDbContract.Message.COLUMN_NAME_TYPE, type);
        values.put(HoiiDbContract.Message.COLUMN_NAME_BODY, body);
        values.put(HoiiDbContract.Message.COLUMN_NAME_DATE_TIME, dateTime);


        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                HoiiDbContract.Message.TABLE_NAME, "",
                values);*/
        String qry = "insert into " + HoiiDbContract.Message.TABLE_NAME + " (" + HoiiDbContract.Message._ID + ","
                + HoiiDbContract.Message.COLUMN_NAME_SENT_RECIEVE+","+ HoiiDbContract.Message.COLUMN_NAME_FROM_TO+","
                + HoiiDbContract.Message.COLUMN_NAME_TYPE+","+ HoiiDbContract.Message.COLUMN_NAME_BODY+","
                + HoiiDbContract.Message.COLUMN_NAME_DATE_TIME+") values ('"+msgId+"',"+sent_rcvd+",'"+from_to+"',"+type+",'"
                +body+"','"+dateTime+"');";
        db.execSQL(qry);

        return true;
    }
public boolean getMessages(){
    SQLiteDatabase db = this.getReadableDatabase();
   //Cursor c= db.rawQuery("select "+ HoiiDbContract.Message.COLUMN_NAME_FROM_TO+" from "+HoiiDbContract.Message.TABLE_NAME ,null);
   Cursor c= db.rawQuery("SELECT * FROM hoiidb.sqlite_master WHERE type='table';",null);
    c.moveToFirst();
    System.out.println(String.valueOf(c.getColumnCount()));
    return true;
}
    public boolean insertRoaster(String phone, String status, String lastSeen, String profilePic) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(HoiiDbContract.Roaster.COLUMN_NAME_PHONE, phone);
        values.put(HoiiDbContract.Roaster.COLUMN_NAME_STATUS, status);
        values.put(HoiiDbContract.Roaster.COLUMN_NAME_LAST_SEEN, lastSeen);
        values.put(HoiiDbContract.Roaster.COLUMN_NAME_PROFILE_PIC, profilePic);


        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                HoiiDbContract.Roaster.TABLE_NAME, "",
                values);

        return true;
    }

    public Cursor getAllRoaster() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + HoiiDbContract.Roaster.TABLE_NAME, null);
        return res;
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}

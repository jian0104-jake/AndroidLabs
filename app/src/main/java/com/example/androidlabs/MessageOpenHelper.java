package com.example.androidlabs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MessageOpenHelper extends SQLiteOpenHelper {

    protected final static String DATABASE_NAME = "MessageDB";
    protected final static int VERSION_NUM = 1;
    protected final static String TABLE_NAME = "MESSAGES";
    protected final static String COL_MESSAGE = "MESSAGE";
    protected final static String COL_IS_SEND = "IS_SEND";
    protected final static String COL_ID = "_id";

    public MessageOpenHelper(Context ctx){
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_MESSAGE + " text,"
                + COL_IS_SEND  + " INTEGER);");  // add or remove columns
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}

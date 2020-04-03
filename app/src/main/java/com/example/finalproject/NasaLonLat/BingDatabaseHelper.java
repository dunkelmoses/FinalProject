package com.example.finalproject.NasaLonLat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/**
 * Helper class for managing the database.
 */
public class BingDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "NasaLonLat";
    private static final int VERSION_NUM = 2;

    private static final String TABLE_NAME = "LonLatTable";
    static final String COL_ID = "ID";
    static final String COL_LAT = "LAT";
    static final String COL_LON = "LON";
    static final String COL_URL = "URL";

    BingDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_NAME+" (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_URL + " TEXT," +
                COL_LAT +" TEXT,"+
                COL_LON + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    Cursor getAll() {
        return getReadableDatabase().rawQuery("SELECT * FROM "+TABLE_NAME, null);
    }

    long insertData(ContactLonLat bingImage) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_URL, bingImage.getPath());
        contentValues.put(COL_LAT, bingImage.getLatitude());
        contentValues.put(COL_LON, bingImage.getLongitude());
        return getWritableDatabase().insert(TABLE_NAME, null, contentValues);
    }




    long deleteData(ContactLonLat bingImage) {
        return getWritableDatabase().delete(TABLE_NAME,COL_ID+"=?",new String[]{String.valueOf(bingImage.getId())});
    }


    void update(ContactLonLat bingImage) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_URL, bingImage.getPath());
        contentValues.put(COL_LAT, bingImage.getLatitude());
        contentValues.put(COL_LON, bingImage.getLongitude());
        getWritableDatabase().update(TABLE_NAME, contentValues, COL_ID+"="+bingImage.getId(), null);
    }

}

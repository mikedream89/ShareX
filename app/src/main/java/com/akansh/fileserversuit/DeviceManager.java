package com.akansh.fileserversuit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DeviceManager extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="FSX_DeviceList.db";
    public static final String TABLE_NAME="D_LIST";

    public static final String DEVICE_ID="DEVICE_ID";
    public static final String DEVICE_TYPE="DEVICE_TYPE";

    public DeviceManager(Context context) {
        super(context,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_NAME+" (ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,DEVICE_ID TEXT NOT NULL,DEVICE_TYPE INT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
    }

    public boolean addDevice(String id,int type) {
        if(id.length()>0) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(DEVICE_ID, id);
            contentValues.put(DEVICE_TYPE, type);
            long result = db.insert(TABLE_NAME, null, contentValues);
            return !(result == -1);
        }else{
            return false;
        }
    }

    public int getRemDevices() {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("SELECT 1 FROM "+TABLE_NAME+" WHERE DEVICE_TYPE=?", new String[]{String.valueOf(Constants.DEVICE_TYPE_PERMANENT)});
        try {
            return res.getCount();
        } finally {
            res.close();
        }
    }

    public boolean isDeviceExist(String id) {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("SELECT 1 FROM "+TABLE_NAME+" WHERE DEVICE_ID=? AND DEVICE_TYPE!=?", new String[]{id, String.valueOf(Constants.DEVICE_TYPE_DENIED)});
        try {
            return (res.getCount()>0);
        } finally {
            res.close();
        }
    }

    public boolean isDeviceDenied(String id) {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("SELECT 1 FROM "+TABLE_NAME+" WHERE DEVICE_ID=? AND DEVICE_TYPE=?", new String[]{id, String.valueOf(Constants.DEVICE_TYPE_DENIED)});
        try {
            return (res.getCount()>0);
        } finally {
            res.close();
        }
    }

    public void clearAll() {
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_NAME,null,null);
    }

    public void clearTmp() {
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_NAME, "DEVICE_TYPE=? OR DEVICE_TYPE=?", new String[]{String.valueOf(Constants.DEVICE_TYPE_TEMP), String.valueOf(Constants.DEVICE_TYPE_DENIED)});
    }
}

package ru.hotel72.accessData;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Evgeny
 * Date: 17.02.13
 * Time: 10:02
 * To change this template use File | Settings | File Templates.
 */
public class DBHelper{

    private final DBOpenHelper dbOpenHelper;

    public DBHelper(Context context) {
        dbOpenHelper = new DBOpenHelper(context);
    }

    public ArrayList<Integer> getLikedFlats(){
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        Cursor c = db.query("liked_flat", null, null, null, null, null, null);

        ArrayList<Integer> result = new ArrayList<Integer>();

        if (c.moveToFirst()) {
            int flatIdColIndex = c.getColumnIndex("flat_id");

            do {
                result.add(c.getInt(flatIdColIndex));
            } while (c.moveToNext());
        }
        c.close();
        dbOpenHelper.close();

        return result;
    }

    public void addLickedFlat(Integer id) {
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        cv.put("flat_id", id);
        db.insert("liked_flat", null, cv);

        dbOpenHelper.close();
    }

    public void removeLickedFlat(Integer id){
        String where = "flat_id = ?";
        String[] whereArgs = {id.toString()};

        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        db.delete("liked_flat", where, whereArgs);

        dbOpenHelper.close();
    }

    private class DBOpenHelper extends SQLiteOpenHelper {
        public DBOpenHelper(Context context) {
            super(context, "hotel72DB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d("DataBase Creation", "initial dataBase creation");
            db.execSQL("create table liked_flat ("
                    + "id integer primary key autoincrement,"
                    + "flat_id integer"
                    + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

        }
    }
}

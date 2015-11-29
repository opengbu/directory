package in.darbose.gbu.directory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "user";
    public static final String KEY_FNAME = "fname";
    public static final String KEY_LNAME = "lname";
    public static final String KEY_ID = "id";
    static String DATABASE_NAME = "userdata";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_FNAME + " TEXT, " + KEY_LNAME + " TEXT)";
        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    /**
     * Inserts User into SQLite DB
     *
     * @param queryValues
     */
    public void insertUser(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, queryValues.get("userId"));
        values.put(KEY_FNAME, queryValues.get("userName"));
        values.put(KEY_LNAME, queryValues.get("userPhone"));
        database.insert(TABLE_NAME, null, values);
        database.close();
    }


    /**
     * Get list of Users from SQLite DB as Array List
     *
     * @return
     */
    public ArrayList<HashMap<String, String>> getAllUsers() {
        ArrayList<HashMap<String, String>> usersList;
        usersList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("userId", cursor.getString(0));
                map.put("userName", cursor.getString(1));
                map.put("userPhone", cursor.getString(2));
                usersList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        return usersList;
    }

}
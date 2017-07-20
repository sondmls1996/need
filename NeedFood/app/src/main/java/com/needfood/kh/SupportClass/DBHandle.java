package com.needfood.kh.SupportClass;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.needfood.kh.Constructor.Listuser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gau on 10/01/2017.
 */

public class DBHandle extends SQLiteOpenHelper {
    public  static final String TAG = DBHandle.class.getSimpleName();
    public  static final String DB_NAME = "needfood.db";
    public  static final int DB_VERSION = 1;
    public  static final String USER_TABLE = "user";
    public  static final String UID = "id";
    public  static final String NAME = "fulllname";
    public  static final String EMAIL = "email";
    public  static final String PHONE = "fone";
    public  static final String PASS = "pass";


    public DBHandle(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_USER=
                "CREATE TABLE " + USER_TABLE + "(" +
                        UID + " TEXT NOT NULL," +
                        NAME + " TEXT NOT NULL," +
                        EMAIL + " TEXT," +
                        PHONE + " TEXT," +
                        PASS + " TEXT" +
                       ");";

        db.execSQL(CREATE_TABLE_USER);

        Log.d("TAG",TAG);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + USER_TABLE);

        onCreate(db);
    }

    public void addUser(Listuser listu) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, listu.getName()); // Contact Name
        values.put(EMAIL, listu.getEmail()); // Contact Phone
        values.put(PHONE, listu.getPhone());
        values.put(PASS, listu.getPass());

        db.insert(USER_TABLE, null, values);
        db.close(); // Closing database connection
    }
    public boolean updateinfo (String name, String email,String phone,String userid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, name );
        values.put(EMAIL, email);
        values.put(PHONE, phone);

        db.update(USER_TABLE,values,UID+" = ?",new String[]{userid});
        return true;
    }
    //
//
//
    public List<Listuser> getAllUser() {
        List<Listuser> contactList = new ArrayList<Listuser>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + USER_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Listuser contact = new Listuser();
                contact.setName(cursor.getString(0));
                contact.setEmail(cursor.getString(1));
                contact.setPhone(cursor.getString(2));
                contact.setPass(cursor.getString(3));

                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }
    public void deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(USER_TABLE,null,null);
        db.close();
    }



}
